package com.example.es.service.converter;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.es.entity.request.facet.FacetsPayload;

public interface Converter {
    interface Service {
        interface ValueField<T, V extends com.example.es.entity.request.ValueField<T>> {
            Query convert(V valueField);
        }

        interface RangeField<T, V extends com.example.es.entity.request.RangeField<T>> {
            Query convert(V rangeField);
        }

        interface RangeFacet<T, V extends com.example.es.entity.request.facet.RangeFacet<T>> {
            Aggregation convert(V rangeFacet);
        }

        interface ValueFacet {
            Aggregation convert(FacetsPayload.ValueFacet rangeFacet);
        }
    }
}
