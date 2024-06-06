/*
 * Copyright (C) 2007-2024 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.aws;

import org.craftercms.commons.config.profiles.aws.S3Profile;
import org.craftercms.commons.file.stores.S3Utils;
import software.amazon.awssdk.services.s3.S3AsyncClient;

/**
 * {@link AbstractAwsClientCachingFactory} for S3 async clients.
 */
public class S3AsyncClientCachingFactory extends AbstractAwsClientCachingFactory<S3Profile, S3AsyncClient>{

    @Override
    protected S3AsyncClient createClient(S3Profile profile) {
        return S3Utils.createAsyncClient(profile);
    }
}
