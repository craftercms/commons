package org.craftercms.commons.validation;

import java.util.ResourceBundle;

import org.craftercms.commons.i10n.I10nUtils;

public class ValidationUtils {

    public static final String DEFAULT_ERROR_BUNDLE_NAME = "crafter.commons.validation.errors";

    private ValidationUtils() {
    }

    public static String getErrorMessage(ResourceBundle bundle, String errorCode, Object... args) {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(DEFAULT_ERROR_BUNDLE_NAME);
        }

        return I10nUtils.getLocalizedMessage(bundle, errorCode, args);
    }

}
