package org.craftercms.commons.zip;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utility class for zipping
 *
 * @author Alfonso Vásquez
 */
public class Zip {

    public static final Logger logger = LoggerFactory.getLogger(Zip.class);

    private ZipOutputStream zipOutputStream;

    /**
     * Zips a file or folder. If it's a folder, it will recursively add all it's children to the zip too.
     *
     * @param file
     *          the file to zip
     * @param zipFile
     *          the output zip file
     * @throws IOException
     */
    public static void zipFile(File file, File zipFile) throws IOException {
        Zip zip = new Zip(zipFile);
        try {
            zip.addFile(file);
        } finally {
            zip.close();
        }
    }

    /**
     * Creates a zip with the specified zip file.
     *
     * @param zipFile
     *          the zip file
     */
    public Zip(File zipFile) throws FileNotFoundException {
        this(new FileOutputStream(zipFile));
    }

    /**
     * Creates a zip output stream using the specified output stream.
     *
     * @param outputStream
     *          the output stream to wrap in a zip output stream.
     */
    public Zip(OutputStream outputStream) {
        zipOutputStream = new ZipOutputStream(new BufferedOutputStream(outputStream));
    }

    /**
     * Adds a file or folder to the zip. If it's a folder, it will recursively add all it's children.
     *
     * @param file
     *          the file to add
     * @throws IOException
     */
    public void addFile(File file) throws IOException {
        addFile("", file);
    }

    /**
     * Closes the zip output stream.
     */
    public void close() {
        IOUtils.closeQuietly(zipOutputStream);
    }

    /**
     * Adds a file or folder to the zip. If it's a folder, it will recursively add all it's children.
     *
     * @param path
     *          the path of the file inside the zip
     * @param file
     *          the file to add
     * @throws IOException
     */
    private void addFile(String path, File file) throws IOException {
        if (file.isDirectory()) {
            addFolder(path, file);
        } else {
            ZipEntry entry = new ZipEntry(path + "/" + file.getName());

            if (logger.isDebugEnabled()) {
                logger.debug("Adding zip entry '" + entry.getName() + "'");
            }

            try {
                zipOutputStream.putNextEntry(entry);
            } catch (IOException e) {
                throw new IOException("Unable to add zip entry '" + entry.getName() + "'", e);
            }

            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);

                IOUtils.copy(inputStream, zipOutputStream);
            } catch (IOException e) {
                throw new IOException("Error while writing content of file " + file.getAbsolutePath() + " to zip", e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }

    /**
     * Adds a folder to the zip, by adding all it's children.
     *
     * @param path
     *          the path of the folder inside the zip
     * @param folder
     *          the folder to add
     * @throws IOException
     */
    private void addFolder(String path, File folder) throws IOException {
        for (File file : folder.listFiles()) {
            if (path.equals("")) {
                addFile(folder.getName(), file);
            } else {
                addFile(path + "/" + folder.getName(), file);
            }
        }
    }

}
