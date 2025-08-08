/*
 * Copyright (C) 2007-2025 Crafter Software Corporation. All Rights Reserved.
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

package org.craftercms.commons.notification.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.configuration2.Configuration;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.mail.Email;
import org.craftercms.commons.mail.EmailException;
import org.craftercms.commons.mail.EmailFactory;
import org.craftercms.commons.notification.NotificationException;
import org.craftercms.commons.notification.NotificationSender;
import org.slf4j.Logger;

import java.beans.ConstructorProperties;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.craftercms.commons.config.ConfigUtils.*;
import static org.slf4j.LoggerFactory.getLogger;


/**
 * {@link NotificationSender} implementation that sends an email notification.
 * The output file is attached if it's available. An {@link EmailSender} instance can be configured (see {@link #init}) with
 * the following {@link Configuration} properties:
 *
 * <ul>
 *     <li><strong>from:</strong> The value of the From field in the emails.</li>
 *     <li><strong>to:</strong> The value of the To field in the emails.</li>
 *     <li><strong>subject:</strong> The value of the Subject field in the emails.</li>
 *     <li><strong>html:</strong> Whether the emails are HTML.</li>
 * </ul>
 * See also {@link NotificationSender} for the common properties available to all notification senders.
 */
public class EmailSender extends NotificationSender<EmailSender.EmailMessage> {
	private static final Logger logger = getLogger(EmailSender.class);

	public static final String FROM_CONFIG_KEY = "from";
	public static final String TO_CONFIG_KEY = "to";
	public static final String SUBJECT_CONFIG_KEY = "subject";
	public static final String HTML_CONFIG_KEY = "html";

	public static final String OUTPUT_ATTACHED_MODEL_KEY = "outputAttached";

	protected final EmailFactory emailFactory;

	protected String from;
	protected String[] to;
	protected String subject;
	protected boolean html;

	@SuppressWarnings("unused")
	@ConstructorProperties({"freeMarkerConfig", "objectMapper", "dateTimePattern",
			"durationPattern", "emailFactory", "from", "subject", "html"})
	public EmailSender(freemarker.template.Configuration freeMarkerConfig, ObjectMapper objectMapper,
					   String dateTimePattern, String durationPattern,
					   EmailFactory emailFactory, String from,
					   String subject, boolean html) {
		super(freeMarkerConfig, objectMapper, dateTimePattern, durationPattern);
		this.emailFactory = emailFactory;
		this.from = from;
		this.subject = subject;
		this.html = html;
	}

	@Override
	public void init(Configuration config) throws ConfigurationException {
		super.init(config);
		from = getStringProperty(config, FROM_CONFIG_KEY, from);
		to = getRequiredStringArrayProperty(config, TO_CONFIG_KEY);
		subject = getStringProperty(config, SUBJECT_CONFIG_KEY, subject);
		html = getBooleanProperty(config, HTML_CONFIG_KEY, html);
	}

	@Override
	protected void doNotify(EmailMessage message) throws NotificationException {
		File attachment = message.getAttachment();
		try {
			Email email;

			if (attachment != null) {
				email = emailFactory.getEmail(from, to, null, null, subject, message.getBody(), html,
						attachment);
			} else {
				email = emailFactory.getEmail(from, to, null, null, subject, message.getBody(), html);
			}

			email.send();
			logger.info("Email notification successfully sent to {} recipients", to.length);
			if (logger.isDebugEnabled()) {
				logger.debug("Recipients: {}", Arrays.toString(to));
			}
		} catch (EmailException e) {
			throw new NotificationException("Error while sending email notification", e);
		} finally {
			deleteQuietly(attachment);
		}
	}

	@Override
	protected EmailMessage doCreateMessage(String templateName, Object payload) {
		File attachment = getAttachment(payload);
		EmailMessage message = new EmailMessage(templateName, attachment);
		message.getModel().put(OUTPUT_ATTACHED_MODEL_KEY, attachment != null);
		return message;
	}

	/**
	 * Returns a temporary file with the notification payload as JSON.
	 *
	 * @param payload The payload object to be serialized
	 * @return The temporary file
	 */
	private File getAttachment(Object payload) {
		if (payload == null) {
			return null;
		}
		File attachmentTmpFile = null;
		try {
			attachmentTmpFile = File.createTempFile("payload", ".json");
			objectMapper.writeValue(attachmentTmpFile, payload);
			return attachmentTmpFile;
		} catch (IOException e) {
			deleteQuietly(attachmentTmpFile);
			logger.error("Failed to write payload of type {} to JSON", payload.getClass().getName(), e);
			return null;
		}
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	/**
	 * {@link NotificationMessage} extension that includes a file attachment.
	 */
	protected class EmailMessage extends NotificationSender<?>.NotificationMessage {
		private final File attachment;

		public EmailMessage(String templateName, File attachment) {
			super(templateName);
			this.attachment = attachment;
		}

		public File getAttachment() {
			return attachment;
		}
	}
}
