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
import java.io.IOException;

import org.craftercms.commons.git.api.GitRepository;
import org.craftercms.commons.git.api.GitRepositoryFactory;
import org.craftercms.commons.git.exception.GitException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by alfonsovasquez on 8/7/16.
 */
public class GitRepositoryFactoryImpl implements GitRepositoryFactory {

    private static final Logger logger = LoggerFactory.getLogger(GitRepositoryFactoryImpl.class);

    @Override
    public GitRepository open(File dir) throws GitException {
        try {
            Git git = Git.open(dir);

            logger.debug("Git repository opened at {}", git.getRepository().getDirectory());

            return new GitRepositoryImpl(git);
        } catch (IOException e) {
            throw new GitException("Error opening Git repository at " + dir, e);
        }
    }

    @Override
    public GitRepository init(File dir, boolean bare) throws GitException {
        try {
            Git git = Git.init()
                .setDirectory(dir)
                .setBare(bare)
                .call();

            logger.info("New Git repository created at {}", git.getRepository().getDirectory());

            return new GitRepositoryImpl(git);
        } catch (GitAPIException e) {
            throw new GitException("Error creating new Git repository in " + dir, e);
        }
    }

    @Override
    public GitRepository clone(String remoteUrl, File localDir) throws GitException {
        try {
            Git git = Git.cloneRepository()
                .setURI(remoteUrl)
                .setDirectory(localDir)
                .call();

            logger.info("Remote repository {} cloned into local dir {}", remoteUrl, localDir);

            return new GitRepositoryImpl(git);
        } catch (GitAPIException e) {
            throw new GitException("Error cloning remote repository " + remoteUrl + " into local dir " + localDir, e);
        }
    }

}
