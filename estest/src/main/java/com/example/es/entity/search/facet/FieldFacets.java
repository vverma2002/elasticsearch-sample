package com.example.es.entity.search.facet;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@Builder
@RequiredArgsConstructor
public class FieldFacets {
    String name;
    List<Facet> facets;
}
