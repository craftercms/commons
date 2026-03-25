/*
 * Copyright (C) 2007-2026 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.validation.esapi;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.owasp.esapi.SecurityConfiguration;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.EnterpriseSecurityException;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.reference.DefaultIntrusionDetector;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * Custom IntrusionDetector to log exceptions without the stack trace
 */
public class CrafterDefaultIntrusionDetector extends DefaultIntrusionDetector {
	private final Logger logger = ESAPI.getLogger("IntrusionDetector");

	@Override
	public void addException(Exception e) {
		if (ESAPI.securityConfiguration().getDisableIntrusionDetection()) return;

		if (e instanceof EnterpriseSecurityException) {
			// Remove the exception from the warning
			logger.warning(Logger.SECURITY_FAILURE, ((EnterpriseSecurityException) e).getLogMessage());
			logger.debug(Logger.SECURITY_FAILURE, "", e);
		} else {
			// Remove the exception from the warning
			logger.warning(Logger.SECURITY_FAILURE, e.getMessage());
			logger.debug(Logger.SECURITY_FAILURE, "", e);
		}


		// add the exception to the current user, which may trigger a detector
		User user = ESAPI.authenticator().getCurrentUser();
		String eventName = e.getClass().getName();

		if (e instanceof IntrusionException) {
			return;
		}

		// add the exception to the user's store, handle IntrusionException if thrown
		try {
			addSecurityEvent(user, eventName);
		} catch (IntrusionException ex) {
			SecurityConfiguration.Threshold quota = ESAPI.securityConfiguration().getQuota(eventName);
			Iterator i = quota.actions.iterator();
			while (i.hasNext()) {
				String action = (String) i.next();
				String message = "User exceeded quota of " + quota.count + " per " + quota.interval + " seconds for event " + eventName + ". Taking actions " + quota.actions;
				takeSecurityAction(action, message);
			}
		}
	}

	// **************************************************************************
	// Methods copied from the DefaultIntrusionDetector to keep addException changes to the minimum
	private void takeSecurityAction(String action, String message) {
		if (ESAPI.securityConfiguration().getDisableIntrusionDetection()) return;

		if (action.equals("log")) {
			logger.fatal(Logger.SECURITY_FAILURE, "INTRUSION - " + message);
		}
		User user = ESAPI.authenticator().getCurrentUser();
		if (user == User.ANONYMOUS)
			return;
		if (action.equals("disable")) {
			user.disable();
		}
		if (action.equals("logout")) {
			user.logout();
		}
	}


	private void addSecurityEvent(User user, String eventName) {
		if (ESAPI.securityConfiguration().getDisableIntrusionDetection()) return;

		if (user.isAnonymous()) return;

		HashMap eventMap = user.getEventMap();

		// if there is a threshold, then track this event
		SecurityConfiguration.Threshold threshold = ESAPI.securityConfiguration().getQuota(eventName);
		if (threshold != null) {
			Event event = (Event) eventMap.get(eventName);
			if (event == null) {
				event = new Event(eventName);
				eventMap.put(eventName, event);
			}
			// increment
			event.increment(threshold.count, threshold.interval);
		}
	}

	private static class Event {
		public String key;
		public Stack times = new Stack();

		//public long count = 0;
		public Event(String key) {
			this.key = key;
		}

		public void increment(int count, long interval) throws IntrusionException {
			if (ESAPI.securityConfiguration().getDisableIntrusionDetection()) return;

			Date now = new Date();
			times.add(0, now);
			while (times.size() > count) times.remove(times.size() - 1);
			if (times.size() == count) {
				Date past = (Date) times.get(count - 1);
				long plong = past.getTime();
				long nlong = now.getTime();
				if (nlong - plong < interval * 1000) {
					throw new IntrusionException("Threshold exceeded", "Exceeded threshold for " + key);
				}
			}
		}
	}

}
