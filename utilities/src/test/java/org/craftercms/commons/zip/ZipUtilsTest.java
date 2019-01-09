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
package org.craftercms.commons.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.assertTrue;

/**
 * @author Alfonso Vásquez
 */
public class ZipUtilsTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void testZipAndUnZip() throws Exception {
        File folder1 = new ClassPathResource("zip/folder1").getFile();
        File file1 = new ClassPathResource("zip/file1.txt").getFile();
        File file2 = new ClassPathResource("zip/folder1/file2.txt").getFile();
        File file3 = new ClassPathResource("zip/folder1/folder2/file3.txt").getFile();

        List<File> files = Arrays.asList(folder1, file1);
        File zipFile = tmpFolder.newFile("zippedFiles.zip");

        ZipUtils.zipFiles(files, zipFile);

        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);

        File unZipOutputFolder = tmpFolder.newFolder("unzippedFiles");

        ZipUtils.unZipFiles(zipFile, unZipOutputFolder);

        File unZippedFile1 = new File(unZipOutputFolder, "file1.txt");
        File unZippedFile2 = new File(unZipOutputFolder, "folder1/file2.txt");
        File unZippedFile3 = new File(unZipOutputFolder, "folder1/folder2/file3.txt");

        assertTrue(unZippedFile1.exists());
        assertFileContents(file1, unZippedFile1);

        assertTrue(unZippedFile2.exists());
        assertFileContents(file2, unZippedFile2);

        assertTrue(unZippedFile3.exists());
        assertFileContents(file3, unZippedFile3);
    }

    private void assertFileContents(File expected, File actual) throws IOException {
        InputStream expectedInputStream = new FileInputStream(expected);
        InputStream actualInputStream = new FileInputStream(actual);

        assertTrue(IOUtils.contentEquals(expectedInputStream, actualInputStream));
    }

}
