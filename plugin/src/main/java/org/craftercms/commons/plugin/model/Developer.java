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
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;

/**
 * Holds all metadata about the developers
 *
 * @author joseross
 * @since 3.1.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Developer {

    /**
     * The list of people that worked on the plugin
     */
    protected List<Contact> people;

    /**
     * The company that worked on the plugin
     */
    protected Contact company;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Developer)) {
            return false;
        }
        final Developer developer = (Developer)o;
        return isEqualCollection(emptyIfNull(people), emptyIfNull(developer.people)) &&
            Objects.equals(company, developer.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(people, company);
    }

    @Override
    public String toString() {
        return "Developer{" + "people=" + people + ", company=" + company + '}';
    }

}