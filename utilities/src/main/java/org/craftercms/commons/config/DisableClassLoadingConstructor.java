/*
 * Copyright (C) 2007-2021 Crafter Software Corporation. All Rights Reserved.
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

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Extension of {@link Constructor} to disable any class from being loaded
 *
 * @author joseross
 * @since 3.1.13
 */
public class DisableClassLoadingConstructor extends Constructor {

    public DisableClassLoadingConstructor(LoaderOptions loadingConfig) {
        super(loadingConfig);
    }

    public DisableClassLoadingConstructor(Class<?> theRoot, LoaderOptions loadingConfig) {
        super(theRoot, loadingConfig);
    }

    public DisableClassLoadingConstructor(TypeDescription theRoot, LoaderOptions loadingConfig) {
        super(theRoot, loadingConfig);
    }

    public DisableClassLoadingConstructor(String theRoot, LoaderOptions loadingConfig) throws ClassNotFoundException {
        super(theRoot, loadingConfig);
    }

    @Override
    protected Class<?> getClassForName(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException();
    }

}
