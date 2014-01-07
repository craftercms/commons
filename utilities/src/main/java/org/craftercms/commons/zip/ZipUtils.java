package org.craftercms.commons.zip;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
     * @param files A collection of files and directories
     * @param outputStream The output stream of the destination zip file
     *
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
     * @param files A collection of files and directories
     * @param zipFile The path of the destination zip file
     *
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
     * @param inputStream the zip input stream
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
     * @param zipFile the zip file
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
     * @param path the path of the parent folder in the zip
     * @param folder the directory to be  added
     * @param zos the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void addFolderToZip(String path, File folder, ZipOutputStream zos) throws IOException {
        String currentPath = StringUtils.isNotEmpty(path) ? path + "/" + folder.getName() : folder.getName();

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
     * @param zos the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void addFileToZip(String path, File file, ZipOutputStream zos) throws IOException {
        String currentPath = StringUtils.isNotEmpty(path) ? path + "/" + file.getName() : file.getName();

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
