package org.craftercms.commons.zip;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utility for zipping files.
 *
 * @author Jose Vega
 * @author Alfonso Vásquez
 */
public class Zipper {

    private static final int BUFFER_SIZE = 4096;

    /**
     * Compresses a collection of files to a destination zip output stream.
     *
     * @param files A collection of files and directories
     * @param destZipOutput The output stream of the destination zip file
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void zipFiles(List<File> files, OutputStream destZipOutput) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(destZipOutput);

        for (File file : files) {
            if (file.isDirectory()) {   //if it's a folder
                addFolderToZip(file, file.getName(), zos);
            } else {
                addFileToZip(file, zos);
            }
        }

        zos.flush();
        zos.close();
    }

    /**
     * Compresses a collection of files to a destination zip file.
     *
     * @param files A collection of files and directories
     * @param destZipFile The path of the destination zip file
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void zipFiles(List<File> files, File destZipFile) throws IOException {
        zipFiles(files, new FileOutputStream(destZipFile));
    }

    /**
     * Adds a directory to the current zip
     * @param folder the directory to be  added
     * @param parentFolder the path of parent directory
     * @param zos the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void addFolderToZip(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) { //if it's still a folder go recursively until find a file
                addFolderToZip(file, parentFolder + "/" + file.getName(), zos);
                continue;
            }

            zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read;

            while ((read = bis.read(bytesIn)) != -1) {
                zos.write(bytesIn, 0, read);
            }

            bis.close();
            zos.closeEntry();
        }
    }

    /**
     * Adds a file to the current zip output stream
     * @param file the file to be added
     * @param zos the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void addFileToZip(File file, ZipOutputStream zos) throws IOException {
        zos.putNextEntry(new ZipEntry(file.getName()));

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;

        while ((read = bis.read(bytesIn)) != -1) {
            zos.write(bytesIn, 0, read);
        }

        bis.close();
        zos.closeEntry();
    }

}
