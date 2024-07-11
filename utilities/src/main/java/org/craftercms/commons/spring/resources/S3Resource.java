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
package org.craftercms.commons.spring.resources;

import org.apache.commons.io.FilenameUtils;
import org.craftercms.commons.config.profiles.aws.S3Profile;
import org.craftercms.commons.aws.S3ClientCachingFactory;
import org.craftercms.commons.lang.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Implementation of {@link Resource} for S3 files.
 *
 * @author avasquez
 */
public class S3Resource implements RangeAwareResource {

    private static final Logger logger = LoggerFactory.getLogger(S3Resource.class);

    private S3ClientCachingFactory clientFactory;
    private S3Profile profile;
    private String bucket;
    private String key;

    public S3Resource(S3ClientCachingFactory clientFactory, S3Profile profile, String key) {
        this(clientFactory, profile, null, key);
    }

    public S3Resource(S3ClientCachingFactory clientFactory, S3Profile profile, String bucket, String key) {
        this.clientFactory = clientFactory;
        this.profile = profile;
        this.bucket = bucket;
        this.key = key;
    }

    protected String getActualKey() {
        String keyUri = UrlUtils.concat(profile.getPrefix(), key);
        try {
            return URLDecoder.decode(keyUri, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return keyUri;
        }
    }

    @Override
    public boolean exists() {
        try {
            getClient().headObject(getHeadObjectRequest());
            return true;
        } catch (NoSuchKeyException e) {
            logger.error(format("Error while checking if object '%s' exists", getDescription()), e);
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
        return getMetadata().contentLength();
    }

    @Override
    public long lastModified() throws IOException {
        return getMetadata().lastModified().toEpochMilli();
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        return new S3Resource(clientFactory, profile, UrlUtils.concat(getActualKey(), relativePath));
    }

    @Override
    public String getFilename() {
        return FilenameUtils.getName(getActualKey());
    }

    @Override
    public String getDescription() {
        return toString();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(getBucket())
                    .key(getActualKey())
                    .build();
            return getClient().getObject(getObjectRequest);
        } catch (NoSuchKeyException e) {
            throw new FileNotFoundException(getDescription() + " not found");
        } catch (Exception e) {
            throw new IOException("Error while getting object content for " + getDescription(), e);
        }
    }

    @Override
    public InputStream getInputStream(long start, long end) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(getBucket())
                    .key(getActualKey())
                    .range("bytes=" + start + "-" + end)
                    .build();
            return getClient().getObject(getObjectRequest);
        } catch (NoSuchKeyException e) {
            throw new FileNotFoundException(getDescription() + " not found");
        } catch (Exception e) {
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

    private S3Client getClient() {
        return clientFactory.getClient(profile);
    }

    private String getBucket() {
        return isEmpty(bucket)? profile.getBucketName() : bucket;
    }

    /**
     * Get head object request
     * @return instance of {@link HeadObjectRequest}
     */
    private HeadObjectRequest getHeadObjectRequest() {
        return HeadObjectRequest.builder()
                .bucket(getBucket())
                .key(getActualKey())
                .build();
    }

    /**
     * Get S3 object metadata
     * @return instance of {@link HeadObjectResponse}
     * @throws IOException
     */
    private HeadObjectResponse getMetadata() throws IOException {
        try {
            return getClient().headObject(getHeadObjectRequest());
        } catch (NoSuchKeyException e) {
            throw new FileNotFoundException(getDescription() + " not found");
        } catch (Exception e) {
            throw new IOException("Error while getting object metadata for " + getDescription(), e);
        }
    }

}
