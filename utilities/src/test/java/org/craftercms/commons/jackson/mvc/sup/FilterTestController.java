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

package org.craftercms.commons.jackson.mvc.sup;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cortiz on 5/26/14.
 */
@Controller
public class FilterTestController {

    public static final String SELECTOR = "/alias";
    public static final String ALIAS_NESTED_SELECTOR = "/aliasSelector";

    @RequestMapping(SELECTOR)
    @ResponseBody
    public Person aliasSelector() {
        return new Person();
    }


    @RequestMapping(ALIAS_NESTED_SELECTOR)
    @ResponseBody
    public User alias() {
        return new User();
    }
}
