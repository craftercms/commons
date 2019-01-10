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
package org.craftercms.commons.search.batch;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that is used to hold the progress of a single batch index update operation.
 *
 * @author avasquez
 */
public class UpdateStatus {

    protected List<String> successfulUpdates;
    protected List<String> successfulDeletes;
    protected List<String> failedUpdates;
    protected List<String> failedDeletes;
    protected Lock listLock;

    public UpdateStatus() {
        this.successfulUpdates = new ArrayList<>();
        this.successfulDeletes = new ArrayList<>();
        this.failedUpdates = new ArrayList<>();
        this.failedDeletes = new ArrayList<>();
        this.listLock = new ReentrantLock();
    }

    @JsonProperty("successful_updates")
    public List<String> getSuccessfulUpdates() {
        return synchronizedCopy(successfulUpdates);
    }

    @JsonProperty("successful_deletes")
    public List<String> getSuccessfulDeletes() {
        return synchronizedCopy(successfulDeletes);
    }

    @JsonProperty("failed_updates")
    public List<String> getFailedUpdates() {
        return synchronizedCopy(failedUpdates);
    }

    @JsonProperty("failed_deletes")
    public List<String> getFailedDeletes() {
        return synchronizedCopy(failedDeletes);
    }

    public void addSuccessfulUpdate(String path) {
        synchronizedAdd(successfulUpdates, path);
    }

    public void addSuccessfulDelete(String path) {
        synchronizedAdd(successfulDeletes, path);
    }

    public void addFailedUpdate(String path) {
        synchronizedAdd(failedUpdates, path);
    }

    public void addFailedDelete(String path) {
        synchronizedAdd(failedDeletes, path);
    }

    @JsonProperty("failed_updates_and_deletes")
    public int getFailedUpdatesAndDeletes() {
        listLock.lock();
        try {
            return failedUpdates.size() + failedDeletes.size();
        } finally {
            listLock.unlock();
        }
    }

    @JsonProperty("successful_updates_and_deletes")
    public int getSuccessfulUpdatesAndDeletes() {
        listLock.lock();
        try {
            return successfulUpdates.size() + successfulDeletes.size();
        } finally {
            listLock.unlock();
        }
    }

    @JsonProperty("attempted_updates_and_deletes")
    public int getAttemptedUpdatesAndDeletes() {
        listLock.lock();
        try {
            return successfulUpdates.size() + successfulDeletes.size() + failedUpdates.size() + failedDeletes.size();
        } finally {
            listLock.unlock();
        }
    }

    public boolean hasBeenUpdatedOrDeleted(String path) {
        listLock.lock();
        try {
            return successfulUpdates.contains(path) ||
                   successfulDeletes.contains(path) ||
                   failedUpdates.contains(path) ||
                   failedDeletes.contains(path);
        } finally {
            listLock.unlock();
        }
    }

    protected List<String> synchronizedCopy(List<String> list) {
        listLock.lock();
        try {
            return new ArrayList<>(list);
        } finally {
            listLock.unlock();
        }
    }

    protected void synchronizedAdd(List<String> list, String path) {
        listLock.lock();
        try {
            list.add(path);
        } finally {
            listLock.unlock();
        }
    }

}
