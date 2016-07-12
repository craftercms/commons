/*
 * Copyright (C) 2007-2016 Crafter Software Corporation.
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
package org.craftercms.commons.git.impl;

import java.io.File;

import org.craftercms.commons.git.api.GitRepository;
import org.craftercms.commons.git.exception.GitException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by alfonsovasquez on 8/7/16.
 */
public class GitRepositoryImpl implements GitRepository {

    private static final Logger logger = LoggerFactory.getLogger(GitRepositoryImpl.class);

    protected Git git;

    public GitRepositoryImpl(Git git) {
        this.git = git;
    }

    @Override
    public File getDirectory() {
        return git.getRepository().getDirectory();
    }

    @Override
    public void add(String filePattern) throws GitException {
        try {
            git.add().addFilepattern(filePattern).call();

            logger.debug("File '{}' added to Git repository {}", filePattern, getRepoDir());
        } catch (GitAPIException e) {
            throw new GitException("Error adding " + filePattern + " to Git repository " + getRepoDir(), e);
        }
    }

    @Override
    public void remove(String filePattern) throws GitException {
        try {
            git.rm().addFilepattern(filePattern).call();

            logger.debug("File '{}' removed from Git repository {}", filePattern, getRepoDir());
        } catch (GitAPIException e) {
            throw new GitException("Error removing " + filePattern + " from Git repository " + getRepoDir(), e);
        }
    }

    @Override
    public void commit(String message) throws GitException {
        try {
            git.commit().setMessage(message).call();

            logger.debug("Commit successful for Git repository {}", getRepoDir());
        } catch (GitAPIException e) {
            throw new GitException("Error executing commit for Git repository " + getRepoDir(), e);
        }
    }

    @Override
    public void push() throws GitException {
        try {
            git.push().call();

            logger.debug("Push successful for Git repository {}", getRepoDir());
        } catch (GitAPIException e) {
            throw new GitException("Error executing push for Git repository " + getRepoDir(), e);
        }
    }

    @Override
    public void pull() throws GitException {
        try {
            git.pull().call();

            logger.debug("Pull successful for Git repository {}", getRepoDir());
        } catch (GitAPIException e) {
            throw new GitException("Error executing pull for Git repository " + getRepoDir(), e);
        }
    }

    @Override
    public void close() throws Exception {
        git.close();
    }

    protected File getRepoDir() {
        return git.getRepository().getDirectory();
    }

}
