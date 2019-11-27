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

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(final String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        S3Profile s3Profile = (S3Profile) o;
        return bucketName.equals(s3Profile.bucketName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bucketName);
    }

    @Override
    public String toString() {
        return "S3Profile{" +
               "profileId='" + profileId + '\'' +
               ", region='" + region + '\'' +
               ", endpoint='" + endpoint + '\'' +
               ", bucketName='" + bucketName + '\'' +
               '}';
    }
}