package com.aiinterview.storage.connector.minio;

import com.aiinterview.storage.authenticator.CredentialProvider;
import com.aiinterview.storage.connector.minio.util.MinioConverter;
import com.aiinterview.storage.connector.minio.util.MinioRequestBuilder;
import com.aiinterview.storage.exceptions.ErrorMessage;
import com.aiinterview.storage.exceptions.StorageException;
import com.aiinterview.storage.model.BaseFileResult;
import com.aiinterview.storage.model.StorageType;
import com.aiinterview.storage.model.credential.common.StorageCredential;
import com.aiinterview.storage.model.credential.minio.MinioCredential;
import com.aiinterview.storage.model.file.FileResult;
import com.aiinterview.storage.model.storage.StorageClient;
import com.aiinterview.storage.util.StorageKeyConverter;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.VersioningConfiguration;
import kotlin.NotImplementedError;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MinioStorageClient implements StorageClient {

    private final MinioRequestBuilder requestBuilder;

    private final MinioConverter resultConverter;

    private final CredentialProvider credentialProvider;

    public MinioStorageClient(MinioRequestBuilder requestBuilder, MinioConverter resultConverter, CredentialProvider credentialProvider) {
        this.requestBuilder = requestBuilder;
        this.resultConverter = resultConverter;
        this.credentialProvider = credentialProvider;
    }

    private MinioClient buildClient() {
        StorageCredential storageCredential = this.credentialProvider.getCredential(StorageType.MINIO);
        if (storageCredential instanceof MinioCredential credential) {
            return this.buildClient(credential);
        }
        throw new StorageException(ErrorMessage.MISSING_STORAGE_CONFIGURATION);
    }

    public MinioClient buildClient(MinioCredential configuration) throws StorageException {
        MinioClient.Builder minioClientBuilder = MinioClient.builder();
        if (Objects.nonNull(configuration.getAccessKey()) && Objects.nonNull(configuration.getSecretKey())) {
            minioClientBuilder.credentials(configuration.getAccessKey(), configuration.getSecretKey());
        }
        if (Objects.nonNull(configuration.getEndpoint())) {
            minioClientBuilder.endpoint(configuration.getEndpoint());
        }
        if (Objects.nonNull(configuration.getRegion()) && !configuration.getRegion().isBlank()) {
            minioClientBuilder.region(configuration.getRegion());
        }
        return minioClientBuilder.build();
    }

    @Override
    public StorageType getType() {
        return StorageType.MINIO;
    }

    @Override
    public Boolean checkConnection(StorageCredential storageConfiguration) throws StorageException {
        if (storageConfiguration instanceof MinioCredential credential) {
            MinioClient client = this.buildClient(credential);
            boolean connectionValid = false;
            try {
                this.listBucket(client);
                connectionValid = true;
            } catch (StorageException e) {
                throw new StorageException(ErrorMessage.STORAGE_EXECUTION_FAIL, e);
            }
            return connectionValid;
        }
        throw new StorageException(ErrorMessage.MISSING_STORAGE_CONFIGURATION);
    }

    private List<Bucket> listBucket(MinioClient client) throws StorageException {
        List<Bucket> buckets = new ArrayList<>();
        try {
            buckets = client.listBuckets();
        } catch (Exception e) {
            throw new StorageException(ErrorMessage.STORAGE_EXECUTION_FAIL, e);
        }
        return buckets;
    }

    private FileResult uploadFile(PutObjectArgs putObjectRequest) {
        MinioClient client = this.buildClient();
        ObjectWriteResponse objectWriteResponse = null;
        try {
            objectWriteResponse = client.putObject(putObjectRequest);
            FileResult fileResult = resultConverter.toFileResult(objectWriteResponse);
            return fileResult;
        } catch (Exception e) {
            throw new StorageException(ErrorMessage.STORAGE_EXECUTION_FAIL, e);
        }
    }

    @Override
    public FileResult uploadFile(String bucketName, String key, File file) throws StorageException {
        String fileName = StorageKeyConverter.extractFileNameFromStorageKey(key);
        PutObjectArgs putObjectArgs = requestBuilder.buildPutObjectArgs(bucketName, key, file);
        return this.uploadFile(putObjectArgs);
    }

    @Override
    public FileResult uploadFile(String bucketName, String key, InputStream inputStream) throws StorageException {
        String fileName = StorageKeyConverter.extractFileNameFromStorageKey(key);
        PutObjectArgs putObjectArgs = requestBuilder.buildPutObjectArgs(bucketName, key, inputStream);
        return this.uploadFile(putObjectArgs);
    }

    @Override
    public Boolean bucketExists(String bucketName) throws StorageException {
        MinioClient client = this.buildClient();
        try {
            return client.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException(ErrorMessage.STORAGE_EXECUTION_FAIL, e);
        }
    }

    @Override
    public Boolean keyExists(String bucketName, String key) throws StorageException {
        MinioClient client = this.buildClient();
        try {
            client.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new StorageException(ErrorMessage.STORAGE_EXECUTION_FAIL, e);
        }
    }

    @Override
    public Boolean getVersioning(String bucketName) throws StorageException {
        MinioClient client = this.buildClient();
        try {
            VersioningConfiguration config = client.getBucketVersioning(
                    GetBucketVersioningArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            if (config != null) {
                return VersioningConfiguration.Status.ENABLED.equals(config.status());
            }
            return false;
        } catch (Exception e) {
            throw new StorageException(ErrorMessage.STORAGE_EXECUTION_FAIL, e);
        }
    }

    @Override
    public FileResult copyFile(String bucketName, String sourceKey, String destinationKey) throws StorageException {
        CopyObjectArgs copyObjectArgs = requestBuilder.buildCopyObjectRequest(bucketName, sourceKey, destinationKey);
        return this.copyFile(copyObjectArgs);
    }

    private FileResult copyFile(CopyObjectArgs args) {
        MinioClient client = this.buildClient();
        try {
            ObjectWriteResponse res = client.copyObject(args);
            return resultConverter.toFileResult(res);
        } catch (Exception e) {
            throw new StorageException(ErrorMessage.STORAGE_EXECUTION_FAIL, e);
        }
    }

    @Override
    public FileResult removeFile(String bucketName, String key) throws StorageException {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .object(key)
                .bucket(bucketName)
                .build();

        return this.removeFile(removeObjectArgs);
    }

    private FileResult removeFile(RemoveObjectArgs args) {
        MinioClient client = this.buildClient();
        try {
            client.removeObject(args);
            return BaseFileResult.builder()
                    .key(args.object())
                    .bucket(args.bucket())
                    .build();
        } catch (Exception e) {
            throw new StorageException(ErrorMessage.STORAGE_EXECUTION_FAIL, e);
        }
    }

    @Override
    public FileResult moveFile(String bucketName, String sourceKey, String destinationKey) throws StorageException {
        FileResult copied = this.copyFile(bucketName, sourceKey, destinationKey);
        this.removeFile(bucketName, sourceKey);
        return copied;
    }

    @Override
    public FileResult readFile(String bucketName, String key, String versionId) throws StorageException {
        GetObjectArgs getObjectRequest = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(key)
                .versionId(versionId)
                .build();

        return this.readFile(getObjectRequest);
    }

    @Override
    public FileResult readFile(String bucketName, String key) throws StorageException {
        GetObjectArgs getObjectRequest = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(key)
                .build();
        return this.readFile(getObjectRequest);
    }

    @Override
    public FileResult readFile(String fileId) throws StorageException {
        throw new NotImplementedError("Not Implemented");
    }

    private FileResult readFile(GetObjectArgs args) {
        MinioClient client = this.buildClient();
        try {
            GetObjectResponse res = client.getObject(args);
            return resultConverter.toFileResult(res);
        } catch (Exception e) {
            throw new StorageException(ErrorMessage.STORAGE_EXECUTION_FAIL, e);
        }
    }

    @Override
    public URL generatePreSignedUrl(String bucketName, String key, String versionId, Long expiredTime) throws StorageException {
        GetPresignedObjectUrlArgs generatePreSignedObjectUrlRequest = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .expiry((expiredTime == null ? 1000 * 60 * 5 : expiredTime.intValue()), TimeUnit.MILLISECONDS)
                .bucket(bucketName)
                .object(key)
                .build();
        return this.generatePreSignedUrl(generatePreSignedObjectUrlRequest);
    }

    @Override
    public URL generatePreSignedUrl(String bucketName, String key, String versionId, Long expiredTime, Map<String, String> params) throws StorageException {
        GetPresignedObjectUrlArgs generatePreSignedObjectUrlRequest = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .expiry((expiredTime == null ? 1000 * 60 * 5 : expiredTime.intValue()), TimeUnit.MILLISECONDS)
                .bucket(bucketName)
                .object(key)
                .extraQueryParams(params)
                .build();
        return this.generatePreSignedUrl(generatePreSignedObjectUrlRequest);
    }

    private URL generatePreSignedUrl(GetPresignedObjectUrlArgs args) {
        MinioClient client = this.buildClient();
        try {
            String url = client.getPresignedObjectUrl(args);
            return new URL(url);
        } catch (Exception e) {
            throw new StorageException(ErrorMessage.STORAGE_EXECUTION_FAIL, e);
        }
    }
}
