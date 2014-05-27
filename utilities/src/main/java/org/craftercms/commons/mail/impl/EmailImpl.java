/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
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
package org.craftercms.commons.mail.impl;

import javax.mail.internet.MimeMessage;

import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.commons.mail.Email;
import org.craftercms.commons.mail.EmailExceptionAbstract;
import org.craftercms.commons.mail.EmailSendExceptionAbstract;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Default implementation of {@link org.craftercms.commons.mail.Email}, which uses Spring Mail to send a message.
 *
 * @author avasquez
 */
public class EmailImpl implements Email {

    public static final String LOG_KEY_EMAIL_SENT = "mail.emailSent";
    private static final I10nLogger logger = new I10nLogger(EmailImpl.class, "crafter.commons.messages.logging");
    protected JavaMailSender mailSender;
    protected MimeMessage message;

    public EmailImpl(JavaMailSender mailSender, MimeMessage message) {
        this.mailSender = mailSender;
        this.message = message;
    }

    @Override
    public void send() throws EmailExceptionAbstract {
        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new EmailSendExceptionAbstract(e);
        }

        logger.debug(LOG_KEY_EMAIL_SENT);
    }

}
