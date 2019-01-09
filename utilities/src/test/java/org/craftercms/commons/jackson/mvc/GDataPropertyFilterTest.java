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

package org.craftercms.commons.jackson.mvc;


import org.craftercms.commons.jackson.mvc.sup.FilterTestController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:/selector-context.xml")
public class GDataPropertyFilterTest {


    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testNoSelector() throws Exception {
        this.mockMvc.perform(get(FilterTestController.SELECTOR) //Url
            .contentType(MediaType.APPLICATION_JSON)) //
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)) // Check that is JSON
            .andExpect(jsonPath("$.name").exists()).andExpect(jsonPath("$.birthday").exists()).andExpect(jsonPath("$"
            + ".id").exists());
    }

    @Test
    public void testSimpleSelector() throws Exception {
        this.mockMvc.perform(get(FilterTestController.SELECTOR + "?selector=Person(name)") //Url
            .contentType(MediaType.APPLICATION_JSON)) //
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)) // Check that is JSON
            .andExpect(jsonPath("$.name").exists()).andExpect(jsonPath("$.birthday").doesNotExist()).andExpect
            (jsonPath("$" + ".id").doesNotExist());
    }

    @Test
    public void testAliasSelector() throws Exception {
        this.mockMvc.perform(get(FilterTestController.SELECTOR + "?selector=:noName") //Url
            .contentType(MediaType.APPLICATION_JSON)) //
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)) // Check that is JSON
            .andExpect(jsonPath("$.name").doesNotExist()).andExpect(jsonPath("$.birthday").exists()).andExpect
            (jsonPath("$" + ".id").exists());

        //Tests if the cache was done (coverage)
        this.mockMvc.perform(get(FilterTestController.SELECTOR + "?selector=:noName") //Url
            .contentType(MediaType.APPLICATION_JSON)) //
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)) // Check that is JSON
            .andExpect(jsonPath("$.name").doesNotExist()).andExpect(jsonPath("$.birthday").exists()).andExpect
            (jsonPath("$" + ".id").exists());
    }

    @Test
    public void testAliasSelectorDontExists() throws Exception {
        this.mockMvc.perform(get(FilterTestController.SELECTOR + "?selector=:IDontExist") //Url
            .contentType(MediaType.APPLICATION_JSON)) //
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)) // Check that is JSON
            .andExpect(jsonPath("$.name").exists()).andExpect(jsonPath("$.birthday").exists()).andExpect(jsonPath("$"
            + ".id").exists());

    }

    @Test
    public void testAliasSelectorIsBroken() throws Exception {
        this.mockMvc.perform(get(FilterTestController.SELECTOR + "?selector=:brokenSelector") //Url
            .contentType(MediaType.APPLICATION_JSON)) //
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)) // Check that is JSON
            .andExpect(jsonPath("$.name").exists()).andExpect(jsonPath("$.birthday").exists()).andExpect(jsonPath("$" +
            ".id").exists());

    }

    @Test
    public void testPasswordIsIgnore() throws Exception {
        this.mockMvc.perform(get(FilterTestController.ALIAS_NESTED_SELECTOR) //Url
            .contentType(MediaType.APPLICATION_JSON)) //
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)) // Check that is JSON
            .andExpect(jsonPath("$.password").doesNotExist()).andExpect(jsonPath("$.username").exists());

    }
}