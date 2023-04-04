package com.example.es.converter;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import com.example.es.entity.request.ValueField;
import org.springframework.stereotype.Service;

@Service
public class TextValueToQueryConverter extends AbstractValueFieldToQueryConverter<String, ValueField.Text> {
    @Override
    public Class<ValueField.Text> getType() {
        return ValueField.Text.class;
    }

    @Override
    MatchQuery.Builder builderWithValue(ValueField.Text valueField) {
        return new MatchQuery.Builder()
                .query(valueField.getValue());
    }
}
