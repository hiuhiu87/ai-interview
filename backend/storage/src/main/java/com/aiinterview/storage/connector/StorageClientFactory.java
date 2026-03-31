package com.aiinterview.storage.connector;

import com.aiinterview.storage.model.StorageType;
import com.aiinterview.storage.model.storage.StorageClient;

public interface StorageClientFactory {
    StorageClient getClient(StorageType type);
    void registerClient(StorageType type, StorageClient client);
    StorageClient getClient();
}
