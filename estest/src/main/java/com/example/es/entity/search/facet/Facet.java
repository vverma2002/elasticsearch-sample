package com.example.es.entity.search.facet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Facet {
    String value;
    long count;
}
