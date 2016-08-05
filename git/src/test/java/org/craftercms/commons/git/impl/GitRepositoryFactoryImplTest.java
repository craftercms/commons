package org.craftercms.commons.git.impl;

import java.io.File;

import org.craftercms.commons.git.api.GitRepository;
import org.eclipse.jgit.api.Git;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by alfonsovasquez on 8/7/16.
 */
public class GitRepositoryFactoryImplTest {

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    private GitRepositoryFactoryImpl repositoryFactory;

    @Before
    public void setUp() throws Exception {
        repositoryFactory = new GitRepositoryFactoryImpl();
    }

    @Test
    public void testInit() throws Exception {
        GitRepository repo = repositoryFactory.init(tmpDir.getRoot(), false);

        assertNotNull(repo);

        File[] files = tmpDir.getRoot().listFiles();

        assertNotNull(files);
        assertEquals(1, files.length);
        assertEquals(".git", files[0].getName());
        assertTrue(files[0].isDirectory());
    }

    @Test
    public void testOpen() throws Exception {
        Git git = Git.init().setDirectory(tmpDir.getRoot()).call();

        GitRepository repo = repositoryFactory.open(tmpDir.getRoot());

        assertNotNull(repo);
        assertEquals(git.getRepository().getDirectory().getCanonicalPath(), repo.getDirectory().getCanonicalPath());
    }

    @Test
    public void testClone() throws Exception {
        File masterRepoDir = tmpDir.newFolder("master");
        File cloneRepoDir = tmpDir.newFolder("clone");

        try (Git git = Git.init().setDirectory(masterRepoDir).call()) {
            File testFile = new File(masterRepoDir, "test");
            testFile.createNewFile();

            git.add().addFilepattern("test").call();
            git.commit().setMessage("Test commit").call();
        }

        GitRepository repo = repositoryFactory.clone(masterRepoDir.getAbsolutePath(), cloneRepoDir);

        assertNotNull(repo);

        File[] files = cloneRepoDir.listFiles();

        assertNotNull(files);
        assertEquals(2, files.length);

        File gitDir = findFile(".git", files);
        assertNotNull(gitDir);
        assertTrue(gitDir.isDirectory());

        File testFile = findFile("test", files);
        assertNotNull(testFile);
        assertFalse(testFile.isDirectory());
    }

    private File findFile(String name, File[] files) {
        for (File file : files) {
            if (file.getName().equals(name)) {
                return file;
            }
        }

        return null;
    }

}
