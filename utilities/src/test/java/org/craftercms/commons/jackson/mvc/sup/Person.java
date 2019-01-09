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

import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by cortiz on 5/26/14.
 */
public class Person {
    private String name;
    private Date birthday;
    private long id;

    public Person() {
        name = RandomStringUtils.randomAlphanumeric(20);
        birthday = new Date();
        id = new Random().nextLong();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(final Date birthday) {
        this.birthday = birthday;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }
}
