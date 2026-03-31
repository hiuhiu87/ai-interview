package com.aiinterview.storage.model.credential.minio;

import com.aiinterview.storage.model.StorageType;
import com.aiinterview.storage.model.credential.common.BaseCredential;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinioCredential extends BaseCredential {
    protected String endpoint;
    protected String accessKey;
    protected String secretKey;
    protected String bucket;
    protected String region;
    public MinioCredential() {
        super(StorageType.MINIO);
    }
}
