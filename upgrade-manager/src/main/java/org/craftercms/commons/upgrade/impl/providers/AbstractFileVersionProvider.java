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
 package org.craftercms.commons.upgrade.impl.providers;

 import java.io.File;
 import java.nio.file.Path;
 import java.nio.file.Paths;

 /**
  * Base class for all {@link org.craftercms.commons.upgrade.VersionProvider} that use files
  *
  * @author joseross
  * @since 3.1.5
  */
 public abstract class AbstractFileVersionProvider extends AbstractVersionProvider {

     protected Path getFile(Object target) {
         if (target instanceof String) {
             return Paths.get((String)target);
         } else if (target instanceof File) {
             return ((File)target).toPath();
         } else if (target instanceof Path) {
             return (Path)target;
         } else {
             throw new IllegalArgumentException("Target can't be converted to Path: " + target);
         }
     }

     @Override
     protected String doGetVersion(final Object target) throws Exception {
         Path file = getFile(target);
         return readVersionFromFile(file);
     }

     @Override
     protected void doSetVersion(final Object target, final String version) throws Exception {
         Path file = getFile(target);
         writeVersionToFile(file, version);
     }

     protected abstract String readVersionFromFile(Path file) throws Exception;

     protected abstract void writeVersionToFile(Path file, String version) throws Exception;

 }
