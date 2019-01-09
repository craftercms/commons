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
package org.craftercms.commons.spring.resources;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Extension of Spring's {@code Resource} that provides random access to the content.
 *
 * @author avasquez
 */
public interface RangeAwareResource extends Resource {

    /**
     * Returns a range of bytes from the resource's content.
     *
     * @param start the start of the range
     * @param end the end of the range
     *
     * @return an input stream with the content range
     *
     * @throws IOException if an IO error occurs
     */
    InputStream getInputStream(long start, long end) throws IOException;

}
