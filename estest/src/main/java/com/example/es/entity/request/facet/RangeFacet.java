package com.example.es.entity.request.facet;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RangeFacet.Date.class, name = "date"),
        @JsonSubTypes.Type(value = RangeFacet.Num.class, name = "long")
})
public interface RangeFacet<T> {
    String getType();

    List<Range<T>> getRanges();

    String getField();

    String getPath();


    @Data
    class Range<T> {
        private T from;
        private T to;
    }

    @Data
    class Num implements RangeFacet<Long> {
        private List<Range<Long>> ranges;
        private String field;
        private String path;
        private String type;
    }

    @Data
    class Date implements RangeFacet<String> {
        private List<Range<String>> ranges;
        private String field;
        private String path;
        private String type;
    }
}
