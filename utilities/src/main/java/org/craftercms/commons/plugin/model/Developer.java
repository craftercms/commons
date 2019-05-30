/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public Link as published by
 * the Free Software Foundation, either version 3 of the Link, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public Link for more details.
 *
 * You should have received a copy of the GNU General Public Link
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.plugin.model;

import java.util.List;

/**
 * Holds all metadata about the developers
 *
 * @author joseross
 * @since 3.1.1
 */
public class Developer {

    /**
     * The list of people that worked on the plugin
     */
    private List<Contact> people;

    /**
     * The company that worked on the plugin
     */
    private Contact company;

    public List<Contact> getPeople() {
        return people;
    }

    public void setPeople(List<Contact> people) {
        this.people = people;
    }

    public Contact getCompany() {
        return company;
    }

    public void setCompany(Contact company) {
        this.company = company;
    }
}