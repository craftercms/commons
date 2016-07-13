package org.craftercms.commons.mongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by alfonsovasquez on 14/6/16.
 */
public class UpdateHelperTest {

    public static final String EXPECTED_ID = ObjectId.get().toString();
    public static final String EXPECTED_MODIFIER = "{$set: #, $unset: #, $push: #, $pull: #}";
    public static final String EXPECTED_PARAMS = "[{enabled=true}, {attributes.displayName=}, " +
                                                 "{roles={$each=[ADMIN]}}, {roles={$in=[USER]}}]";

    @Mock
    protected CrudRepository<?> repository;
    protected UpdateHelper updateHelper;

    protected String id;
    protected String modifier;
    protected List<Object> params;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();

                id = (String)args[0];
                modifier = (String)args[1];

                params = new ArrayList<>(4);
                params.add(args[4]);
                params.add(args[5]);
                params.add(args[6]);
                params.add(args[7]);

                return null;
            }

        }).when(repository).update(anyString(), anyString(), eq(false), eq(false), anyVararg());

        updateHelper = new UpdateHelper();
    }

    @Test
    public void testExecuteUpdate() throws Exception {
        updateHelper.set("enabled", true);
        updateHelper.unset("attributes.displayName");
        updateHelper.pushAll("roles", Collections.<Object>singletonList("ADMIN"));
        updateHelper.pullAll("roles", Collections.<Object>singletonList("USER"));

        updateHelper.executeUpdate(EXPECTED_ID, repository);

        assertNotNull(id);
        assertEquals(EXPECTED_ID, id);

        assertNotNull(modifier);
        assertEquals(EXPECTED_MODIFIER, modifier);

        assertNotNull(params);
        assertEquals(4, params.size());
        assertEquals(EXPECTED_PARAMS, params.toString());
    }

}
