/*
 * Copyright (C) 2007-2017 Crafter Software Corporation.
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alfonsovasquez on 12/7/16.
 */
public class GitRepositoryImplTest {

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    @Test
    public void testAdd() throws Exception {
        Git git = Git.init().setDirectory(tmpDir.getRoot()).call();
        GitRepositoryImpl repository = new GitRepositoryImpl(git);

        File testFile = new File(tmpDir.getRoot(), "test");
        testFile.createNewFile();

        repository.add("test");

        Status status = git.status().call();

        assertNotNull(status);
        assertEquals(Collections.singleton("test"), status.getAdded());
    }

    @Test
    public void testRemove() throws Exception {
        Git git = Git.init().setDirectory(tmpDir.getRoot()).call();
        GitRepositoryImpl repository = new GitRepositoryImpl(git);

        File testFile = new File(tmpDir.getRoot(), "test");
        testFile.createNewFile();

        git.add().addFilepattern("test").call();
        git.commit().setMessage("Test commit").call();

        repository.remove("test");

        Status status = git.status().call();

        assertNotNull(status);
        assertEquals(Collections.singleton("test"), status.getRemoved());
    }

    @Test
    public void testCommit() throws Exception {
        Git git = Git.init().setDirectory(tmpDir.getRoot()).call();
        GitRepositoryImpl repository = new GitRepositoryImpl(git);

        File testFile = new File(tmpDir.getRoot(), "test");
        testFile.createNewFile();

        git.add().addFilepattern("test").call();

        repository.commit("Test message");

        List<RevCommit> commits = IterableUtils.toList(git.log().all().call());

        assertNotNull(commits);
        assertEquals(1, commits.size());
        assertEquals("Test message", commits.get(0).getFullMessage());
    }

    @Test
    public void testPush() throws Exception {
        File masterRepoDir = tmpDir.newFolder("master.git");
        File cloneRepoDir = tmpDir.newFolder("clone");

        Git masterGit = Git.init()
            .setDirectory(masterRepoDir)
            .setBare(true)
            .call();
        Git cloneGit = Git.cloneRepository()
            .setURI(masterRepoDir.getCanonicalPath())
            .setDirectory(cloneRepoDir)
            .call();
        GitRepositoryImpl cloneRepo = new GitRepositoryImpl(cloneGit);

        File testFile = new File(cloneRepoDir, "test");
        testFile.createNewFile();

        cloneGit.add().addFilepattern("test").call();
        cloneGit.commit().setMessage("Test message").call();

        cloneRepo.push();

        List<RevCommit> commits = IterableUtils.toList(masterGit.log().all().call());

        assertNotNull(commits);
        assertEquals(1, commits.size());
        assertEquals("Test message", commits.get(0).getFullMessage());

        List<String> committedFiles = getCommittedFiles(masterGit.getRepository(), commits.get(0));

        assertNotNull(committedFiles);
        assertEquals(1, committedFiles.size());
        assertEquals("test", committedFiles.get(0));
    }

    @Test
    public void testPull() throws Exception {
        File masterRepoDir = tmpDir.newFolder("master");
        File cloneRepoDir = tmpDir.newFolder("clone");

        Git masterGit = Git.init()
            .setDirectory(masterRepoDir)
            .call();
        Git cloneGit = Git.cloneRepository()
            .setURI(masterRepoDir.getCanonicalPath())
            .setDirectory(cloneRepoDir)
            .call();
        GitRepositoryImpl cloneRepo = new GitRepositoryImpl(cloneGit);

        File testFile = new File(masterRepoDir, "test");
        testFile.createNewFile();

        masterGit.add().addFilepattern("test").call();
        masterGit.commit().setMessage("Test message").call();

        cloneRepo.pull();

        List<RevCommit> commits = IterableUtils.toList(cloneGit.log().all().call());

        assertNotNull(commits);
        assertEquals(1, commits.size());
        assertEquals("Test message", commits.get(0).getFullMessage());

        List<String> committedFiles = getCommittedFiles(cloneGit.getRepository(), commits.get(0));

        assertNotNull(committedFiles);
        assertEquals(1, committedFiles.size());
        assertEquals("test", committedFiles.get(0));
    }

    private List<String> getCommittedFiles(Repository repository, RevCommit commit) throws IOException {
        List<String> files = new ArrayList<>();

        try (TreeWalk treeWalk = new TreeWalk(repository)) {
            treeWalk.addTree(commit.getTree());

            while (treeWalk.next()) {
                files.add(treeWalk.getPathString());
            }
        }

        return files;
    }

}
