/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
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

import javax.servlet.http.HttpServletRequest;
import java.beans.ConstructorProperties;

/**
 * Extension of {@link org.springframework.web.filter.ForwardedHeaderFilter} that can be disabled via configuration
 *
 * @author joseross
 * @since 3.1.9
 */
public class ForwardedHeaderFilter extends org.springframework.web.filter.ForwardedHeaderFilter {

    protected boolean enabled;

    @ConstructorProperties({"enabled"})
    public ForwardedHeaderFilter(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !enabled || super.shouldNotFilter(request);
    }

}
