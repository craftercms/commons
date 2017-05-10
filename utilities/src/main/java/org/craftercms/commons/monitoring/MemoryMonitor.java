package org.craftercms.commons.monitoring;


import org.apache.commons.io.FileUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds basic JVM memory.
 * @author Carlos Ortiz
 * @since 3.0
 */
public final class MemoryMonitor {

    private static final String NON_HEAP_MEMORY = "Non Heap MemoryMonitor";
    private static final String HEAP_MEMORY = "Heap MemoryMonitor";
    private String name;
    private String init;
    private String used;
    private String committed;
    private String max;

    /**
     * Private Constructor of the MemoryMonitor POJO
     * @param memName Type of MemoryMonitor to get the information.
     * @param memoryUsage {@link MemoryUsage} bean where the information is taken from.
     */
    private MemoryMonitor(String memName, MemoryUsage memoryUsage) {
        this.name=memName;
        this.init=FileUtils.byteCountToDisplaySize(memoryUsage.getInit());
        this.used=FileUtils.byteCountToDisplaySize(memoryUsage.getUsed());
        this.committed=FileUtils.byteCountToDisplaySize(memoryUsage.getCommitted());
        this.max=FileUtils.byteCountToDisplaySize(memoryUsage.getMax());
    }


    /**
     * Query all register MemoryPools to get information and create a {@link MemoryMonitor} Pojo
     * @return List with all the memory usage stats.
     */
    public static List<MemoryMonitor> getMemoryStats(){
        ArrayList<MemoryMonitor> memoryPoolInformation = new ArrayList<>();
        MemoryUsage heapMem = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        memoryPoolInformation.add(new MemoryMonitor(HEAP_MEMORY,heapMem));
        MemoryUsage nonHeapMen = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        memoryPoolInformation.add(new MemoryMonitor(NON_HEAP_MEMORY,nonHeapMen));

        for(MemoryPoolMXBean memMXBean :ManagementFactory.getMemoryPoolMXBeans()){
            memoryPoolInformation.add(new MemoryMonitor(memMXBean.getName(), memMXBean.getUsage()));
        }
        return Collections.unmodifiableList(memoryPoolInformation);
    }

    public String getName() {
        return name;
    }

    public String getInit() {
        return init;
    }

    public String getUsed() {
        return used;
    }

    public String getCommitted() {
        return committed;
    }

    public String getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "MemoryMonitor{" +
                "name='" + name + '\'' +
                ", init='" + init + '\'' +
                ", used='" + used + '\'' +
                ", committed='" + committed + '\'' +
                ", max='" + max + '\'' +
                '}';
    }
}
