/*
 * Copyright (C) 2007-2017 Crafter Software Corporation.
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

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Get's current packageVersion information about:
 * 1. Current VM packageVersion runs current code.
 * 2. Build Information from a given manifest file.
 * 3. Name and packageVersion from a given manifest file.
 * @since 3.0
 * @author Carlos Ortiz
 */
public final class VersionMonitor {

    private final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ssZ");
    /**
     * Manifest key for the date in ms when the package was build.
     */
    public static final String BUILD_ON = "Build-On";
    /**
     * Manifest key for name of the Lib/app in the jar (usually maven name tag value).
     */
    public static final String IMPLEMENTATION_TITLE = "Implementation-Title";
    /**
     * Manifest key for VersionMonitor of the code (usually matches Mvn packageVersion)
     */
    public static final String IMPLEMENTATION_VERSION = "Implementation-Version";
    /**
     * Manifest key for codebase packageVersion (usually git hash or svn id).
     */
    public static final String IMPLEMENTATION_BUILD = "Implementation-Build";
    /**
     * System property key for File encoding setting.
     */
    public static final String FILE_ENCODING_SYSTEM_PROP_KEY = "file.encoding";
    /**
     * Manifest Default Path
     */
    public static final String MANIFEST_PATH="/META-INF/MANIFEST.MF";

    private String name;
    private String packageVersion;
    private String build;
    private String build_date;
    private String java_version;
    private String java_vendor;
    private String java_runtime;
    private String java_vm;
    private String system_encoding;
    private String operating_system;
    private String os_architecture;
    private String application_server_container;
    private String jvm_input_arguments;
    private String datetime;
    private String jvm_version;
    private String jvm_vendor;
    private String jvm_implementation_version;

    /**
     * Create a VersionMonitor pojo instance based on a given Manifest File.
     * Empty values are assign if the keys in the manifest are missing.
     * @param manifest Manifest File that contains the VersionMonitor Information.
     */
    private VersionMonitor(Manifest manifest){
        Attributes mainAttrs = manifest.getMainAttributes();
        datetime=DATETIME_FORMATTER.format(new Date());
        initFromManifest(mainAttrs);
        initRuntime();
        initOS();
        initTomcat();
    }

    private void initFromManifest(Attributes mainAttrs) {

        if (mainAttrs.getValue(BUILD_ON) != null) {
            build_date = DATETIME_FORMATTER.format(
                    new Date(
                            Long.parseLong(mainAttrs.getValue(BUILD_ON))
                    )
            );
        } else {
            build_date = DATETIME_FORMATTER.format(new Date(0));
        }
        name = mainAttrs.getValue(IMPLEMENTATION_TITLE);
        packageVersion = mainAttrs.getValue(IMPLEMENTATION_VERSION);
        build = mainAttrs.getValue(IMPLEMENTATION_BUILD);

    }

    /**
     * Gets the current VersionMonitor based on Manifest & current JVM information.
     * @param manifest Manifest were to get the information.
     * @return A VersionMonitor pojo with information.
     */
    public static VersionMonitor getVersion (Manifest manifest){
        return new VersionMonitor(manifest);
    }

    /**
     * Gets the current VersionMonitor base on a Class that will load it's manifest file.
     * @param clazz Class that will load the manifest MF file
     * @return A {@link VersionMonitor} pojo with the information.
     * @throws IOException If Unable to read the Manifest file.
     */
    public static VersionMonitor getVersion(Class clazz) throws IOException {
        Manifest manifest = new Manifest();
        manifest.read(clazz.getResourceAsStream(MANIFEST_PATH));
        return getVersion(manifest);
    }

    private void initTomcat() {
        application_server_container=StringUtils.EMPTY;
    }

    private void initOS() {
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        operating_system=os.getName()+"-"+os.getVersion();
        os_architecture=os.getArch();
        system_encoding=System.getProperty(FILE_ENCODING_SYSTEM_PROP_KEY);
    }

    @SuppressWarnings("unchecked")
    private void initRuntime(){
       RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
       java_version=runtime.getSpecVersion();
       java_vendor=runtime.getSpecVendor();
       java_runtime=runtime.getSpecName();
       java_vm=runtime.getVmName();
       jvm_input_arguments=StringUtils.join(runtime.getInputArguments());
       jvm_implementation_version=runtime.getVmVersion();
       jvm_vendor=runtime.getVmVendor();
       jvm_version=runtime.getSpecVersion();
    }

    @Override
    public String toString() {
        return "VersionMonitor{" +
                ", name='" + name + '\'' +
                ", packageVersion='" + packageVersion + '\'' +
                ", build='" + build + '\'' +
                ", build_date='" + build_date + '\'' +
                ", java_version='" + java_version + '\'' +
                ", java_vendor='" + java_vendor + '\'' +
                ", java_runtime='" + java_runtime + '\'' +
                ", java_vm='" + java_vm + '\'' +
                ", system_encoding='" + system_encoding + '\'' +
                ", operating_system='" + operating_system + '\'' +
                ", os_architecture='" + os_architecture + '\'' +
                ", application_server_container='" + application_server_container + '\'' +
                ", jvm_input_arguments='" + jvm_input_arguments + '\'' +
                ", datetime='" + datetime + '\'' +
                ", jvm_version='" + jvm_version + '\'' +
                ", jvm_vendor='" + jvm_vendor + '\'' +
                ", jvm_implementation_version='" + jvm_implementation_version + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public String getBuild() {
        return build;
    }

    public String getBuild_date() {
        return build_date;
    }

    public String getJava_version() {
        return java_version;
    }

    public String getJava_vendor() {
        return java_vendor;
    }

    public String getJava_runtime() {
        return java_runtime;
    }

    public String getJava_vm() {
        return java_vm;
    }

    public String getSystem_encoding() {
        return system_encoding;
    }

    public String getOperating_system() {
        return operating_system;
    }

    public String getOs_architecture() {
        return os_architecture;
    }

    public String getApplication_server_container() {
        return application_server_container;
    }

    public String getJvm_input_arguments() {
        return jvm_input_arguments;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getJvm_version() {
        return jvm_version;
    }

    public String getJvm_vendor() {
        return jvm_vendor;
    }

    public String getJvm_implementation_version() {
        return jvm_implementation_version;
    }
}

