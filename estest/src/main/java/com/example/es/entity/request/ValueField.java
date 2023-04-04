package com.example.es.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ValueField.Text.class, name = "string"),
        @JsonSubTypes.Type(value = ValueField.Date.class, name = "date"),
        @JsonSubTypes.Type(value = ValueField.DateTime.class, name = "datetime"),
        @JsonSubTypes.Type(value = ValueField.Num.class, name = "long"),
        @JsonSubTypes.Type(value = ValueField.FPNum.class, name = "double")
})
public interface ValueField<T> {
    String getType();

    T getValue();

    String getPath();

    String getField();

    @Data
    class Text implements ValueField<String> {
        private String value;
        private String type;
        private String path;
        private String field;
    }

    @Data
    class Date implements ValueField<LocalDate> {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        private LocalDate value;
        private String type;
        private String path;
        private String field;
    }

    @Data
    class DateTime implements ValueField<LocalDateTime> {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime value;
        private String type;
        private String path;
        private String field;
    }

    @Data
    class Num implements ValueField<Long> {
        private Long value;
        private String type;
        private String path;
        private String field;
    }

    @Data
    class FPNum implements ValueField<Double> {
        private Double value;
        private String type;
        private String path;
        private String field;
    }
}
