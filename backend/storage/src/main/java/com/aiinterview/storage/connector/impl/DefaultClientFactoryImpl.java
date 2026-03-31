package com.aiinterview.storage.connector.impl;

import com.aiinterview.storage.authenticator.CredentialProvider;
import com.aiinterview.storage.connector.StorageClientFactory;
import com.aiinterview.storage.exceptions.ErrorMessage;
import com.aiinterview.storage.exceptions.StorageException;
import com.aiinterview.storage.model.StorageType;
import com.aiinterview.storage.model.storage.StorageClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultClientFactoryImpl implements StorageClientFactory {
    private Map<StorageType, StorageClient> clients = new HashMap<>();

    private final CredentialProvider credentialProvider;

    public DefaultClientFactoryImpl(CredentialProvider credentialProvider) {
        this.credentialProvider = credentialProvider;
    }

    @Override
    public StorageClient getClient(StorageType type) {
        StorageClient client = clients.get(type);
        if(Objects.isNull(client)){
            throw new StorageException(ErrorMessage.STORAGE_NOT_IMPLEMENTED);
        }
        return client;

    }

    @Override
    public void registerClient(StorageType type, StorageClient client) {
        if(Objects.isNull(clients)){
            clients = new HashMap<>();
        }
        clients.put(type, client);
    }

    @Override
    public StorageClient getClient() {
        StorageType storageType = credentialProvider.getStorageType();
        return this.getClient(storageType);
    }
}
