package com.example.es.converter;

import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.example.es.entity.request.RangeField;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractRangeFieldToQueryConverter<T, V extends RangeField<T>>
        implements RangeFieldToQueryConverter<T, V> {

    private final JsonpMapper mapper;

    @Override
    public Query convert(V rangeField) {
        QueryVariant query;
        if (rangeField.getPath() != null) {
            query = createNestedQuery(rangeField);
        } else {
            query = createRangeQuery(rangeField);
        }
        return new Query(query);
    }

    private QueryVariant createNestedQuery(V rangeField) {
        return new NestedQuery.Builder()
                .path(rangeField.getPath())
                .query(new Query(createRangeQuery(rangeField)))
                .build();
    }

    private RangeQuery createRangeQuery(V rangeField) {
        RangeQuery.Builder builder = new RangeQuery.Builder()
                .field(defineField(rangeField));
        less(builder, rangeField);
        greater(builder, rangeField);
        return builder.build();
    }

    private void less(RangeQuery.Builder builder, V rangeField) {
        JsonData max = toJsonData(max(rangeField));
        boolean includeMax = rangeField.isIncludeMax();
        if (includeMax) {
            builder.lte(max);
        } else {
            builder.lt(max);
        }
    }

    private void greater(RangeQuery.Builder builder, V rangeField) {
        JsonData min = toJsonData(min(rangeField));
        boolean includeMin = rangeField.isIncludeMin();
        if (includeMin) {
            builder.gte(min);
        } else {
            builder.gt(min);
        }
    }

    private JsonData toJsonData(Object value) {
        if (value == null) {
            return null;
        }
        return JsonData.of(value, mapper);
    }

    Object min(V rangeField) {
        return rangeField.getMin();
    }

    Object max(V rangeField) {
        return rangeField.getMax();
    }

    private String defineField(V rangeField) {
        String path = rangeField.getPath();
        String field = rangeField.getField();
        if (path == null) {
            return field;
        } else {
            return path + "." + field;
        }

    }
}
