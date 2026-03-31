package com.aiinterview.storage.util;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class InputStreamCollector {
    ByteArrayOutputStream targetStream = new ByteArrayOutputStream();


    @SneakyThrows
    public InputStreamCollector collectInputStream(InputStream input) {
        IOUtils.copy(input, targetStream);
        return this;
    }

    public InputStream getStream() {
        return new ByteArrayInputStream(targetStream.toByteArray());
    }

    public byte[] getBytes() {
        return targetStream.toByteArray();
    }
}
