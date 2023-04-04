package com.example.es.converter;

import org.springframework.stereotype.Service;

import com.example.es.entity.request.ValueField;
import com.example.es.entity.request.ValueField.Num;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery.Builder;

@Service
public class NumValueToQueryConverter extends AbstractValueFieldToQueryConverter<Long, ValueField.Num> {

    @Override
    public Class<Num> getType() {
        return ValueField.Num.class;
    }

    @Override
    Builder builderWithValue(Num valueField) {
        return new MatchQuery.Builder()
                .query(valueField.getValue());
    }

}
