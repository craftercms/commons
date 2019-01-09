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
package org.craftercms.commons.monitoring;


import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Get's current basic JVM statusLabel with a custom statusLabel label (default's to `healthy`)
 * @since 3.0
 * @author Carlos Ortiz.
 */
public final class StatusMonitor {

    /**
     * Default StatusMonitor Label name.
     */
    private final static String DEFAULT_STATUS_LABEL ="healthy";
    private String statusLabel;
    private String uptime;
    private String datetime;
    private final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ssZ");
    /**
     * Creates the StatusMonitor with current information and a custom statusLabel label.
     * @param statusLabel Custom statusLabel label.
     */
    private StatusMonitor(String statusLabel){
        long uptimeInMS = ManagementFactory.getRuntimeMXBean().getUptime();
        this.statusLabel = statusLabel;
        uptime=String.format("%sh %sm %ss", TimeUnit.MILLISECONDS.toHours(uptimeInMS),
                TimeUnit.MILLISECONDS.toMinutes(uptimeInMS),
                TimeUnit.MILLISECONDS.toSeconds(uptimeInMS));
        datetime=DATETIME_FORMATTER.format(new Date());
    }


    /**
     * Creates the StatusMonitor with current information and a custom statusLabel label.
     * @param statusLabel Custom statusLabel label.
     */
    public static StatusMonitor getCurrentStatus(String statusLabel){
        return new StatusMonitor(statusLabel);
    }
    /**
     * Creates the StatusMonitor with current information and a custom statusLabel label.
     * @return  Custom statusLabel label.
     */
    public static StatusMonitor getCurrentStatus(){
        return new StatusMonitor(DEFAULT_STATUS_LABEL);
    }
    public String getStatus() {
        return statusLabel;
    }

    public String getUptime() {
        return uptime;
    }

    public String getDatetime() {
        return datetime;
    }

    @Override
    public String toString() {
        return "StatusMonitor{" +
                "statusLabel='" + statusLabel + '\'' +
                ", uptime='" + uptime + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
