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
        @JsonSubTypes.Type(value = RangeField.Date.class, name = "date"),
        @JsonSubTypes.Type(value = RangeField.DateTime.class, name = "datetime"),
        @JsonSubTypes.Type(value = RangeField.Num.class, name = "long"),
        @JsonSubTypes.Type(value = RangeField.FPNum.class, name = "double")
})
public interface RangeField<T> {
    T getMin();

    T getMax();

    String getType();

    String getField();

    String getPath();

    boolean isIncludeMin();

    boolean isIncludeMax();

    @Data
    class Num implements RangeField<Long> {
        private Long min;
        private Long max;
        private String field;
        private String type;
        private String path;
        private boolean includeMin;
        private boolean includeMax;
    }

    @Data
    class FPNum implements RangeField<Double> {
        private Double min;
        private Double max;
        private String field;
        private String type;
        private String path;
        private boolean includeMin;
        private boolean includeMax;
    }


    @Data
    class Date implements RangeField<LocalDate> {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        private LocalDate min;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        private LocalDate max;
        private String field;
        private String type;
        private String path;
        private boolean includeMin;
        private boolean includeMax;
    }

    @Data
    class DateTime implements RangeField<LocalDateTime> {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime min;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime max;
        private String field;
        private String type;
        private String path;
        private boolean includeMin;
        private boolean includeMax;
    }
}
