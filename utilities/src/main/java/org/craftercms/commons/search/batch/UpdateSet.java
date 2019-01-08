/*
 * Copyright (C) 2007-2017 Crafter Software Corporation.
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
package org.craftercms.commons.search.batch;

import java.util.List;

/**
 * Set of files to add or delete from the index.
 *
 * @author avasquez
 */
public class UpdateSet {

    private List<String> updatePaths;
    private List<String> deletePaths;

    public UpdateSet(List<String> updatePaths, List<String> deletePaths) {
        this.updatePaths = updatePaths;
        this.deletePaths = deletePaths;
    }

    public List<String> getUpdatePaths() {
        return updatePaths;
    }

    public List<String> getDeletePaths() {
        return deletePaths;
    }

}
