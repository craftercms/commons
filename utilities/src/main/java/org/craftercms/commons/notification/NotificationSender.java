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

package org.craftercms.commons.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.configuration2.Configuration;
import org.craftercms.commons.config.ConfigurationException;
import org.slf4j.Logger;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;
import static org.craftercms.commons.config.ConfigUtils.DEFAULT_ENCODING;
import static org.craftercms.commons.config.ConfigUtils.getStringProperty;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Base class for notification senders.
 * <p>
 * A {@link NotificationSender} instance can be configured (see {@link #init}) with
 * the following {@link Configuration} properties:
 *
 * <ul>
 *     <li><strong>serverName:</strong> An identifier of the current server..</li>
 *     <li><strong>dateTimePattern:</strong> Pattern to format dates.</li>
 *     <li><strong>durationPattern:</strong> Pattern to format duration objects.</li>
 * </ul>
 * <p>
 * Additional to the model passed to the {@link #sendMessage} method, the following model keys
 * are available to the notification templates:
 * <ul>
 *     <li><strong>serverName:</strong> The server name configured in the sender.</li>
 *     <li><strong>dateTimeFormatter:</strong> A {@link DateTimeFormatter} instance to format dates.</li>
 *     <li><strong>durationFormatter:</strong> A {@link Function Function&lt;Long, String&gt;} that formats durations in milliseconds.</li>
 *     <li><strong>payload:</strong> The payload object passed to the {@link #sendMessage} method.</li>
 * </ul>
 *
 * @param <M> the type of the notification message that will be created and sent
 */
public abstract class NotificationSender<M extends NotificationSender<?>.NotificationMessage> implements AutoCloseable {

	private static final Logger logger = getLogger(NotificationSender.class);

	protected static final String DATETIME_PATTERN_CONFIG_KEY = "dateTimePattern";
	protected static final String DURATION_PATTERN_CONFIG_KEY = "durationPattern";

	protected static final String PAYLOAD_MODEL_KEY = "payload";
	protected static final String DATETIME_FORMATTER_MODEL_KEY = "dateTimeFormatter";
	protected static final String DURATION_FORMATTER_MODEL_KEY = "durationFormatter";

	protected final freemarker.template.Configuration freeMarkerConfig;
	protected String templateEncoding = DEFAULT_ENCODING;
	protected String templatePrefix = "";
	protected String templateSuffix = "";
	protected final ObjectMapper objectMapper;

	private String dateTimePattern;
	protected DateTimeFormatter dateTimeFormatter;
	private String durationPattern;
	protected Function<Long, String> durationFormatter;

	@ConstructorProperties({"freeMarkerConfig", "objectMapper", "dateTimePattern", "durationPattern"})
	public NotificationSender(freemarker.template.Configuration freeMarkerConfig,
							  ObjectMapper objectMapper, String dateTimePattern,
							  String durationPattern) {
		this.freeMarkerConfig = freeMarkerConfig;
		this.objectMapper = objectMapper;
		this.dateTimePattern = dateTimePattern;
		this.durationPattern = durationPattern;
		initProperties();
	}

	/**
	 * Initializes properties calculated from the configuration.
	 */
	protected void initProperties() {
		dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
		durationFormatter = (durationMillis) -> formatDuration(durationMillis, durationPattern);
	}

	public void init(Configuration config) throws ConfigurationException {
		dateTimePattern = getStringProperty(config, DATETIME_PATTERN_CONFIG_KEY, dateTimePattern);
		durationPattern = getStringProperty(config, DURATION_PATTERN_CONFIG_KEY, durationPattern);

		initProperties();
	}

	@Override
	public void close() throws Exception {
		// No resources to close in this base class, but subclasses may override this method
	}

	/**
	 * Sends a notification message
	 *
	 * @param templateName the name of the template to use for the notification
	 * @param payload      the payload to include in the notification
	 * @param model        additional model data to include in the notification
	 * @throws NotificationException if an error occurs while sending the notification
	 */
	@SuppressWarnings("unused")
	public void sendMessage(String templateName, Object payload, Map<String, Object> model) throws NotificationException {
		doNotify(createMessage(templateName, payload, model));
	}

	/**
	 * Creates a notification message using the specified template name, target, and payload.
	 *
	 * @param templateName the name of the template to use for the notification
	 * @param payload      the payload to include in the notification
	 * @param model        additional model data to include in the notification
	 * @return the notification message
	 */
	protected final M createMessage(String templateName, Object payload, Map<String, Object> model) {
		M message = doCreateMessage(templateName, payload);
		Map<String, Object> templateModel = message.getModel();
		templateModel.put(DATETIME_FORMATTER_MODEL_KEY, dateTimeFormatter);
		templateModel.put(DURATION_FORMATTER_MODEL_KEY, durationFormatter);
		templateModel.put(PAYLOAD_MODEL_KEY, payload);
		if (model != null) {
			templateModel.putAll(model);
		}
		return message;
	}

	/**
	 * Sends the notification message.
	 *
	 * @param message the notification message
	 * @throws NotificationException if an error occurs while sending the notification
	 */
	protected abstract void doNotify(M message) throws NotificationException;

	/**
	 * Creates the notification message to send.
	 *
	 * @param templateName the name of the template to use for the notification
	 * @param payload      the payload to include in the notification
	 * @return the notification message
	 */
	protected abstract M doCreateMessage(String templateName, Object payload);

	@SuppressWarnings("unused")
	public void setTemplatePrefix(String templatePrefix) {
		this.templatePrefix = templatePrefix;
	}

	@SuppressWarnings("unused")
	public void setTemplateSuffix(String templateSuffix) {
		this.templateSuffix = templateSuffix;
	}

	@SuppressWarnings("unused")
	public void setTemplateEncoding(String templateEncoding) {
		this.templateEncoding = templateEncoding;
	}

	/**
	 * Processes the notification template with the given model.
	 *
	 * @param templateModel the model to use for processing the template
	 * @return the template output
	 * @throws NotificationException if an error occurs while loading or processing the template
	 */
	protected String processTemplate(String templateName, Map<String, Object> templateModel) throws NotificationException {
		logger.debug("Processing notification template '{}'", templateName);
		if (isEmpty(templateName)) {
			throw new NotificationException("Template name cannot be empty");
		}
		try {
			String fullTemplateName = templatePrefix + templateName + templateSuffix;
			Template template = freeMarkerConfig.getTemplate(fullTemplateName, templateEncoding);
			StringWriter out = new StringWriter();

			template.process(templateModel, out);

			return out.toString();
		} catch (IOException | TemplateException e) {
			throw new NotificationException(format("Failed to process template '%s'", templateName), e);
		}
	}

	/**
	 * Base class for notification messages.
	 * {@link NotificationSender} implementations may extend this class to provide custom messages.
	 */
	public class NotificationMessage {
		private final Map<String, Object> model = new HashMap<>();
		private final String templateName;

		public NotificationMessage(String templateName) {
			this.templateName = templateName;
		}

		public Map<String, Object> getModel() {
			return model;
		}

		public String getBody() throws NotificationException {
			return processTemplate(templateName, getModel());
		}
	}
}
