package com.aiinterview.storage.connector.minio.util;

import com.aiinterview.storage.model.file.FileResult;
import io.minio.GetObjectResponse;
import io.minio.ObjectWriteResponse;

public interface MinioConverter {
    FileResult toFileResult(ObjectWriteResponse objectWriteResponse);

    FileResult toFileResult(GetObjectResponse getObjectResponse);
}
