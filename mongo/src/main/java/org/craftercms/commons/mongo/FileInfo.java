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
package org.craftercms.commons.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private String storeName;
    private String fileName;
    private Date savedDate;
    private long fileSizeBytes;
    private Map<String,Object> attributes;
    @JsonIgnore
    private InputStream inputStream;


    FileInfo(final GridFSFile savedFile,final boolean withInputStream) {
        this.md5 = savedFile.getMD5();
        this.fileId = (ObjectId)savedFile.getId();
        this.contentType = savedFile.getContentType();
        this.fileSize = FileUtils.readableFileSize(savedFile.getLength());
        this.storeName = savedFile.getFilename();
        this.savedDate = savedFile.getUploadDate();
        this.fileSizeBytes = savedFile.getLength();
        if (withInputStream && savedFile instanceof GridFSDBFile) {
            this.inputStream = ((GridFSDBFile)savedFile).getInputStream();
        }
        attributes=new HashMap<>();
    }

    public FileInfo() {
        attributes=new HashMap<>();
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(final String storeName) {
        this.storeName = storeName;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
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
        if (!storeName.equals(fileInfo.storeName)) {
            return false;
        }

        return true;
    }

    public void setAttribute(final String key,final Object value){
        attributes.put(key,value);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public int hashCode() {
        int result = fileId.hashCode();
        result = 31 * result + storeName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
            "md5='" + md5 + '\'' +
            ", fileId=" + fileId +
            ", contentType='" + contentType + '\'' +
            ", fileSize='" + fileSize + '\'' +
            ", storeName='" + storeName + '\'' +
            ", savedDate=" + savedDate +
            '}';
    }
}
