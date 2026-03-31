package com.aiinterview.storage.model.file;

import java.io.InputStream;
import java.util.List;

public interface FileResult extends VersioningResult{
    String getETag();

    void setETag(String eTag);

    String getBucket();

    void setBucket(String bucket);

    String getFileName();

    void setFileName(String fileName);

    String getKey();

    void setKey(String key);

    String getSubPath();

    void setSubPath(String subPath);

    String getStorageType();

    void setStorageType(String storageType);

    InputStream getInputStream() ;

    void setInputStream(InputStream inputStream) ;

    Long getSize();

    void setSize(Long size);

    String getMimeType();

    void setMimeType(String mimeType);

    String getFileId();

    void setFileId(String fileId);

    List<String> getParentId();

    void setParentId(List<String>  parentId);

}
