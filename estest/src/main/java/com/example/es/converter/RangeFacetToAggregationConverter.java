package com.example.es.converter;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.example.es.entity.request.facet.RangeFacet;
import org.springframework.core.convert.converter.Converter;

public interface RangeFacetToAggregationConverter<T, V extends RangeFacet<T>> extends Converter<V, Aggregation> {
    Class<V> getType();
}
