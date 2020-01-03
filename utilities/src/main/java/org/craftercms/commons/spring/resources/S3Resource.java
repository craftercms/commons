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
package org.craftercms.commons.spring.resources;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.commons.io.FilenameUtils;
import org.craftercms.commons.config.profiles.aws.S3Profile;
import org.craftercms.commons.aws.S3ClientCachingFactory;
import org.craftercms.commons.lang.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * Implementation of {@link Resource} for S3 files.
 *
 * @author avasquez
 */
public class S3Resource implements RangeAwareResource {

    private static final Logger logger = LoggerFactory.getLogger(S3Resource.class);

    private S3ClientCachingFactory clientFactory;
    private S3Profile profile;
    private String key;

    public S3Resource(S3ClientCachingFactory clientFactory, S3Profile profile, String key) {
        this.clientFactory = clientFactory;
        this.profile = profile;
        this.key = key;
    }

    @Override
    public boolean exists() {
        try {
            return getClient().doesObjectExist(profile.getBucketName(), key);
        } catch (Exception e) {
            logger.error("Error while checking if object " + getDescription() + " exists", e);

            return false;
        }
    }

    @Override
    public boolean isReadable() {
        return true;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public URL getURL() throws IOException {
        throw new IOException(getDescription() + " can't be resolved to a URL");
    }

    @Override
    public URI getURI() throws IOException {
        throw new IOException(getDescription() + " can't be resolved to a URI");
    }

    @Override
    public File getFile() throws IOException {
        throw new IOException(getDescription() + " can't be resolved to a File");
    }

    @Override
    public long contentLength() throws IOException {
        return getMetadata().getContentLength();
    }

    @Override
    public long lastModified() throws IOException {
        return getMetadata().getLastModified().getTime();
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        return new S3Resource(clientFactory, profile, UrlUtils.concat(key, relativePath));
    }

    @Override
    public String getFilename() {
        return FilenameUtils.getName(key);
    }

    @Override
    public String getDescription() {
        return toString();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return getClient().getObject(profile.getBucketName(), key).getObjectContent();
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 404) {
                throw new FileNotFoundException(getDescription() + " not found");
            } else {
                throw new IOException("Error while getting object content for " + getDescription(), e);
            }
        } catch (SdkClientException e) {
            throw new IOException("Error while getting object content for " + getDescription(), e);
        }
    }

    @Override
    public InputStream getInputStream(long start, long end) throws IOException {
        try {
            return getClient().getObject(new GetObjectRequest(profile.getBucketName(), key).withRange(start, end))
                              .getObjectContent();
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 404) {
                throw new FileNotFoundException(getDescription() + " not found");
            } else {
                throw new IOException("Error while getting object content for " + getDescription(), e);
            }
        } catch (SdkClientException e) {
            throw new IOException("Error while getting object content for " + getDescription(), e);
        }
    }

    @Override
    public String toString() {
        return "S3Resource{" +
               "profile=" + profile +
               ", key='" + key + '\'' +
               '}';
    }

    private AmazonS3 getClient() {
        return clientFactory.getClient(profile);
    }

    private ObjectMetadata getMetadata() throws IOException {
        try {
            return getClient().getObjectMetadata(profile.getBucketName(), key);
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 404) {
                throw new FileNotFoundException(getDescription() + " not found");
            } else {
                throw new IOException("Error while getting object metadata for " + getDescription(), e);
            }
        } catch (SdkClientException e) {
            throw new IOException("Error while getting object metadata for " + getDescription(), e);
        }
    }

}
