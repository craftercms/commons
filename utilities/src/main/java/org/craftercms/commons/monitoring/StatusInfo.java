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
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Gets current basic JVM status
 * @since 3.0
 * @author Carlos Ortiz
 * @author Jose Ross
 */
public final class StatusInfo {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"));

    private long uptime;

    private String startup;

    /**
     * Create the StatusInfo with current information.
     */
    private StatusInfo() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        startup = FORMATTER.format(Instant.ofEpochMilli(runtime.getStartTime()));
        uptime = TimeUnit.MILLISECONDS.toSeconds(runtime.getUptime());
    }

    /**
     * Create the StatusInfo with current information.
     */
    public static StatusInfo getCurrentStatus(){
        return new StatusInfo();
    }

    public long getUptime() {
        return uptime;
    }

    public String getStartup() {
        return startup;
    }

    @Override
    public String toString() {
        return "StatusInfo{" +
                ", uptime='" + uptime + '\'' +
                ", startup='" + startup + '\'' +
                '}';
    }

}
