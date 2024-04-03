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

import com.amazonaws.client.builder.ExecutorFactory;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.internal.TransferStateChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Provides utility methods for AWS services
 */
public final class AwsUtils {
    /**
     * Builds the {@link TransferManager} using the shared {@link ExecutorService}
     */
    public static TransferManager buildTransferManager(AmazonS3 client, ExecutorFactory executorFactory) {
        return TransferManagerBuilder
                .standard()
                .withS3Client(client)
                .withExecutorFactory(executorFactory)
                .withShutDownThreadPools(false)
                .build();
    }

    /**
     * Copy a list of objects from a source bucket to a target bucket
     *
     * @param client          The {@link AmazonS3} client
     * @param executorFactory The {@link ExecutorFactory} to use to create a {@link TransferManager}
     * @param sourceBucket    The source bucket
     * @param sourceBaseKey   The base key in the source bucket (i.e. prefix for all paths)
     * @param targetBucket    The target bucket
     * @param targetBaseKey   The base key in the target bucket (i.e. prefix for all paths)
     * @param paths           The list of paths to copy
     * @throws InterruptedException If the operation is interrupted while copying the objects
     */
    public static void copyObjects(AmazonS3 client, ExecutorFactory executorFactory, String sourceBucket, String sourceBaseKey,
                                   String targetBucket, String targetBaseKey, List<String> paths) throws InterruptedException {

        TransferManager transferManager = buildTransferManager(client, executorFactory);
        try {
            CountDownLatch doneSignal = new CountDownLatch(paths.size());
            for (String path : paths) {
                String sourceKey = sourceBaseKey + path;
                String targetKey = targetBaseKey + path;
                CopyObjectRequest copyRequest = new CopyObjectRequest()
                        .withSourceBucketName(sourceBucket)
                        .withSourceKey(sourceKey)
                        .withDestinationBucketName(targetBucket)
                        .withDestinationKey(targetKey);
                transferManager.copy(copyRequest, new MultiOperationTransferStateChangeListener(sourceKey, targetKey, doneSignal));
            }
            doneSignal.await();
        } finally {
            transferManager.shutdownNow();
        }
    }

    /**
     * Transfer state change listener that logs the state of the transfer while signaling a {@link CountDownLatch}
     * to be able to wait for the completion of the transfer
     */
    private static class MultiOperationTransferStateChangeListener implements TransferStateChangeListener {
        protected static final Logger logger = LoggerFactory.getLogger(MultiOperationTransferStateChangeListener.class);

        private final String source;
        private final String target;

        private final CountDownLatch doneSignal;

        public MultiOperationTransferStateChangeListener(final String source, final String target, final CountDownLatch doneSignal) {
            this.source = source;
            this.target = target;
            this.doneSignal = doneSignal;
        }

        @Override
        public void transferStateChanged(Transfer transfer, Transfer.TransferState state) {
            switch (state) {
                case InProgress -> logger.debug("Started to copy: '{}' -> '{}'", source, target);
                case Completed -> {
                    logger.debug("Completed copy: '{}' -> '{}'", source, target);
                    doneSignal.countDown();
                }
                case Failed -> {
                    logger.debug("Failed copy: '{}' -> '{}'", source, target);
                    doneSignal.countDown();
                }
                case Canceled -> {
                    logger.debug("Canceled copy: '{}' -> '{}'", source, target);
                    doneSignal.countDown();
                }
            }
        }
    }

}
