package com.aiinterview.storage.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Collection;
import java.util.List;

public class ObjectMapperUtils {

    public static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Model mapper property setting are specified in the fo
     * llowing block. Default
     * property matching strategy is set to Strict see {@link MatchingStrategies}
     * Custom mappings are added using {@link objectMapper#addMappings(PropertyMap)}
     */
    static {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new JavaTimeModule());

    }

    /**
     * Hide from public usage.
     */
    private ObjectMapperUtils() {
    }

    /**
     * <p>
     * Note: outClass object must have default constructor with no arguments
     * </p>
     *
     * @param <D>      type of result object.
     * @param <T>      type of source object to map from.
     * @param entity   entity that needs to be mapped.
     * @param outClass class of result object.
     * @return new object of <code>outClass</code> type.
     */
    public static <D, T> D map(final T entity, Class<D> outClass) {

        return objectMapper.convertValue(entity,outClass);
    }

    /**
     * <p>
     * Note: outClass object must have default constructor with no arguments
     * </p>
     *
     * @param entityList list of entities that needs to be mapped
     * @param outCLass   class of result list element
     * @param <D>        type of objects in result list
     * @param <T>        type of entity in <code>entityList</code>
     * @return list of mapped object with <code><D></code> type.
     */
    public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
        return objectMapper.convertValue(entityList, objectMapper.getTypeFactory().constructCollectionType(List.class, outCLass));
    }

    public static <T> String jsonStringify(T data) {
        try{
            String jsonData = objectMapper.writeValueAsString(data);
            return jsonData;
        }catch(JsonProcessingException e){
           return null;
        }
    }

    public static  <T> T parseJson(String data, Class<T> outCLass) {
        try{
            return objectMapper.readValue(data,outCLass);
        }catch(JsonProcessingException e){
            return null;
        }
    }
}