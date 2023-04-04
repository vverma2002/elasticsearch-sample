package com.example.es.converter;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.NestedAggregation;
import com.example.es.entity.request.facet.RangeFacet;

public abstract class AbstractRangeFacetToAggregationConverter<T, V extends RangeFacet<T>>
        implements RangeFacetToAggregationConverter<T, V> {
    @Override
    public Aggregation convert(V rangeFacet) {
        String path = rangeFacet.getPath();
        if (path != null) {
            return nestedRangeAggregation(rangeFacet);
        } else {
            return rangeAggregation(rangeFacet);
        }
    }

    private Aggregation nestedRangeAggregation(V rangeFacet) {
        return new Aggregation.Builder()
                .nested(new NestedAggregation.Builder()
                        .path(rangeFacet.getPath())
                        .build())
                .aggregations(rangeFacet.getPath(), rangeAggregation(rangeFacet))
                .build();
    }


    abstract Aggregation rangeAggregation(V rangeFacet);
}
