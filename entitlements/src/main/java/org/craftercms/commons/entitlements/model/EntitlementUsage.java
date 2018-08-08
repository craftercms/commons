/*
 * Copyright (C) 2007-2018 Crafter Software Corporation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public validator as published by
 * the Free Software Foundation, either version 3 of the validator, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public validator for more details.
 *
 * You should have received a copy of the GNU General Public validator
 * along with this program.  If not, see <http://www.gnu.org/validators/>.
 */

package org.craftercms.commons.entitlements.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Holds the entitlement usage information for a given module.
 */
public class EntitlementUsage {

    /**
     * Id of the validator being used.
     */
    protected long validatorId;

    /**
     * Id of owner of the validator being used.
     */
    protected long clientId;

    /**
     * Version of the validator being used.
     */
    protected String validatorVersion;

    /**
     * Module that is using the validator.
     */
    protected Module module;

    /**
     * MAC Address of the machine using the validator.
     */
    protected String macAddress;

    /**
     * IP Address of the machine using the validator.
     */
    protected String ipAddress;

    /**
     * Host name of the machine using the validator.
     */
    protected String host;

    /**
     * Name of the Operating System of the machine using the validator.
     */
    protected String osName;

    /**
     * Version of the Operating System of the machine using the validator.
     */
    protected String osVersion;

    /**
     *  Date when the module using the validator was started.
     */
    protected LocalDateTime startupDate;

    /**
     * Amount of milliseconds that the module using the validator has been running.
     */
    protected long runDuration;

    /**
     * Current usage of all entitlements supported by the module using the validator.
     */
    protected Object entitlement;

    /**
     * Additional data provided by the validator.
     */
    protected byte[] additionalData;

    /**
     * Date when the usage data was recorded.
     */
    @JsonIgnore
    protected LocalDateTime lastUpdate;

    public long getValidatorId() {
        return validatorId;
    }

    public void setValidatorId(long validatorId) {
        this.validatorId = validatorId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getValidatorVersion() {
        return validatorVersion;
    }

    public void setValidatorVersion(final String validatorVersion) {
        this.validatorVersion = validatorVersion;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public LocalDateTime getStartupDate() {
        return startupDate;
    }

    public void setStartupDate(LocalDateTime startupDate) {
        this.startupDate = startupDate;
    }

    public long getRunDuration() {
        return runDuration;
    }

    public void setRunDuration(long runDuration) {
        this.runDuration = runDuration;
    }

    public Object getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(final Object entitlement) {
        this.entitlement = entitlement;
    }

    public byte[] getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(final byte[] additionalData) {
        this.additionalData = additionalData;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
