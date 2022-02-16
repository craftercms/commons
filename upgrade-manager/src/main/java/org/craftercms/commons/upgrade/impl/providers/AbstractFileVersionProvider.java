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
 package org.craftercms.commons.upgrade.impl.providers;

 import org.craftercms.commons.upgrade.impl.UpgradeContext;

 import java.io.File;
 import java.nio.file.Path;
 import java.nio.file.Paths;

 /**
  * Base class for all {@link org.craftercms.commons.upgrade.VersionProvider} that use files
  *
  * @param <T> The target type supported
  * @author joseross
  * @since 3.1.5
  */
 public abstract class AbstractFileVersionProvider<T> extends AbstractVersionProvider<T> {

     protected Path getFile(UpgradeContext<T> context) {
         var target = context.getTarget();
         if (target instanceof String) {
             return Paths.get((String) target);
         } else if (target instanceof File) {
             return ((File) target).toPath();
         } else if (target instanceof Path) {
             return (Path) target;
         } else {
             throw new IllegalArgumentException("Target can't be converted to Path: " + target);
         }
     }

     @Override
     protected String doGetVersion(final UpgradeContext<T> context) throws Exception {
         Path file = getFile(context);
         return readVersionFromFile(file);
     }

     @Override
     protected void doSetVersion(final UpgradeContext<T> context, final String version) throws Exception {
         Path file = getFile(context);
         writeVersionToFile(file, version);
     }

     protected abstract String readVersionFromFile(Path file) throws Exception;

     protected abstract void writeVersionToFile(Path file, String version) throws Exception;

 }
