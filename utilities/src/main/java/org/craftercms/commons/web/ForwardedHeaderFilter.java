/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Extension of {@link org.springframework.web.filter.ForwardedHeaderFilter} that can be disabled via configuration
 *
 * @author joseross
 * @since 3.1.9
 */
public class ForwardedHeaderFilter extends org.springframework.web.filter.ForwardedHeaderFilter {

    protected boolean enabled;

    public ForwardedHeaderFilter(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !enabled || super.shouldNotFilter(request);
    }

}
