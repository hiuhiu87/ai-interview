package com.aiinterview.storage.model.credential.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.aiinterview.storage.model.StorageType;
import com.aiinterview.storage.model.credential.minio.MinioCredential;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MinioCredential.class, name = "MINIO"),
})
public interface StorageCredential {
    StorageType getType();

    void setType(StorageType type);
}
