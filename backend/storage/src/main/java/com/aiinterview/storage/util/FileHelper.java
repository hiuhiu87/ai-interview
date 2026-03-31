package com.aiinterview.storage.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FileHelper {
    public static Map<String, String> mimeTypeMap = new HashMap<>(){{
        put("pdf","application/pdf");
        put("doc","application/msword");
        put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        put("xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        put("pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation");
        put("txt","text/plain");
        put("json","application/json");
        put("mp4","video/mp4");
        put("mp4s","application/mp4");
        put("txt","text/plain");
        put("jpe","image/jpeg");
        put("jpeg","image/jpeg");
        put("jpg","image/jpeg");
        put("pjpg","image/jpeg");
        put("jfif","image/jpeg");
        put("jfif-tbnl","image/jpeg");
        put("jif","image/jpeg");
        put("gif","image/gif");
        put("ico","image/x-icon");
        put("png","image/png");
        put("svg","image/svg+xml");
        put("svgz","image/svg+xml");
        put("zip","application/zip");
    }};
    public static FileInputStream toFiLeInputStream(File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error("File Not Found",e);
        }
        return inputStream;
    }

    public static InputStream getFileInputStream(MultipartFile file){
        log.debug("getFileInputStream(): START");
        InputStream inputStream = null;
        try{
            inputStream = file.getInputStream();
        } catch (IOException e) {
            log.error("Invalid File Input Stream",e);
        }
        log.debug("getFileInputStream(): END");
        return inputStream;
    }

    public static String guessFileMimeType(String fileName) {
        log.debug("getFileExtension(): START");
        String mimeType= null;
        Pattern fileNamePattern = Pattern.compile("(?<name>.*)\\.(?<extension>.+)");
        Matcher matchResult = fileNamePattern.matcher(fileName);
        if(matchResult.find()){
            String extension = matchResult.group("extension");
            if(Objects.nonNull(extension) && mimeTypeMap.containsKey(extension)){
                mimeType = mimeTypeMap.get(extension);
            }
        }
        log.debug("getFileExtension(): END");
        return mimeType;
    }

    public static String encodeValue(String value){
        log.debug("encodeValue(): START");
        String encodeValue = value;
        if(Objects.nonNull(value)){
            encodeValue = URLEncoder.encode(encodeValue, StandardCharsets.UTF_8);
        }
        log.debug("encodeValue(): END");
        return encodeValue;
    }
}
