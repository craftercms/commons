 /*
  * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
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
 package org.craftercms.commons.upgrade.impl.pipeline;

 import org.craftercms.commons.upgrade.UpgradeOperation;
 import org.craftercms.commons.upgrade.exception.UpgradeException;
 import org.junit.Before;
 import org.junit.Test;

 import static java.util.Arrays.asList;
 import static org.mockito.Matchers.any;
 import static org.mockito.Mockito.doThrow;
 import static org.mockito.Mockito.mock;
 import static org.mockito.Mockito.verify;

 /**
  * @author joseross
  */
 public class DefaultUpgradePipelineImplTest {

     private UpgradeOperation secondOperation;

     private DefaultUpgradePipelineImpl pipeline;

     @Before
     public void setUp() throws UpgradeException {
         UpgradeOperation firstOperation = mock(UpgradeOperation.class);
         doThrow(UpgradeException.class).when(firstOperation).execute(any());

         secondOperation = mock(UpgradeOperation.class);

         pipeline = new DefaultUpgradePipelineImpl("test", asList(firstOperation, secondOperation));
     }

     @Test(expected = UpgradeException.class)
     public void shouldStopOnFailure() throws UpgradeException {
         pipeline.execute(null);
     }

     @Test
     public void shouldContinueOnFailure() throws UpgradeException {
         pipeline.setContinueOnFailure(true);
         pipeline.execute(null);

         verify(secondOperation).execute(any());
     }

 }
