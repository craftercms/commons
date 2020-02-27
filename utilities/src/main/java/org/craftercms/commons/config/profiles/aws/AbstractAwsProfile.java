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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.craftercms.commons.config.profiles.ConfigurationProfile;

import java.util.Objects;

/**
 * Holds the basic information required by all AWS connections.
 *
 * @author joseross
 */
public abstract class AbstractAwsProfile extends ConfigurationProfile {

    /**
     * Provides the credentials to authenticate in AWS services.
     */
    protected AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

    /**
     * Region to use in AWS services.
     */
    protected String region;

    /**
     * Endpoint to connect to compatible services (eg. Openstack Swift)
     */
    protected String endpoint;

    public AWSCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public void setCredentialsProvider(final AWSCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AbstractAwsProfile that = (AbstractAwsProfile) o;
        return Objects.equals(region, that.region) &&
               Objects.equals(endpoint, that.endpoint) &&
               areCredentialsEqual(that);
    }

    @Override
    public int hashCode() {
        String accessKey = credentialsProvider.getCredentials().getAWSAccessKeyId();
        String secretKey = credentialsProvider.getCredentials().getAWSSecretKey();

        return Objects.hash(super.hashCode(), region, endpoint, accessKey, secretKey);
    }

    protected boolean areCredentialsEqual(AbstractAwsProfile that) {
        AWSCredentials thisCredentials = credentialsProvider.getCredentials();
        AWSCredentials thatCredentials = that.credentialsProvider.getCredentials();

        if (thisCredentials == thatCredentials) {
            return true;
        }

        return thisCredentials != null && thatCredentials != null &&
               thisCredentials.getClass() == thatCredentials.getClass() &&
               thisCredentials.getAWSAccessKeyId().equals(thatCredentials.getAWSAccessKeyId()) &&
               thisCredentials.getAWSSecretKey().equals(thatCredentials.getAWSSecretKey());
    }

}