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
package org.craftercms.commons.core.processors.impl;

import org.craftercms.core.processors.impl.IncludeDescriptorsProcessor;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Required;

/**
 * Extension of {@link IncludeDescriptorsProcessor} that prevents pages from being included when {@code disablePageInclusion} is true.
 *
 * @author avasquez
 */
public class PageAwareIncludeDescriptorsProcessor extends IncludeDescriptorsProcessor {

    protected boolean disablePageInclusion;
    protected String pagesPathPattern;

    public PageAwareIncludeDescriptorsProcessor() {
        disablePageInclusion = true;
    }

    public void setDisablePageInclusion(boolean disablePageInclusion) {
        this.disablePageInclusion = disablePageInclusion;
    }

    @Required
    public void setPagesPathPattern(String pagesPathPattern) {
        this.pagesPathPattern = pagesPathPattern;
    }

    @Override
    protected boolean isIncludeDisabled(Element includeElement) {
        return super.isIncludeDisabled(includeElement) || (disablePageInclusion && includeElement.getTextTrim().matches(pagesPathPattern));
    }

}
