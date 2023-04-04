package com.example.es.converter;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.es.entity.request.ValueField;
import org.springframework.core.convert.converter.Converter;

public interface ValueFieldToQueryConverter<T, V extends ValueField<T>> extends Converter<V, Query> {

    Class<V> getType();
}
