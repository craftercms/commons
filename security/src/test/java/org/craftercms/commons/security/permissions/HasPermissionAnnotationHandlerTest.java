/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
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

import java.util.HashMap;
import java.util.Map;

import org.craftercms.commons.security.exception.ActionDeniedException;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.permissions.annotations.HasPermission;
import org.craftercms.commons.security.permissions.annotations.HasPermissionAnnotationHandler;
import org.craftercms.commons.security.permissions.annotations.ProtectedResource;
import org.craftercms.commons.security.permissions.impl.PermissionEvaluatorImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author avasquez
 */
@SuppressWarnings("unchecked")
public class HasPermissionAnnotationHandlerTest {

    private MockSubjectResolver subjectResolver;
    private HasPermissionAnnotationHandler annotationHandler;
    private MockSecuredService service;

    @Before
    public void setUp() throws Exception {
        createTestSubjectResolver();
        createTestAnnotationHandler();
        createTestService();
    }

    @Test
    public void testAllowed() throws Exception {
        MockProtectedResource object1 = new MockProtectedResource();
        object1.id = 1;

        subjectResolver.subject = "user1";

        String result = service.doSomethingWithObject(object1);

        assertEquals(String.format("I did something with resource '%s'", object1), result);

        subjectResolver.subject = "user2";

        result = service.doAnotherThingWithObjectId(2);

        assertEquals(String.format("I did another thing with resource ID '%s'", 2), result);

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
            fail("PermissionException expected");
        } catch (PermissionException e) {
            // expected, so continue
        }

        subjectResolver.subject = null;

        try {
            service.doYetAnotherThingWithNoObject();
            fail("PermissionException expected");
        } catch (PermissionException e) {
            // expected, so continue
        }
    }

    private void createTestAnnotationHandler() throws PermissionException {
        Map<Class<?>, PermissionEvaluator<?, ?>> evaluators = new HashMap<>(1);
        evaluators.put(DefaultPermission.class, createTestPermissionEvaluator());

        annotationHandler = new HasPermissionAnnotationHandler();
        annotationHandler.setPermissionEvaluators(evaluators);
    }

    private void createTestService() throws PermissionException {
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(new MockSecuredServiceImpl());
        proxyFactory.addAspect(annotationHandler);

        service = proxyFactory.getProxy();
    }

    private PermissionEvaluator<String, Object> createTestPermissionEvaluator() throws PermissionException {
        PermissionEvaluatorImpl<String, Object> evaluator = new PermissionEvaluatorImpl<>();
        evaluator.setSubjectResolver(subjectResolver);
        evaluator.setPermissionResolver(createTestPermissionResolver());

        return evaluator;
    }

    private void createTestSubjectResolver() {
        subjectResolver = new MockSubjectResolver();
    }

    private PermissionResolver<String, Object> createTestPermissionResolver() throws PermissionException {
        Permission permission1 = new DefaultPermission().allow("doSomething");
        Permission permission2 = new DefaultPermission().allowAny();
        Permission permission3 = new DefaultPermission().allow("doYetAnotherThing");

        PermissionResolver<String, Object> resolver = mock(PermissionResolver.class);
        when(resolver.getPermission(eq("user1"), any())).thenReturn(permission1);
        when(resolver.getPermission(eq("user2"), any())).thenReturn(permission2);
        when(resolver.getGlobalPermission("user2")).thenReturn(permission3);

        return resolver;
    }

    private interface MockSecuredService {

        String doSomethingWithObject(MockProtectedResource object);

        String doAnotherThingWithObjectId(long id);

        String doYetAnotherThingWithNoObject();

        void doSomethingWrongPermissionType();

    }

    private static class MockSubjectResolver implements SubjectResolver<String> {

        private String subject;

        @Override
        public String getCurrentSubject() {
            return subject;
        }

    }

    private static class MockProtectedResource {

        private long id;

        @Override
        public String toString() {
            return "MockProtectedResource{" +
                "id='" + id + '\'' +
                '}';
        }

    }

    @HasPermission(type = DefaultPermission.class, action = "doSomething")
    private static class MockSecuredServiceImpl implements MockSecuredService {

        @Override
        public String doSomethingWithObject(@ProtectedResource MockProtectedResource object) {
            return String.format("I did something with resource '%s'", object);
        }

        @Override
        @HasPermission(type = DefaultPermission.class, action = "doAnotherThing")
        public String doAnotherThingWithObjectId(@ProtectedResource long id) {
            return String.format("I did another thing with resource ID '%s'", id);
        }

        @Override
        @HasPermission(type = DefaultPermission.class, action = "doYetAnotherThing")
        public String doYetAnotherThingWithNoObject() {
            return "I did yet another thing";
        }

        @HasPermission(type = Permission.class, action = "doSomething")
        public void doSomethingWrongPermissionType() {
        }

    }

}
