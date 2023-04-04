package com.example.es.converter;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.example.es.entity.request.ValueField;

public abstract class AbstractValueFieldToQueryConverter<T, V extends ValueField<T>> implements ValueFieldToQueryConverter<T, V> {
    @Override
    public Query convert(V valueField) {
        QueryVariant query;
        if (valueField.getPath() != null) {
            query = createNestedQuery(valueField);
        } else {
            query = createMatchQuery(valueField);
        }
        return new Query(query);
    }

    private QueryVariant createNestedQuery(V valueField) {
        return new NestedQuery.Builder()
                .path(valueField.getPath())
                .query(new Query(createMatchQuery(valueField)))
                .build();
    }

    private MatchQuery createMatchQuery(V valueField) {
        return builderWithValue(valueField)
                .field(defineField(valueField))
                .build();
    }

    private String defineField(V valueField) {
        String path = valueField.getPath();
        String field = valueField.getField();
        if (path == null) {
            return field;
        } else {
            return path + "." + field;
        }

    }

    abstract MatchQuery.Builder builderWithValue(V valueField);
}
