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
package org.craftercms.commons.file.blob.impl.s3;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.aws.S3AsyncClientCachingFactory;
import org.craftercms.commons.aws.S3ClientCachingFactory;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.profiles.aws.S3Profile;
import org.craftercms.commons.file.blob.exception.BlobStoreException;
import org.craftercms.commons.file.blob.impl.AbstractBlobStore;
import org.craftercms.commons.spring.resources.S3Resource;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Implementation of {@link org.craftercms.commons.file.blob.BlobStore} for AWS S3
 *
 * @author joseross
 * @since 3.1.6
 */
public class AwsS3BlobStore extends AbstractBlobStore<S3Profile> {

    /**
     * The client factory
     */
    protected S3ClientCachingFactory clientFactory;

    /**
     * The async client factory
     */
    protected S3AsyncClientCachingFactory asyncClientFactory;

    public void setClientFactory(S3ClientCachingFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    protected S3Client getClient() {
        return clientFactory.getClient(profile);
    }

    public void setAsyncClientFactory(S3AsyncClientCachingFactory asyncClientFactory) {
        this.asyncClientFactory = asyncClientFactory;
    }

    protected S3AsyncClient getAsyncClient() {
        return asyncClientFactory.getClient(profile);
    }

    protected String getKey(Mapping mapping, String path) {
        StringBuilder sb = new StringBuilder();
        if (isNotEmpty(mapping.prefix)) {
            sb.append(appendIfMissing(removeStart(mapping.prefix, "/"), "/"));
        }
        sb.append(removeStart(path, "/"));
        return  sb.toString();
    }

    @Override
    public void doInit(HierarchicalConfiguration<ImmutableNode> config) throws ConfigurationException {
        // do nothing
    }

    @Override
    protected Resource doGetContent(Mapping mapping, String path) {
        try {
            return new S3Resource(clientFactory, profile, mapping.target, getKey(mapping, path));
        } catch (Exception e) {
            throw new BlobStoreException("Error getting content at " + mapping.target + "/" + getKey(mapping, path), e);
        }
    }

}
