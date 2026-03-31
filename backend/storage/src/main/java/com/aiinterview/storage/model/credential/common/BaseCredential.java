package com.aiinterview.storage.model.credential.common;

import com.aiinterview.storage.model.StorageType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class BaseCredential  implements StorageCredential{
    protected StorageType type;

    public BaseCredential(StorageType type) {
        this.type = type;
    }
}
