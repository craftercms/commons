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

package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.validators.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Validator;

import static org.craftercms.commons.validation.annotations.param.EsapiValidationType.EMAIL;

public class EmailAddressTest implements ValidatorTest {

    private Validator validator;
    @Override
    public Validator getValidator() {
        return validator;
    }

    @Before
    public void setUp() {
        validator = new EsapiValidator(EMAIL);
    }

    @Test
    public void testValidEmailsRFC() {
        assertValid("simple@example.com");
        assertValid("Sample.With.Capital@example.com");
        assertValid("very.common@example.com");
        assertValid("name.d'uncommon@example.com");
        assertValid("crafter.D'studio@example.com");
        assertValid("disposable.style.email.with+symbol@example.com");
        assertValid("\"very.unusual.@.unusual.com\"@example.com");
        assertValid("admin@mailserver1");
        assertValid("example@s.example");
        assertValid("long.email-address-with-hyphens@and.subdomains.example.com");
        assertValid("user.name+tag+sorting@example.com");
        assertValid("name/surname@example.com");
        assertValid("\" \"@example.org");
        assertValid("\"john..doe\"@example.org");
        assertValid("mailhost!username@example.org");
        assertValid("\"very.(),:;<>[]\\\".VERY.\\\"very@\\\\ \\\"very\\\".unusual\"@strange.example.com");
        assertValid("user%example.com@example.org");
        assertValid("user-@example.org");
        assertValid("postmaster@[123.123.123.123]");
        assertValid("postmaster@[2001:0db8:85a3:0000:0000:8a2e:0370:7334]");
        assertValid("_test@[2001:0db8:85a3:0000:0000:8a2e:0370:7334]");
        assertValid("_test@[2001:db8:85a3::8a2e:370:7334]");
        assertValid("_test@[2001:db8::1]");
        assertValid("_test@[::1]");
        assertValid("_test@[::]");
    }

    @Test
    public void testInvalidEmailsRFC() {
        assertRejected("Abc.example.com");
        assertRejected("a@b@c@example.com");
        assertRejected("A@b@c@example.com");
        assertRejected("a\"b(c)d,e:f;g<h>i[j\\k]l@example.com");
        assertRejected("just\"not\"right@example.com");
        assertRejected("this is\"not\\allowed@example.com");
        assertRejected("this\\ still\\\"not\\\\allowed@example.com");
        assertRejected("i.like.underscores@but_they_are_not_allowed_in_this_part");
    }
}
