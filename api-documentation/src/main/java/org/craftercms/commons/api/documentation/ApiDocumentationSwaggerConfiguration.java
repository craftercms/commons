/*
 * Copyright (C) 2007-2013 Crafter Software Corporation.
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

package org.craftercms.commons.api.documentation;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.List;

/**
 * Swagger configuration class.
 *
 * @author Dejan Brkic
 */
@Configuration
@EnableSwagger
public class ApiDocumentationSwaggerConfiguration {

    private SpringSwaggerConfig springSwaggerConfig;

    /**
     * Required to autowire SpringSwaggerConfig
     */
    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;
    }
    @Value("#{'${swaggerSpringMvcPlugin.includePatterns}'.split(',')}")
    private List<String> includePatterns;

    @Value("${swagger.apiTitle}")
    private String apiTitle;

    @Value("${swagger.apiDescription}")
    private String apiDescription;

    @Value("${swagger.apiTermsOfServiceUrl}")
    private String apiTermsOfServiceUrl;

    @Value("${swagger.apiContactEmail}")
    private String apiContactEmail;

    @Value("${swagger.apiLicenceType}")
    private String apiLicenceType;

    @Value("${swagger.apiLicenceUrl}")
    private String apiLicenceUrl;


    @Bean
    public SwaggerSpringMvcPlugin customSwaggerSpringMvcPlugin(){
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
                .apiInfo(apiInfo())
                .includePatterns(includePatterns.toArray(new String[includePatterns.size()]))
                .build();
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(apiTitle, apiDescription, apiTermsOfServiceUrl, apiContactEmail,
                apiLicenceType, apiLicenceUrl);
        return apiInfo;
    }
}
