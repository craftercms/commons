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
package org.craftercms.commons.git.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.git.auth.GitAuthenticationConfigurator;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.jgit.api.ResetCommand.ResetType.HARD;
import static org.eclipse.jgit.lib.Constants.HEAD;


/**
 * Common operations related to git
 *
 * @author joseross
 * @author avasquez
 * @since 4.0
 */
public abstract class GitUtils {

    private static final Logger logger = LoggerFactory.getLogger(GitUtils.class);

    public static final String GIT_FOLDER_NAME = ".git";
    public static final String GIT_INDEX_NAME = "index";
    public static final String GIT_LOCK_NAME = GIT_INDEX_NAME + ".lock";

    public static final String CORE_CONFIG_SECTION = "core";
    public static final String BIG_FILE_THRESHOLD_CONFIG_PARAM = "bigFileThreshold";
    public static final String COMPRESSION_CONFIG_PARAM = "compression";
    public static final String FILE_MODE_CONFIG_PARAM = "fileMode";

    public static final String BIG_FILE_THRESHOLD_DEFAULT = "20m";
    public static final int COMPRESSION_DEFAULT = 0;
    public static final boolean FILE_MODE_DEFAULT = false;

    public static List<DiffEntry> doDiff(Git git, ObjectReader reader, ObjectId fromCommitId,
                                     ObjectId toCommitId) throws IOException, GitAPIException {
        AbstractTreeIterator fromTreeIter = getTreeIteratorForCommit(git, reader, fromCommitId);
        AbstractTreeIterator toTreeIter = getTreeIteratorForCommit(git, reader, toCommitId);

        return git.diff().setOldTree(fromTreeIter).setNewTree(toTreeIter).call();
    }

    public static AbstractTreeIterator getTreeIteratorForCommit(Git git, ObjectReader reader,
                                                            ObjectId commitId) throws IOException {
        if (commitId != null) {
            RevTree tree = getTreeForCommit(git.getRepository(), commitId);
            CanonicalTreeParser treeParser = new CanonicalTreeParser();

            treeParser.reset(reader, tree.getId());

            return treeParser;
        } else {
            return new EmptyTreeIterator();
        }
    }

    public static RevTree getTreeForCommit(Repository repo, ObjectId commitId) throws IOException  {
        try (RevWalk revWalk = new RevWalk(repo)) {
            RevCommit commit = revWalk.parseCommit(commitId);

            return commit.getTree();
        }
    }

    /**
     * Opens the Git repository at the specified location.
     *
     * @param localRepositoryFolder the folder where the Git repository is
     * @return the Git instance used to handle the repository
     * @throws IOException if an error occurs
     */
    public static Git openRepository(File localRepositoryFolder) throws IOException {
        return Git.open(localRepositoryFolder);
    }

    /**
     * Clones a remote repository into a specific local folder.
     *
     * @param remoteName       the name of the remote
     * @param remoteUrl        the URL of the remote. This should be a legal Git URL.
     * @param branch           the branch which should be cloned
     * @param authConfigurator the {@link GitAuthenticationConfigurator} class used to configure the authentication
     *                         with the remote repository
     * @param localFolder      the local folder into which the remote repository should be cloned
     * @param bigFileThreshold the value of the Git {@code core.bigFileThreshold} config property
     * @param compression      the value of the Git {@code core.compression} config property
     * @param fileMode         the value of the Git {@code core.fileMode} config property
     * @return the Git instance used to handle the cloned repository
     * @throws GitAPIException if a Git related error occurs
     * @throws IOException     if an IO error occurs
     */
    public static Git cloneRemoteRepository(String remoteName, String remoteUrl, String branch,
                                            GitAuthenticationConfigurator authConfigurator, File localFolder,
                                            String bigFileThreshold, Integer compression,
                                            Boolean fileMode) throws GitAPIException, IOException {
        CloneCommand command = Git.cloneRepository();
        command.setRemote(remoteName);
        command.setURI(remoteUrl);
        command.setDirectory(localFolder);

        if (StringUtils.isNotEmpty(branch)) {
            command.setCloneAllBranches(false);
            command.setBranchesToClone(Collections.singletonList(Constants.R_HEADS + branch));
            command.setBranch(branch);
        }

        if (authConfigurator != null) {
            authConfigurator.configureAuthentication(command);
        }

        Git git = command.call();
        StoredConfig config = git.getRepository().getConfig();

        String bigFileTresholdOrDefault = StringUtils.isEmpty(bigFileThreshold) ? BIG_FILE_THRESHOLD_DEFAULT : bigFileThreshold;
        Integer compressionOrDefault = compression == null ? COMPRESSION_DEFAULT : compression;
        Boolean fileModeOrDefault = fileMode == null ? FILE_MODE_DEFAULT : fileMode;

        config.setString(CORE_CONFIG_SECTION, null, BIG_FILE_THRESHOLD_CONFIG_PARAM, bigFileTresholdOrDefault);
        config.setInt(CORE_CONFIG_SECTION, null, COMPRESSION_CONFIG_PARAM, compressionOrDefault);
        config.setBoolean(CORE_CONFIG_SECTION, null, FILE_MODE_CONFIG_PARAM, fileModeOrDefault);
        config.save();

        return git;
    }

