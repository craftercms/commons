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
package org.craftercms.commons.mail;

import java.io.File;

/**
 * Factory for creating {@link org.craftercms.commons.mail.Email}s.
 *
 * @author avasquez
 */
public interface EmailFactory {

    /**
     * Creates a new {@link org.craftercms.commons.mail.Email}.
     *
     * @param from          the sender's address
     * @param to            the recipients' addresses (optional)
     * @param cc            the CC recipients' addresses (optional)
     * @param bcc           the BCC recipients' addresses (optional)
     * @param subject       the subject of the email
     * @param body          the text body of the email
     * @param html          if the body is in HTML format
     * @param attachments   the attachments to add to the email
     *
     * @return the created email
     */
    Email getEmail(String from, String[] to, String[] cc, String[] bcc, String subject, String body,
                   boolean html, File... attachments) throws EmailException;

    /**
     * Creates a new {@link org.craftercms.commons.mail.Email}.
     *
     * @param from      the sender's address
     * @param to        the recipients' addresses (optional)
     * @param cc        the CC recipients' addresses (optional)
     * @param bcc       the BCC recipients' addresses (optional)
     * @param replyTo   the address to reply to
     * @param subject   the subject of the email
     * @param body      the text body of the email
     * @param html      if the body is in HTML format
     *
     * @return the created email
     */
    Email getEmail(String from, String[] to, String[] cc, String[] bcc, String replyTo, String subject, String body,
                   boolean html, File... attachments) throws EmailException;

    /**
     * Creates a new {@link org.craftercms.commons.mail.Email}.
     *
     * @param from          the sender's address
     * @param to            the recipients' addresses (optional)
     * @param cc            the CC recipients' addresses (optional)
     * @param bcc           the BCC recipients' addresses (optional)
     * @param subject       the subject of the email
     * @param templateName  the template name of the email
     * @param templateModel the template model of the email
     * @param html          if the body is in HTML format
     * @return the created email
     */
    Email getEmail(String from, String[] to, String[] cc, String[] bcc, String subject, String templateName,
                   Object templateModel, boolean html, File... attachments) throws EmailException;

    /**
     * Creates a new {@link org.craftercms.commons.mail.Email}.
     *
     * @param from          the sender's address
     * @param to            the recipients' addresses (optional)
     * @param cc            the CC recipients' addresses (optional)
     * @param bcc           the BCC recipients' addresses (optional)
     * @param replyTo       the address to reply to
     * @param subject       the subject of the email
     * @param templateName  the template name of the email
     * @param templateModel the template model of the email
     * @param html          if the body is in HTML format
     * @return the created email
     */
    Email getEmail(String from, String[] to, String[] cc, String[] bcc, String replyTo, String subject,
                   String templateName, Object templateModel, boolean html, File... attachments) throws EmailException;

}
