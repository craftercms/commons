/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.security.permissions;

import org.craftercms.commons.security.exception.ActionDeniedException;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.exception.PermissionRuntimeException;
import org.craftercms.commons.security.exception.SubjectNotFoundException;
import org.craftercms.commons.security.permissions.annotations.HasPermission;
import org.craftercms.commons.security.permissions.annotations.HasPermissionAnnotationHandler;
import org.craftercms.commons.security.permissions.annotations.SecuredObject;
import org.craftercms.commons.security.permissions.impl.PermissionEvaluatorImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * @author avasquez
 */
public class HasPermissionAnnotationHandlerTest {

    private MockSubjectResolver subjectResolver;
    private HasPermissionAnnotationHandler annotationHandler;
    private MockSecuredService service;

    @Before
    public void setUp() throws Exception {
        subjectResolver = createTestSubjectResolver();
        annotationHandler = createTestAnnotationHandler();
        service = createTestService();
    }

    @Test
    public void testAllowed() throws Exception {
        MockSecuredObject object1 = new MockSecuredObject();
        object1.id = 1;

        subjectResolver.subject = "user1";

        String result = service.doSomethingWithObject(object1);

        assertEquals(String.format("I did something with object '%s'", object1), result);

        subjectResolver.subject = "user2";

        result = service.doAnotherThingWithObjectId(2);

        assertEquals(String.format("I did another thing with object ID '%s'", 2), result);

        result = service.doYetAnotherThingWithNoObject();

        assertEquals("I did yet another thing", result);
    }

    @Test
    public void testNotAllowed() throws Exception {
        subjectResolver.subject = "user1";

        try {
            service.doAnotherThingWithObjectId(1);
            fail("ActionDeniedException expected");
        } catch (ActionDeniedException e) {
            // expected, so continue
        }

        subjectResolver.subject = "user3";

        try {
            service.doYetAnotherThingWithNoObject();
            fail("ActionDeniedException expected");
        } catch (ActionDeniedException e) {
            // expected, so continue
        }
    }

    @Test
    public void testExceptions() throws Exception {
        try {
            service.doSomethingWrongPermissionType();
            fail("PermissionRuntimeException expected");
        } catch (PermissionRuntimeException e) {
            // expected, so continue
        }

        subjectResolver.subject = null;

        try {
            service.doYetAnotherThingWithNoObject();
            fail("PermissionRuntimeException expected");
        } catch (PermissionRuntimeException e) {
            // expected, so continue
        }
    }

    private HasPermissionAnnotationHandler createTestAnnotationHandler() throws PermissionException {
        Map<Class<?>, PermissionEvaluator<?, ?>> evaluators = new HashMap<>(1);
        evaluators.put(MockPermission.class, createTestPermissionEvaluator());

        HasPermissionAnnotationHandler handler = new HasPermissionAnnotationHandler();
        handler.setPermissionEvaluators(evaluators);

        return handler;
    }

    private MockSecuredService createTestService() throws PermissionException {
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(new MockSecuredServiceImpl());
        proxyFactory.addAspect(annotationHandler);

        return proxyFactory.getProxy();
    }

    private PermissionEvaluator<String, Object> createTestPermissionEvaluator() throws PermissionException {
        PermissionEvaluatorImpl<String, Object> evaluator = new PermissionEvaluatorImpl<>();
        evaluator.setSubjectResolver(subjectResolver);
        evaluator.setPermissionResolver(createTestPermissionResolver());

        return evaluator;
    }

    private MockSubjectResolver createTestSubjectResolver() {
        return new MockSubjectResolver();
    }

    private PermissionResolver<String, Object> createTestPermissionResolver() throws PermissionException {
        Permission permission1 = new MockPermission().allow("doSomething");
        Permission permission2 = new MockPermission().allowAny();
        Permission permission3 = new MockPermission().allow("doYetAnotherThing");

        PermissionResolver<String, Object> resolver = mock(PermissionResolver.class);
        when(resolver.getPermission(eq("user1"), any())).thenReturn(permission1);
        when(resolver.getPermission(eq("user2"), any())).thenReturn(permission2);
        when(resolver.getGlobalPermission("user2")).thenReturn(permission3);

        return resolver;
    }

    private static class MockSubjectResolver implements SubjectResolver<String> {

        private String subject;

        @Override
        public String getCurrentSubject() {
            return subject;
        }

    }

    private static class MockPermission extends PermissionBase {

    }

    private static class MockSecuredObject {

        private long id;

        @Override
        public String toString() {
            return "MockSecuredObject{" +
                    "id='" + id + '\'' +
                    '}';
        }

    }

    private interface MockSecuredService {

        String doSomethingWithObject(MockSecuredObject object);

        String doAnotherThingWithObjectId(long id);

        String doYetAnotherThingWithNoObject();

        void doSomethingWrongPermissionType();

    }

    @HasPermission(type = MockPermission.class, action = "doSomething")
    private static class MockSecuredServiceImpl implements MockSecuredService {

        @Override
        public String doSomethingWithObject(@SecuredObject MockSecuredObject object) {
            return String.format("I did something with object '%s'", object);
        }

        @Override
        @HasPermission(type = MockPermission.class, action = "doAnotherThing")
        public String doAnotherThingWithObjectId(@SecuredObject long id) {
            return String.format("I did another thing with object ID '%s'", id);
        }

        @Override
        @HasPermission(type = MockPermission.class, action = "doYetAnotherThing")
        public String doYetAnotherThingWithNoObject() {
            return "I did yet another thing";
        }

        @HasPermission(type = Permission.class, action = "doSomething")
        public void doSomethingWrongPermissionType() {
        }

    }

}
