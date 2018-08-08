/*
 * Copyright (C) 2007-2018 Crafter Software Corporation.
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

package org.craftercms.commons.entitlements.model;

/**
 * Holds all entitlement values for a given module.
 *
 * @author joseross
 */
public class Entitlement {

    protected Module module;

    protected int numberOfAssets;
    protected int numberOfDescriptors;
    protected int numberOfItems;
    protected int numberOfUsers;
    protected int numberOfSites;

    public Entitlement() {
    }

    public Entitlement(final Module module) {
        this.module = module;
    }

    public Entitlement(final Module module, final int numberOfAssets, final int numberOfDescriptors, final int
        numberOfItems, final int numberOfUsers, final int numberOfSites) {
        this.module = module;
        this.numberOfAssets = numberOfAssets;
        this.numberOfDescriptors = numberOfDescriptors;
        this.numberOfItems = numberOfItems;
        this.numberOfUsers = numberOfUsers;
        this.numberOfSites = numberOfSites;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(final Module module) {
        this.module = module;
    }

    public int getNumberOfAssets() {
        return numberOfAssets;
    }

    public void setNumberOfAssets(final int numberOfAssets) {
        this.numberOfAssets = numberOfAssets;
    }

    public int getNumberOfDescriptors() {
        return numberOfDescriptors;
    }

    public void setNumberOfDescriptors(final int numberOfDescriptors) {
        this.numberOfDescriptors = numberOfDescriptors;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(final int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(final int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public int getNumberOfSites() {
        return numberOfSites;
    }

    public void setNumberOfSites(final int numberOfSites) {
        this.numberOfSites = numberOfSites;
    }

    @Override
    public String toString() {
        return "Entitlement{" + "module=" + module + ", numberOfAssets=" + numberOfAssets + ", "
            + "numberOfDescriptors=" + numberOfDescriptors + ", numberOfItems=" + numberOfItems + ", numberOfUsers="
            + numberOfUsers + ", numberOfSites=" + numberOfSites + '}';
    }

}
