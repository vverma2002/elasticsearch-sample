package com.example.es.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.example.es.entity.request.ValueField;
import com.example.es.entity.request.ValueField.DateTime;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery.Builder;

@Service
public class DateTimeValueToQueryConverter
        extends AbstractValueFieldToQueryConverter<LocalDateTime, ValueField.DateTime> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public Class<DateTime> getType() {
        return DateTime.class;
    }

    @Override
    Builder builderWithValue(DateTime valueField) {
        return new MatchQuery.Builder()
                .field(DATE_FORMATTER.format(valueField.getValue()));
    }

}
