package com.aiinterview.storage.authenticator;

import com.aiinterview.storage.model.StorageType;
import com.aiinterview.storage.model.credential.common.StorageCredential;

public interface CredentialProvider {
    StorageCredential storeCredential(StorageCredential credential);

    StorageCredential getCredential(StorageType type);

    StorageType getStorageType();
}
