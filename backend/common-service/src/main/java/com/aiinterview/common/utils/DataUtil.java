package com.aiinterview.common.utils;

import com.aiinterview.common.constant.ValidationConstants;
import com.aiinterview.common.exceptions.ErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class DataUtil {

    private static final Random RANDOM = new Random();
    private static final ObjectMapper OBJECT_MAPPER;
    private static final Pattern CODE_PATTERN = Pattern.compile("^(.*?)-(.*?)-(\\d+)$");


    static {
        SimpleModule simpleModule = new JavaTimeModule();

        OBJECT_MAPPER = JsonMapper.builder()
                .addModule(simpleModule)
                .build();
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static String toPascalCase(String str) {
        StringBuilder pascalCase = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : str.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                pascalCase.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                pascalCase.append(Character.toLowerCase(c));
            }
        }

        return pascalCase.toString();
    }

    /**
     * Removes extra spaces from a given string.
     *
     * @param str the input string with extra spaces
     * @return the string with extra spaces removed
     */
    public static String removeExtraSpaces(String str) {
        String[] words = str.split("\\s+"); // Split by one or more whitespace characters
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                sb.append(" "); // Add a space after the first word
            }
            sb.append(words[i]);
        }
        return sb.toString().trim(); // Trim leading and trailing spaces
    }

    public static boolean nullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean nullOrEmpty(List<String> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        for (String str : list) {
            if (str != null && !str.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static String randomDit(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(RANDOM.nextInt(10));
        }
        return builder.toString();
    }

    public static String writeAsJson(Object input) {
        try {
            return OBJECT_MAPPER.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            log.error("Parse json error", e);
            return null;
        }
    }

    public static <T> T readJson(String json, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("Read json error", e);
            return null;
        }
    }

    public static <T> T readJson(String json, TypeReference<T> typeReference) {
        try {
            if (json == null || json.isEmpty()) {
                return null;
            }
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Read json error", e);
            return null;
        }
    }

    public static <T> T readJson(byte[] bytes, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(bytes, type);
        } catch (IOException e) {
            log.error("Read json error", e);
            return null;
        }
    }

    public static <T> T readJson(byte[] bytes, TypeReference<T> type) {
        try {
            return OBJECT_MAPPER.readValue(bytes, type);
        } catch (IOException e) {
            log.error("Read json error", e);
            return null;
        }
    }

    public static Map<String, Object> objectToMap(Object input) {
        return OBJECT_MAPPER.convertValue(input, new TypeReference<>() {
        });
    }

    public static <T> T convert(Map<String, Object> data, Class<T> type) {
        try {
            return OBJECT_MAPPER.convertValue(data, type);
        } catch (IllegalArgumentException e) {
            log.error("Convert Map to object fail: ", e);
            return null;
        }
    }

    public static <T> T convert(Map<String, Object> data, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.convertValue(data, typeReference);
        } catch (IllegalArgumentException e) {
            log.error("Convert Map to object fail: ", e);
            return null;
        }
    }

    public static Map<String, Object> jsonStringToMap(String value) {
        if (Objects.isNull(value) || StringUtils.isBlank(value))
            return new HashMap<>();

        try {
            return OBJECT_MAPPER.readValue(value, Map.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static <T> T deepCopy(Object object, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(object), type);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T withDefaultValue(T o, T defaultValue) {
        return o == null ? defaultValue : o;
    }

    public static Map<String, String> objectToMapString(Object input) {
        return OBJECT_MAPPER.convertValue(input, new TypeReference<>() {
        });
    }

    public List<String> parseCommaSeparated(String input) {
        if (StringUtils.isBlank(input)) {
            return Collections.emptyList();
        }

        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .toList();
    }

}
