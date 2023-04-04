package com.example.es.entity.search.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
@AllArgsConstructor
public class UpdateOperation {
    Long id;
    Map<String, Object> updateValue;
}
