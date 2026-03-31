package com.aiinterview.storage.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class StorageKeyConverter {
    public static  String extractFileNameFromStorageKey(String key){
        String fileName = key;
        if(Objects.nonNull(key)){
            String[] pathTokens = key.split("/");
            if(pathTokens.length > 0){
                fileName = pathTokens[pathTokens.length - 1];
            }
        }
        return fileName;
    }

    public static  String extractSubPathFromStorageKey(String key){
        String subPath = "";
        if(Objects.nonNull(key)){
            String[] pathTokens = key.split("/");
            if(pathTokens.length > 1){
                subPath = String.join("/", Arrays.stream(pathTokens).collect(Collectors.toList()).subList(0, pathTokens.length -1 ));
            }
        }
        return subPath;
    }
}
