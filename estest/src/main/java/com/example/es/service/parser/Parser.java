package com.example.es.service.parser;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.json.JsonData;
import com.example.es.entity.search.facet.FieldFacets;

import java.util.List;
import java.util.Map;

public interface Parser<T> {
    T parse(Map<String, JsonData> fields);

    Class<T> getType();

    interface Service {
        <T> T parse(Map<String, JsonData> fields, Class<T> document);
    }

    interface Aggregation {
        List<FieldFacets> parse(Map<String, Aggregate> aggregates);
    }
}
