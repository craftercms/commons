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

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.List;

/**
 */
public class User extends Person {
    private String username;
    @JsonIgnore
    private String password;
    private List<String> roles;
    private Address address;


    public User() {
        roles = Arrays.asList("Dr", "Dumpster Jedi", "Friend", "Slinky Owner");

        username = "Dr.Zoidberg";
        password = "thisIsAReallyLongPwd";
        address = new Address();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(final List<String> roles) {
        this.roles = roles;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }
}
