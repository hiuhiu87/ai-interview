package com.aiinterview.storage.connector.minio.util.impl;


import com.aiinterview.storage.connector.minio.util.MinioRequestBuilder;
import com.aiinterview.storage.util.FileHelper;
import io.minio.*;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Objects;

@Slf4j
public class DefaultMinioRequestBuilder implements MinioRequestBuilder {

    public PutObjectArgs buildPutObjectArgs(String bucketName, String objectKey, InputStream inputStream, Long streamLength, Long partLength) {
        PutObjectArgs putObjectArgs = null;
        long streamPartLength = -1;
        if (Objects.nonNull(partLength)) {
            streamPartLength = partLength;
        }
        putObjectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectKey).stream(inputStream, streamLength, streamPartLength).build();
        return putObjectArgs;
    }

    public PutObjectArgs buildPutObjectArgs(String bucketName, String objectKey, File file) {
        PutObjectArgs putObjectArgs = null;
        long streamSize = file.length();
        FileInputStream inputStreamData = FileHelper.toFiLeInputStream(file);
        putObjectArgs = buildPutObjectArgs(bucketName, objectKey, inputStreamData, streamSize, -1L);
        return putObjectArgs;
    }

    @Override
    public PutObjectArgs buildPutObjectArgs(String bucketName, String objectKey, InputStream inputStream) {
        PutObjectArgs putObjectArgs = null;
        long streamSize;
        try {
            streamSize = inputStream.available();
            putObjectArgs = buildPutObjectArgs(bucketName, objectKey, inputStream, streamSize, -1L);
        } catch (IOException e) {
            log.error("Invalid File Stream", e);
        }
        return putObjectArgs;
    }

    public GetObjectArgs buildGetObjectArgs(String bucketName, String objectKey) {
        GetObjectArgs getObjectArgs = null;
        getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(objectKey).build();
        return getObjectArgs;
    }

    @Override
    public RemoveObjectsArgs buildRemoveObjectsArgs(String bucketName, Collection<DeleteObject> objectKeys) {
        RemoveObjectsArgs removeObjectsArgs = null;
        removeObjectsArgs = RemoveObjectsArgs.builder().bucket(bucketName).objects(objectKeys).build();
        return removeObjectsArgs;
    }

    @Override
    public RemoveObjectArgs buildRemoveObjectArgs(String bucketName, String objectKey) {
        RemoveObjectArgs removeObjectArgs = null;
        removeObjectArgs = RemoveObjectArgs.builder().bucket(bucketName).object(objectKey).build();
        return removeObjectArgs;
    }

    @Override
    public CopyObjectArgs buildCopyObjectRequest(String bucketName, String sourceKey, String destinationKey) {
        CopyObjectArgs copyObjectArgs = null;
        copyObjectArgs = CopyObjectArgs.builder()
                .source(CopySource.builder()
                        .bucket(bucketName)
                        .object(sourceKey)
                        .build())
                .bucket(bucketName)
                .object(destinationKey)
                .build();
        return copyObjectArgs;
    }
}
