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
 * Get's current basic JVM status
 * @since 3.0
 * @author Carlos Ortiz.
 */
public final class StatusMonitor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"));

    private long uptime;

    private String startup;

    /**
     * Creates the StatusMonitor with current information.
     */
    private StatusMonitor() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        startup = FORMATTER.format(Instant.ofEpochMilli(runtime.getStartTime()));
        uptime = TimeUnit.MILLISECONDS.toSeconds(runtime.getUptime());
    }

    /**
     * Creates the StatusMonitor with current information.
     */
    public static StatusMonitor getCurrentStatus(){
        return new StatusMonitor();
    }

    public long getUptime() {
        return uptime;
    }

    public String getStartup() {
        return startup;
    }

    @Override
    public String toString() {
        return "StatusMonitor{" +
                ", uptime='" + uptime + '\'' +
                ", startup='" + startup + '\'' +
                '}';
    }

}
