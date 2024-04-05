/*
 * Copyright (C) 2007-2024 Crafter Software Corporation. All Rights Reserved.
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

package org.craftercms.commons.freemarker;

import freemarker.ext.jakarta.servlet.HttpRequestParametersHashModel;
import freemarker.template.SimpleHash;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import java.util.Map;

/**
 * Extends {@link FreeMarkerView} to support rendering variables in Spring 6
 */
public class CrafterFreeMarkerView extends FreeMarkerView {

    public static final String KEY_REQUEST_PARAMS = "RequestParameters";

    @Override
    protected SimpleHash buildTemplateModel(final Map<String, Object> model, final HttpServletRequest request,
                                            final HttpServletResponse response) {
        SimpleHash templateModel = super.buildTemplateModel(model, request, response);
        HttpRequestParametersHashModel requestParamsModel = new HttpRequestParametersHashModel(request);
        templateModel.put(KEY_REQUEST_PARAMS, requestParamsModel);

        return templateModel;
    }
}
