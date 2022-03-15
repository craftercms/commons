/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
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

import java.util.Objects;

/**
 * Holds the information to connect to AWS S3.
 *
 * @author joseross
 */
public class S3Profile extends AbstractAwsProfile {

    /**
     * Name of the bucket.
     */
    protected String bucketName;

    /**
     * Optional prefix to prepend to all keys
     */
    protected String prefix;

    /**
     * Indicates if path style access should be used for all request
     */
    protected boolean pathStyleAccessEnabled;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(final String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isPathStyleAccessEnabled() {
        return pathStyleAccessEnabled;
    }

    public void setPathStyleAccessEnabled(final boolean pathStyleAccessEnabled) {
        this.pathStyleAccessEnabled = pathStyleAccessEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        S3Profile s3Profile = (S3Profile) o;
        return pathStyleAccessEnabled == s3Profile.pathStyleAccessEnabled &&
               Objects.equals(bucketName, s3Profile.bucketName) && Objects.equals(prefix, s3Profile.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bucketName, prefix, pathStyleAccessEnabled);
    }

    @Override
    public String toString() {
        return "S3Profile{" +
               "profileId='" + profileId + '\'' +
               ", region='" + region + '\'' +
               ", endpoint='" + endpoint + '\'' +
               ", bucketName='" + bucketName + '\'' +
               ", prefix='" + prefix + '\'' +
               ", pathStyleAccessEnabled=" + pathStyleAccessEnabled +
               '}';
    }

}