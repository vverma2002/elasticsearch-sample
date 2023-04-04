package com.example.es.converter;

import co.elastic.clients.json.JsonpMapper;
import com.example.es.entity.request.RangeField;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DateRangeFieldToQueryConverter extends AbstractRangeFieldToQueryConverter<LocalDate, RangeField.Date> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public DateRangeFieldToQueryConverter(JsonpMapper mapper) {
        super(mapper);
    }

    @Override
    public Class<RangeField.Date> getType() {
        return RangeField.Date.class;
    }

    @Override
    Object min(RangeField.Date rangeField) {
        LocalDate min = rangeField.getMin();
        if (min == null) {
            return null;
        }
        return DATE_FORMATTER.format(min);
    }

    @Override
    Object max(RangeField.Date rangeField) {
        LocalDate max = rangeField.getMax();
        if (max == null) {
            return null;
        }
        return DATE_FORMATTER.format(max);
    }
}
