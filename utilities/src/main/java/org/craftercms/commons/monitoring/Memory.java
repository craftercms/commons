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
public final class Memory {

    private static final String NON_HEAP_MEMORY = "Non Heap Memory";
    private static final String HEAP_MEMORY = "Heap Memory";
    private String name;
    private String init;
    private String used;
    private String committed;
    private String max;

    /**
     * Private Constructor of the Memory POJO
     * @param memName Type of Memory to get the information.
     * @param memoryUsage {@link MemoryUsage} bean where the information is taken from.
     */
    private Memory(String memName,MemoryUsage memoryUsage) {
        this.name=memName;
        this.init=FileUtils.byteCountToDisplaySize(memoryUsage.getInit());
        this.used=FileUtils.byteCountToDisplaySize(memoryUsage.getUsed());
        this.committed=FileUtils.byteCountToDisplaySize(memoryUsage.getCommitted());
        this.max=FileUtils.byteCountToDisplaySize(memoryUsage.getMax());
    }


    /**
     * Query all register MemoryPools to get information and create a {@link Memory} Pojo
     * @return List with all the memory usage stats.
     */
    public static List<Memory> getMemoryStats(){
        ArrayList<Memory> memoryPoolInformation = new ArrayList<>();
        MemoryUsage heapMem = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        memoryPoolInformation.add(new Memory(HEAP_MEMORY,heapMem));
        MemoryUsage nonHeapMen = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        memoryPoolInformation.add(new Memory(NON_HEAP_MEMORY,nonHeapMen));

        for(MemoryPoolMXBean memMXBean :ManagementFactory.getMemoryPoolMXBeans()){
            memoryPoolInformation.add(new Memory(memMXBean.getName(), memMXBean.getUsage()));
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
        return "Memory{" +
                "name='" + name + '\'' +
                ", init='" + init + '\'' +
                ", used='" + used + '\'' +
                ", committed='" + committed + '\'' +
                ", max='" + max + '\'' +
                '}';
    }
}