    /**
     * Execute a Git pull.
     *
     * @param git              the Git instance used to handle the repository
     * @param remoteName       the name of the remote where to pull from
     * @param remoteUrl        the URL of the remote (remote will be set to the URL)
     * @param branch           the branch to pull
     * @param mergeStrategy    the merge strategy to use
     * @param authConfigurator the {@link GitAuthenticationConfigurator} class used to configure the authentication
     *                         with the remote repository
     * @return the result of the pull
     * @throws GitAPIException if a Git related error occurs
     * @throws URISyntaxException if the remote URL is invalid
     */
    public static PullResult pull(Git git, String remoteName, String remoteUrl, String branch,
                                  MergeStrategy mergeStrategy, GitAuthenticationConfigurator authConfigurator)
            throws GitAPIException, URISyntaxException {
        addRemote(git, remoteName, remoteUrl);

        PullCommand command = git.pull();
        command.setRemote(remoteName);
        command.setRemoteBranchName(branch);

        if (mergeStrategy != null) {
            command.setStrategy(mergeStrategy);
        }

        if (authConfigurator != null) {
            authConfigurator.configureAuthentication(command);
        }

        return command.call();
    }

    /**
     * Executes a git push.
     *
     * @param git              the Git instance used to handle the repository
     * @param remote           remote name or URL
     * @param pushAll          if the push should push all local branches
     * @param remoteBranch     the remote remoteBranch being pushed to
     * @param authConfigurator the {@link GitAuthenticationConfigurator} class used to configure the authentication
     *                         with the remote
     *                         repository
     * @param force            sets the force preference for the push
     * @return the result of the push
     * @throws GitAPIException if a Git related error occurs
     */
    public static Iterable<PushResult> push(Git git, String remote, boolean pushAll, String localBranch,
                                            String remoteBranch, GitAuthenticationConfigurator authConfigurator,
                                            boolean force) throws GitAPIException {
        PushCommand push = git.push();
        push.setRemote(remote);
        push.setForce(force);

        if (pushAll) {
            push.setPushAll();
        } else if (StringUtils.isNotEmpty(remoteBranch)) {
            push.setRefSpecs(new RefSpec(localBranch + ":" + Constants.R_HEADS + remoteBranch));
        }

        if (authConfigurator != null) {
            authConfigurator.configureAuthentication(push);
        }

        return push.call();
    }

    /**
     * Executes a git gc.
     * @param repoPath full path of the repository
     * @throws GitAPIException if there is an error running the command
     * @throws IOException if there is an error opening the repository
     */
    public static void cleanup(String repoPath) throws GitAPIException, IOException {
        try (Git git = openRepository(new File(repoPath))) {
            git.gc().call();
        }
    }

    /**
     * Executes a git reset to discard all uncommitted changes
     * @param git the git repository
     * @throws GitAPIException if there is an error performing the reset
     */
    public static void discardAllChanges(Git git) throws GitAPIException {
        git.reset().setMode(HARD).setRef(HEAD).call();
    }

    /**
     * Adds a remote if it doesn't exist. If the remote exists but the URL is different, updates the URL
     *
     * @param git the Git repo
     * @param remoteName the name oif the remote
     * @param remoteUrl the URL of the remote
     *
     * @throws GitAPIException if a Git error occurs
     * @throws URISyntaxException if the remote URL is an invalid Git URL
     */
    private static void addRemote(Git git, String remoteName, String remoteUrl) throws GitAPIException,
                                                                                       URISyntaxException {
        String currentUrl = git.getRepository().getConfig().getString("remote", remoteName, "url");
        if (StringUtils.isNotEmpty(currentUrl)) {
            if (!currentUrl.equals(remoteUrl)) {
                RemoteSetUrlCommand remoteSetUrl = git.remoteSetUrl();
                remoteSetUrl.setRemoteName(remoteName);
                remoteSetUrl.setRemoteUri(new URIish(remoteUrl));
                remoteSetUrl.call();
            }
        } else {
            RemoteAddCommand remoteAdd = git.remoteAdd();
            remoteAdd.setName(remoteName);
            remoteAdd.setUri(new URIish(remoteUrl));
            remoteAdd.call();
        }
    }

    /**
     * Check if a git repository is locked
     *
     * @param repoPath path to repository
     * @return true if locked, false otherwise
     */
    public static boolean isRepositoryLocked(String repoPath) {
        Path path = Paths.get(repoPath, GIT_FOLDER_NAME, GIT_LOCK_NAME);
        return Files.exists(path);
    }

    /**
     * Unlock a git repository by deleting the `.lock` file
     *
     * @param repoPath path to repository
     *
     * @throws IOException
     */
    public static void unlock(String repoPath) throws IOException {
        Path path = Paths.get(repoPath, GIT_FOLDER_NAME, GIT_LOCK_NAME);
        logger.warn("The repository '{}' is locked, trying to unlock it", repoPath);
        deleteFile(path);
        logger.info(".git/index.lock is deleted from repository '{}'", repoPath);
    }

    /**
     * Delete the git index for a given git repository (used to fix a broken repo)
     *
     * @param repoPath path to repository
     *
     * @throws IOException
     */
    public static void deleteGitIndex(String repoPath) throws IOException {
        deleteFile(Paths.get(repoPath, GIT_FOLDER_NAME, GIT_INDEX_NAME));
    }

    /**
     * Force delete a file on disk
     *
     * @param file path to file
     *
     * @throws IOException
     */
    protected static void deleteFile(Path file) throws IOException {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            logger.debug("Error deleting file '{}', forcing delete", file, e);
            FileUtils.forceDelete(file.toFile());
        }
    }

}
