/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
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

package org.craftercms.commons.ebus.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import reactor.core.Environment;
import reactor.core.support.DefaultEnvironmentSupplier;
import reactor.function.Supplier;
import reactor.spring.factory.CreateOrReuseFactoryBean;

/**
 * {@link org.springframework.context.annotation.ImportBeanDefinitionRegistrar} implementation that configures
 * necessary Reactor components.
 *
 * @author Dejan Brkic
 */
public class EBusBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String DEFAULT_EBUS_ENVIRONMENT_NAME = "ebusEnvironment";

    @Override
    public void registerBeanDefinitions(final AnnotationMetadata annotationMetadata,
                                        final BeanDefinitionRegistry beanDefinitionRegistry) {

        // Create a root Environment
        if (!beanDefinitionRegistry.containsBeanDefinition(DEFAULT_EBUS_ENVIRONMENT_NAME)) {
            BeanDefinitionBuilder envBeanDef = BeanDefinitionBuilder.rootBeanDefinition(CreateOrReuseFactoryBean.class);
            envBeanDef.addConstructorArgValue(DEFAULT_EBUS_ENVIRONMENT_NAME);
            envBeanDef.addConstructorArgValue(Environment.class);

            Supplier<Environment> envSupplier = new DefaultEnvironmentSupplier();
            envBeanDef.addConstructorArgValue(envSupplier);
            beanDefinitionRegistry.registerBeanDefinition(DEFAULT_EBUS_ENVIRONMENT_NAME,
                envBeanDef.getBeanDefinition());
        }

        // Create a EBusBeanAutoConfiguration
        if (!beanDefinitionRegistry.containsBeanDefinition(EBusBeanAutoConfiguration.class.getName())) {
            BeanDefinitionBuilder autoConfigDef = BeanDefinitionBuilder.rootBeanDefinition(EBusBeanAutoConfiguration
                .class);
            beanDefinitionRegistry.registerBeanDefinition(EBusBeanAutoConfiguration.class.getName(),
                autoConfigDef.getBeanDefinition());
        }
    }
}
