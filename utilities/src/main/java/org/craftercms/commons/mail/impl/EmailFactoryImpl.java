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

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.craftercms.commons.mail.*;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Default implementation of {@link org.craftercms.commons.mail.EmailFactory}.
 *
 * @author avasquez
 */
public class EmailFactoryImpl implements EmailFactory {

    public static final String DEFAULT_ENCODING = "UTF-8";

    protected JavaMailSender mailSender;
    protected String defaultFromAddress;
    protected Configuration freeMarkerConfig;
    protected String templateEncoding;

    public EmailFactoryImpl() {
        templateEncoding = DEFAULT_ENCODING;
    }

    @Required
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Required
    public void setDefaultFromAddress(String defaultFromAddress) {
        this.defaultFromAddress = defaultFromAddress;
    }

    @Required
    public void setFreeMarkerConfig(Configuration freeMarkerConfig) {
        this.freeMarkerConfig = freeMarkerConfig;
    }

    public void setTemplateEncoding(String templateEncoding) {
        this.templateEncoding = templateEncoding;
    }

    @Override
    public Email getEmail(String from, String[] to, String[] cc, String[] bcc, String subject, String body,
                          boolean html) throws EmailException {
        MimeMessage message = createMessage(from, to, cc, bcc, subject, body, html);
        Email email = new EmailImpl(mailSender, message);

        return email;
    }

    @Override
    public Email getEmail(String from, String[] to, String[] cc, String[] bcc, String subject, String templateName,
                          Object templateModel, boolean html) throws EmailException {
        String body = processTemplate(templateName, templateModel);
        MimeMessage message = createMessage(from, to, cc, bcc, subject, body, html);
        Email email = new EmailImpl(mailSender, message);

        return email;
    }

    protected MimeMessage createMessage(String from, String[] to, String[] cc,  String[] bcc, String subject,
                                        String body, boolean html) throws EmailException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage());

        try {
            messageHelper.setFrom(from != null? from : defaultFromAddress);
            if (to != null) {
                messageHelper.setTo(to);
            }
            if (cc != null) {
                messageHelper.setCc(cc);
            }
            if (bcc != null) {
                messageHelper.setBcc(bcc);
            }
            messageHelper.setSubject(subject);
            messageHelper.setText(body, html);
        } catch (AddressException e) {
            throw new EmailAddressException(e);
        } catch (MessagingException e) {
            throw new GeneralEmailException(e);
        }

        return messageHelper.getMimeMessage();
    }

    protected String processTemplate(String templateName, Object templateModel) throws EmailException {
        try {
            Template template = freeMarkerConfig.getTemplate(templateName, templateEncoding);
            StringWriter out = new StringWriter();

            template.process(templateModel, out);

            return out.toString();
        } catch (IOException | TemplateException e) {
            throw new GeneralEmailException(e);
        }
    }

}
