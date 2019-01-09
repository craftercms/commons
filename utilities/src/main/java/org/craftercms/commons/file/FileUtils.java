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
package org.craftercms.commons.file;

import java.text.DecimalFormat;

/**
 * General file manipulation utilities.
 */
public class FileUtils {

    /**
     * Prevents a Instance of FileUtils.
     */
    private FileUtils(){}

    /**
     * Given the size of a file outputs as human readable size using SI prefix.
     * <i>Base 1024</i>
     * @param size Size in bytes of a given File.
     * @return SI String representing the file size (B,KB,MB,GB,TB).
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[] {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int)(Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
