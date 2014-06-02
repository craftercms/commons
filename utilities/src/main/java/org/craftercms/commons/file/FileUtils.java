package org.craftercms.commons.file;

import java.text.DecimalFormat;

/**
 * General file manipulation utilities.
 */
public class FileUtils {

    /**
     * Prevents a Instance of FileUtils.
     */
    private FileUtils(){}

    /**
     * Given the size of a file outputs as human readable size using SI prefix.
     * <i>Base 1024</i>
     * @param size Size in bytes of a given File.
     * @return SI String representing the file size (B,KB,MB,GB,TB).
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[] {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int)(Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
