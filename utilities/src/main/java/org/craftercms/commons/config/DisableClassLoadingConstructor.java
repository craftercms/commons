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
package org.craftercms.commons.config;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Extension of {@link Constructor} to disable any class from being loaded
 *
 * @author joseross
 * @since 3.1.13
 */
public class DisableClassLoadingConstructor extends Constructor {

    public DisableClassLoadingConstructor() {
    }

    public DisableClassLoadingConstructor(Class<?> theRoot) {
        super(theRoot);
    }

    public DisableClassLoadingConstructor(TypeDescription theRoot) {
        super(theRoot);
    }

    public DisableClassLoadingConstructor(String theRoot) throws ClassNotFoundException {
        super(theRoot);
    }

    @Override
    protected Class<?> getClassForName(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException();
    }

}
