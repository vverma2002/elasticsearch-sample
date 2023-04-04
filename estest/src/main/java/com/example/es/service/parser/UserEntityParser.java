package com.example.es.service.parser;

import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.example.es.entity.search.document.LocaleEntity;
import com.example.es.entity.search.document.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class UserEntityParser implements Parser<UserEntity> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final JsonpMapper jsonpMapper;
    private final Map<String, FieldDeserializer<UserEntity>> deserializers = Map.of(
            "id", new ValueFieldDeserializer<>(UserEntity::setId, Long[].class),
            "nameEn", new ValueFieldDeserializer<>(UserEntity::setNameEn, String[].class),
            "nameAr", new ValueFieldDeserializer<>(UserEntity::setNameAr, String[].class),
            "nameEnCopy", new ValueFieldDeserializer<>(UserEntity::setNameEnCopy, String[].class),
            "gender", new ValueFieldDeserializer<>(UserEntity::setGender, String[].class),
            "dateOfBirth", new DateFieldDeserializer<>(UserEntity::setDateOfBirth),
            "favouriteNumber", new ValueFieldDeserializer<>(UserEntity::setFavouriteNumber, Integer[].class),
            "placeOfBirth", new LocaleFieldDeserializer<>(UserEntity::setPlaceOfBirth)
    );

    private final Map<String, FieldDeserializer<LocaleEntity>> localeDeserializers = Map.of(
            "en", new ValueFieldDeserializer<>(LocaleEntity::setEn, String[].class),
            "ar", new ValueFieldDeserializer<>(LocaleEntity::setAr, String[].class)
    );

    @Override
    public UserEntity parse(Map<String, JsonData> fields) {
        UserEntity userEntity = new UserEntity();
        deserializers.forEach((key, value) -> {
            JsonData jsonData = fields.get(key);
            value.deserialize(userEntity, jsonData);
        });
        return userEntity;
    }

    @Override
    public Class<UserEntity> getType() {
        return UserEntity.class;
    }

    private interface FieldDeserializer<T> {
        void deserialize(T entity, JsonData field);
    }

    @Value
    private class ValueFieldDeserializer<T, V> implements FieldDeserializer<V> {
        BiConsumer<V, T> setter;
        Class<T[]> arrayType;

        @Override
        public void deserialize(V entity, JsonData field) {
            if (field == null) {
                return;
            }
            T[] array = field.to(arrayType, jsonpMapper);
            if (array != null && array.length != 0) {
                setter.accept(entity, array[0]);
            }
        }
    }

    @Value
    private class DateFieldDeserializer<V> implements FieldDeserializer<V> {
        BiConsumer<V, LocalDate> setter;

        @Override
        public void deserialize(V entity, JsonData field) {
            if (field == null) {
                return;
            }
            String[] array = field.to(String[].class, jsonpMapper);
            if (array != null && array.length != 0) {
                setter.accept(entity, LocalDate.parse(array[0], DATE_FORMATTER));
            }
        }
    }

    @Value
    private class LocaleFieldDeserializer<V> implements FieldDeserializer<V> {
        BiConsumer<V, LocaleEntity> setter;

        @Override
        public void deserialize(V entity, JsonData field) {
            if (field == null) {
                return;
            }
            Map<String, List<String>>[] array = field.to(Map[].class, jsonpMapper);
            if (array != null && array.length != 0) {
                LocaleEntity localeEntity = new LocaleEntity();
                localeDeserializers.forEach((key, value) -> {
                    List<String> val = array[0].get(key);
                    JsonData jsonData = val != null ? JsonData.of(val) : null;
                    value.deserialize(localeEntity, jsonData);
                });
                setter.accept(entity, localeEntity);
            }
        }
    }
}
