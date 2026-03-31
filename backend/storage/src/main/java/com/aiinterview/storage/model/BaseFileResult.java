package com.aiinterview.storage.model;

import com.aiinterview.storage.model.file.FileResult;
import lombok.*;

import java.io.InputStream;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseFileResult implements FileResult {
    private String eTag;
    private String bucket;
    private String fileName;
    private String key;
    private String subPath;
    private String versionId;
    private String storageType;
    private InputStream inputStream;
    private Long size;
    private String mimeType;
    private String fileId;
    private List<String> parentId;
}
