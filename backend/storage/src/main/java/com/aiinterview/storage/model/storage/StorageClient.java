package com.aiinterview.storage.model.storage;

import com.aiinterview.storage.exceptions.StorageException;
import com.aiinterview.storage.model.StorageType;
import com.aiinterview.storage.model.credential.common.StorageCredential;
import com.aiinterview.storage.model.file.FileResult;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public interface StorageClient {
    StorageType getType();

    Boolean checkConnection(StorageCredential storageConfiguration) throws StorageException;

    FileResult uploadFile(String bucketName, String key, File file) throws StorageException;

    FileResult uploadFile(String bucketName, String key, InputStream inputStream) throws StorageException;

    Boolean bucketExists(String bucketName) throws StorageException;

    Boolean keyExists(String bucketName, String key) throws StorageException;

    Boolean getVersioning(String bucketName) throws StorageException;

    FileResult copyFile(String bucketName, String sourceKey, String destinationKey) throws StorageException;

    FileResult removeFile(String bucketName, String key) throws StorageException;

    FileResult moveFile(String bucketName, String sourceKey, String destinationKey) throws StorageException;

    FileResult readFile(String bucketName, String key, String versionId) throws StorageException;

    FileResult readFile(String bucketName, String key) throws StorageException;

    FileResult readFile(String fileId) throws StorageException;

    URL generatePreSignedUrl(String bucketName, String key, String versionId, Long expiredTime)  throws StorageException;

    URL generatePreSignedUrl(String bucketName, String key, String versionId, Long expiredTime, Map<String, String> params)  throws StorageException;
}