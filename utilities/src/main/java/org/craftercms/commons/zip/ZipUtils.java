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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility for zipping files.
 *
 * @author Jose Vega
 * @author Alfonso Vásquez
 */
public class ZipUtils {

    /**
     * Zips a collection of files to a destination zip output stream.
     *
     * @param files        A collection of files and directories
     * @param outputStream The output stream of the destination zip file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void zipFiles(List<File> files, OutputStream outputStream) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(outputStream);

        for (File file : files) {
            if (file.isDirectory()) {   //if it's a folder
                addFolderToZip("", file, zos);
            } else {
                addFileToZip("", file, zos);
            }
        }

        zos.finish();
    }

    /**
     * Zips a collection of files to a destination zip file.
     *
     * @param files   A collection of files and directories
     * @param zipFile The path of the destination zip file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void zipFiles(List<File> files, File zipFile) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(zipFile));
        try {
            zipFiles(files, os);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    /**
     * Unzips a zip from an input stream into an output folder.
     *
     * @param inputStream  the zip input stream
     * @param outputFolder the output folder where the files
     * @throws IOException
     */
    public static void unZipFiles(InputStream inputStream, File outputFolder) throws IOException {
        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {
            File file = new File(outputFolder, ze.getName());
            OutputStream os = new BufferedOutputStream(FileUtils.openOutputStream(file));

            try {
                IOUtils.copy(zis, os);
            } finally {
                IOUtils.closeQuietly(os);
            }

            zis.closeEntry();
            ze = zis.getNextEntry();
        }
    }

    /**
     * Unzips a zip file into an output folder.
     *
     * @param zipFile      the zip file
     * @param outputFolder the output folder where the files
     * @throws IOException
     */
    public static void unZipFiles(File zipFile, File outputFolder) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(zipFile));
        try {
            unZipFiles(is, outputFolder);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Adds a directory to the current zip
     *
     * @param path   the path of the parent folder in the zip
     * @param folder the directory to be  added
     * @param zos    the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void addFolderToZip(String path, File folder, ZipOutputStream zos) throws IOException {
        String currentPath = StringUtils.isNotEmpty(path)? path + "/" + folder.getName(): folder.getName();

        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                addFolderToZip(currentPath, file, zos);
            } else {
                addFileToZip(currentPath, file, zos);
            }
        }
    }

    /**
     * Adds a file to the current zip output stream
     *
     * @param path the path of the parent folder in the zip
     * @param file the file to be added
     * @param zos  the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void addFileToZip(String path, File file, ZipOutputStream zos) throws IOException {
        String currentPath = StringUtils.isNotEmpty(path)? path + "/" + file.getName(): file.getName();

        zos.putNextEntry(new ZipEntry(currentPath));

        InputStream is = new BufferedInputStream(new FileInputStream(file));
        try {
            IOUtils.copy(is, zos);
        } finally {
            IOUtils.closeQuietly(is);
        }

        zos.closeEntry();
    }

}
