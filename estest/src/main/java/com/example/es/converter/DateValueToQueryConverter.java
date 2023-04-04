package com.example.es.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.example.es.entity.request.ValueField;
import com.example.es.entity.request.ValueField.Date;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery.Builder;

@Service
public class DateValueToQueryConverter extends AbstractValueFieldToQueryConverter<LocalDate, ValueField.Date> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public Class<Date> getType() {
        return Date.class;
    }

    @Override
    Builder builderWithValue(Date valueField) {
        return new MatchQuery.Builder()
                .query(DATE_FORMATTER.format(valueField.getValue()));
    }

}
