package com.aiinterview.storage.connector.minio.util.impl;

import com.aiinterview.storage.connector.minio.util.MinioConverter;
import com.aiinterview.storage.model.BaseFileResult;
import com.aiinterview.storage.model.StorageType;
import com.aiinterview.storage.model.file.FileResult;
import com.aiinterview.storage.util.FileHelper;
import com.aiinterview.storage.util.StorageKeyConverter;
import io.minio.GetObjectResponse;
import io.minio.ObjectWriteResponse;


public class DefaultMinioConverter implements MinioConverter {
    @Override
    public FileResult toFileResult(ObjectWriteResponse objectWriteResponse) {
        String eTag = objectWriteResponse.etag();
        String versionId = objectWriteResponse.versionId();
        String bucket = objectWriteResponse.bucket();
        String key = objectWriteResponse.object();
        String fileName = StorageKeyConverter.extractFileNameFromStorageKey(key);
        String mimeType = FileHelper.guessFileMimeType(fileName);
        return BaseFileResult.builder()
                .fileName(fileName)
                .eTag(eTag)
                .mimeType(mimeType)
                .bucket(bucket)
                .key(key)
                .versionId(versionId)
                .storageType(StorageType.MINIO.name())
                .build();
    }

    @Override
    public FileResult toFileResult(GetObjectResponse getObjectResponse) {
        String bucket = getObjectResponse.bucket();
        String key = getObjectResponse.object();
        String fileName = StorageKeyConverter.extractFileNameFromStorageKey(key);
        String mimeType = FileHelper.guessFileMimeType(fileName);
        return BaseFileResult.builder()
                .fileName(fileName)
                .mimeType(mimeType)
                .bucket(bucket)
                .inputStream(getObjectResponse)
                .key(key)
                .storageType(StorageType.MINIO.name())
                .build();
    }
}
