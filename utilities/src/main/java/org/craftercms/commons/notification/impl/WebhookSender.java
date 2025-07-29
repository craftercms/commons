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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.notification.NotificationException;
import org.craftercms.commons.notification.NotificationSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.util.Set;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.springframework.http.HttpMethod.POST;

/**
 * {@link NotificationSender} implementation that sends a webhook notification.
 * The payload is available to the template as an object with the key "payload", and serialized as JSON with the key "payloadJson".
 * A {@link WebhookSender} instance can be configured with the following {@link Configuration} properties:
 *
 * <ul>
 *     <li><strong>url:</strong> The URL to send the webhook notification to.</li>
 *     <li><strong>method:</strong> The HTTP method to use. Default is <code>post</code>.</li>
 *     <li><strong>contentType:</strong> The content type of the request body. Default is <code>application/json</code></li>
 * </ul>
 * See also {@link NotificationSender} for the common properties available to all notification senders.
 */
public class WebhookSender extends NotificationSender<NotificationSender<?>.NotificationMessage> {
	private static final Logger logger = LoggerFactory.getLogger(WebhookSender.class);

	private static final Set<String> VALID_METHODS = Set.of("GET", "POST", "PUT", "PATCH", "DELETE");

	private static final String URL_CONFIG_KEY = "url";
	private static final String METHOD_CONFIG_KEY = "method";
	private static final String CONTENT_TYPE_CONFIG_KEY = "contentType";
	private static final String PAYLOAD_JSON_MODEL_KEY = "payloadJson";

	private final CloseableHttpClient httpClient;

	private RequestConfig requestConfig;

	private String method = POST.name();
	private String url;
	private String contentType = APPLICATION_JSON.getMimeType();

	@SuppressWarnings("unused")
	@ConstructorProperties({"freeMarkerConfig", "objectMapper",
			"dateTimePattern", "durationPattern"})
	public WebhookSender(freemarker.template.Configuration freeMarkerConfig, ObjectMapper objectMapper,
						 String dateTimePattern, String durationPattern) {
		super(freeMarkerConfig, objectMapper, dateTimePattern, durationPattern);
		httpClient = HttpClients.createDefault();
		requestConfig = RequestConfig.custom().build();
	}

	@Override
	public void init(Configuration config) throws ConfigurationException {
		super.init(config);
		method = config.getString(METHOD_CONFIG_KEY, method);
		validateMethod();
		contentType = config.getString(CONTENT_TYPE_CONFIG_KEY, contentType);
		url = config.getString(URL_CONFIG_KEY);
		validateUrl();
	}

	protected void validateUrl() throws ConfigurationException {
		if (StringUtils.isEmpty(url)) {
			logger.error("URL is required for WebhookSender");
			throw new ConfigurationException("URL is required for WebhookSender");
		}
	}


	protected void validateMethod() throws ConfigurationException {
		if (StringUtils.isEmpty(method)) {
			logger.error("HTTP method is required for WebhookSender");
			throw new ConfigurationException("HTTP method is required for WebhookSender");
		}

		method = method.toUpperCase();
		if (!VALID_METHODS.contains(method)) {
			logger.error("Invalid HTTP method '{}' specified for WebhookSender. Valid methods are: {}", method, VALID_METHODS);
			throw new ConfigurationException("Invalid HTTP method specified for WebhookSender: " + method);
		}
	}

	@Override
	public void close() throws IOException {
		if (httpClient != null) {
			httpClient.close();
		}
	}

	@Override
	protected NotificationMessage doCreateMessage(String templateName, Object payload) {
		NotificationMessage message = new NotificationMessage(templateName);
		try {
			String payloadJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
			message.getModel().put(PAYLOAD_JSON_MODEL_KEY, payloadJson);
		} catch (JsonProcessingException e) {
			logger.error("Failed to write event payload to JSON", e);
		}

		return message;
	}

	@Override
	protected void doNotify(NotificationSender<?>.NotificationMessage message) throws NotificationException {
		logger.info("Sending webhook notification to {} with method {}", url, method);
		try {
			HttpUriRequest request = createRequest(message);
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (HttpStatusCode.valueOf(statusCode).is2xxSuccessful()) {
					logger.info("Webhook notification sent successfully to {}", url);
				} else {
					logger.error("Webhook failed with status '{}'. Response: '{}'", response.getStatusLine(), EntityUtils.toString(response.getEntity()));
					throw new NotificationException("Webhook notification failed with status: " + response.getStatusLine());
				}
			}
		} catch (Exception e) {
			logger.error("Failed to send webhook notification", e);
			throw new NotificationException("Failed to send webhook notification", e);
		}
	}

	/**
	 * Creates the HTTP request to send the notification.
	 *
	 * @param message The notification message.
	 */
	private HttpUriRequest createRequest(NotificationSender<?>.NotificationMessage message) throws Exception {
		ContentType ct = ContentType.getByMimeType(contentType);
		if (ct == null) {
			logger.warn("Invalid content type '{}', using application/json", contentType);
			ct = ContentType.APPLICATION_JSON;
		}
		return RequestBuilder
				.create(method)
				.setUri(url)
				.setConfig(requestConfig)
				.setEntity(new StringEntity(message.getBody(), ct))
				.build();
	}

	@SuppressWarnings("unused")
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@SuppressWarnings("unused")
	public void setMethod(String method) throws ConfigurationException {
		this.method = method;
		validateMethod();
	}

	@SuppressWarnings("unused")
	public void setUrl(String url) throws ConfigurationException {
		this.url = url;
		validateUrl();
	}

	@SuppressWarnings("unused")
	public void setTimeout(int timeout) {
		requestConfig = RequestConfig.custom()
				.setConnectTimeout(timeout)
				.setSocketTimeout(timeout)
				.build();
	}
}
