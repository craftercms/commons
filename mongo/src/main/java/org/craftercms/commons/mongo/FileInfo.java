package org.craftercms.commons.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

import java.io.InputStream;
import java.util.Date;

import org.bson.types.ObjectId;
import org.craftercms.commons.file.FileUtils;

/**
 * Grid Fs File Information
 */
public class FileInfo {

    private String md5;
    private ObjectId fileId;
    private String contentType;
    private String fileSize;
    private String fileName;
    private Date savedDate;
    private long fileSizeBytes;
    @JsonIgnore
    private InputStream inputStream;

    FileInfo(final GridFSFile savedFile,final boolean withInputStream) {
        this.md5 = savedFile.getMD5();
        this.fileId = (ObjectId)savedFile.getId();
        this.contentType = savedFile.getContentType();
        this.fileSize = FileUtils.readableFileSize(savedFile.getLength());
        this.fileName = savedFile.getFilename();
        this.savedDate = savedFile.getUploadDate();
        this.fileSizeBytes = savedFile.getLength();
        if (withInputStream && savedFile instanceof GridFSDBFile) {
            this.inputStream = ((GridFSDBFile)savedFile).getInputStream();
        }
    }

    public FileInfo() {
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(final String md5) {
        this.md5 = md5;
    }

    public ObjectId getFileId() {
        return fileId;
    }

    public void setFileId(final ObjectId fileId) {
        this.fileId = fileId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(final String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public Date getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(final Date savedDate) {
        this.savedDate = savedDate;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(final long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final FileInfo fileInfo = (FileInfo)o;

        if (!fileId.equals(fileInfo.fileId)) {
            return false;
        }
        if (!fileName.equals(fileInfo.fileName)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = fileId.hashCode();
        result = 31 * result + fileName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
            "md5='" + md5 + '\'' +
            ", fileId=" + fileId +
            ", contentType='" + contentType + '\'' +
            ", fileSize='" + fileSize + '\'' +
            ", fileName='" + fileName + '\'' +
            ", savedDate=" + savedDate +
            '}';
    }
}
