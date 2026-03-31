package com.aiinterview.storage.connector.minio.util;

import io.minio.*;
import io.minio.messages.DeleteObject;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

public interface MinioRequestBuilder {
    PutObjectArgs buildPutObjectArgs(String bucketName, String objectKey, InputStream inputStream, Long streamLength, Long partLength);

    PutObjectArgs buildPutObjectArgs(String bucketName, String objectKey, File file) ;
    PutObjectArgs buildPutObjectArgs(String bucketName, String objectKey, InputStream inputStream) ;

    GetObjectArgs buildGetObjectArgs(String bucketName, String objectKey) ;
    RemoveObjectsArgs buildRemoveObjectsArgs(String bucketName, Collection<DeleteObject> objectKeys) ;

    RemoveObjectArgs buildRemoveObjectArgs(String bucketName, String key) ;

    CopyObjectArgs buildCopyObjectRequest(String bucketName, String sourceKey, String destinationKey) ;
}
