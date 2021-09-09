/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.config.profiles.aws;

import com.amazonaws.auth.*;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.config.profiles.ConfigurationProfile;

import java.util.Objects;

/**
 * Holds the basic information required by all AWS connections.
 *
 * @author joseross
 */
public abstract class AbstractAwsProfile extends ConfigurationProfile {

    /**
     * Region to use in AWS services.
     */
    protected String region;

    /**
     * Endpoint to connect to compatible services (eg. Openstack Swift)
     */
    protected String endpoint;

    /**
     * The AWS access key (if using static credentials)
     */
    protected String accessKey;

    /**
     * The AWS secret key (if using static credentials)
     */
    protected String secretKey;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public AWSCredentialsProvider getCredentialsProvider() {
        if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
            return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
        } else {
            return DefaultAWSCredentialsProviderChain.getInstance();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AbstractAwsProfile that = (AbstractAwsProfile) o;
        return Objects.equals(region, that.region) &&
               Objects.equals(endpoint, that.endpoint) &&
               Objects.equals(accessKey, that.accessKey) &&
               Objects.equals(secretKey, that.secretKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), region, endpoint, accessKey, secretKey);
    }

}