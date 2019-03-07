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

import com.sun.management.OperatingSystemMXBean;

/**
 * Holds basic JVM memory.
 * @author Carlos Ortiz
 * @since 3.0
 */
public final class MemoryInfo {

    private long totalJvmMemory;
    private long freeJvmMemory;
    private long maxJvmMemory;
    private long totalOsMemory;
    private long freeOsMemory;
    private long totalSwapMemory;
    private long freeSwapMemory;

    /**
     * Private Constructor of the MemoryInfo POJO
     */
    private MemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        totalJvmMemory = runtime.totalMemory();
        freeJvmMemory = runtime.freeMemory();
        maxJvmMemory = runtime.maxMemory();
        totalOsMemory = os.getTotalPhysicalMemorySize();
        freeOsMemory = os.getFreePhysicalMemorySize();
        totalSwapMemory = os.getTotalSwapSpaceSize();
        freeSwapMemory = os.getFreeSwapSpaceSize();
    }

    public static MemoryInfo getCurrentMemory() {
        return new MemoryInfo();
    }

    public long getTotalJvmMemory() {
        return totalJvmMemory;
    }

    public long getFreeJvmMemory() {
        return freeJvmMemory;
    }

    public long getMaxJvmMemory() {
        return maxJvmMemory;
    }

    public long getTotalOsMemory() {
        return totalOsMemory;
    }

    public long getFreeOsMemory() {
        return freeOsMemory;
    }

    public long getTotalSwapMemory() {
        return totalSwapMemory;
    }

    public long getFreeSwapMemory() {
        return freeSwapMemory;
    }

    @Override
    public String toString() {
        return "MemoryInfo{" +
            "totalJvmMemory=" + totalJvmMemory +
            ", freeJvmMemory=" + freeJvmMemory +
            ", totalOsMemory=" + totalOsMemory +
            ", freeOsMemory=" + freeOsMemory +
            ", totalSwapMemory=" + totalSwapMemory +
            ", freeSwapMemory=" + freeSwapMemory +
            '}';
    }
}
