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
 package org.craftercms.commons.upgrade.impl.operations;

 import org.craftercms.commons.config.ConfigurationException;
 import org.craftercms.commons.upgrade.VersionProvider;
 import org.craftercms.commons.upgrade.exception.UpgradeException;
 import org.junit.Before;
 import org.junit.Test;

 import static org.mockito.Matchers.any;
 import static org.mockito.Matchers.eq;
 import static org.mockito.Mockito.mock;
 import static org.mockito.Mockito.verify;
 import static org.mockito.Mockito.when;

 /**
  * @author joseross
  */
 public class UpdateVersionUpgradeOperationTest {

     public static final String INITIAL_VERSION = "1.0";
     public static final String FINAL_VERSION = "2.0";

     private VersionProvider versionProvider;

     private UpdateVersionUpgradeOperation operation;

     @Before
     public void setUp() throws UpgradeException, ConfigurationException {
         versionProvider = mock(VersionProvider.class);

         operation = new UpdateVersionUpgradeOperation(versionProvider);
         operation.init(INITIAL_VERSION, FINAL_VERSION, null);
     }

     @Test
     public void versionShouldBeUpdated() throws UpgradeException {
         when(versionProvider.getVersion(any())).thenReturn(INITIAL_VERSION);

         operation.execute(null);

         verify(versionProvider).setVersion(any(), eq(FINAL_VERSION));
     }

 }
