package com.example.es.converter;

import org.springframework.core.convert.converter.Converter;

import com.example.es.entity.request.RangeField;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public interface RangeFieldToQueryConverter<T, V extends RangeField<T>> extends Converter<V, Query> {
    Class<V> getType();
}
