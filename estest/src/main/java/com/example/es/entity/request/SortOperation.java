package com.example.es.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortOperation {
    @JsonProperty("asc")
    ASC("asc"),
    @JsonProperty("desc")
    DESC("desc");

    private final String order;
}
