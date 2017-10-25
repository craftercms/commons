package org.craftercms.commons.validation.validators.impl;

public class SecurePathValidator extends StringValidator {

    public static final String[] DEFAULT_BLACKLISTED_REGEXES = {"^[^:]+:", "[\\/](\\.+|~)", "(\\.+|~)[\\/]"};

    public SecurePathValidator(String targetKey) {
        super(targetKey);

        matchFullInput = false;
        blacklistRegexes = DEFAULT_BLACKLISTED_REGEXES;
    }

}
