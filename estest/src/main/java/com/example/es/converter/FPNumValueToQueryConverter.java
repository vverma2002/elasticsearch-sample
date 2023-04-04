package com.example.es.converter;

import org.springframework.stereotype.Service;

import com.example.es.entity.request.ValueField;
import com.example.es.entity.request.ValueField.FPNum;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery.Builder;

@Service
public class FPNumValueToQueryConverter extends AbstractValueFieldToQueryConverter<Double, ValueField.FPNum> {

    @Override
    public Class<FPNum> getType() {
        return FPNum.class;
    }

    @Override
    Builder builderWithValue(FPNum valueField) {
        return new MatchQuery.Builder()
                .query(valueField.getValue());
    }

}
