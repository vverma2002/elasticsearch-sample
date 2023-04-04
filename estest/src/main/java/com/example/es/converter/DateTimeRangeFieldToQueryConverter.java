package com.example.es.converter;

import co.elastic.clients.json.JsonpMapper;
import com.example.es.entity.request.RangeField;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateTimeRangeFieldToQueryConverter extends AbstractRangeFieldToQueryConverter<LocalDateTime, RangeField.DateTime> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public DateTimeRangeFieldToQueryConverter(JsonpMapper mapper) {
        super(mapper);
    }

    @Override
    public Class<RangeField.DateTime> getType() {
        return RangeField.DateTime.class;
    }

    @Override
    Object min(RangeField.DateTime rangeField) {
        LocalDateTime min = rangeField.getMin();
        if (min == null) {
            return null;
        }
        return DATE_FORMATTER.format(min);
    }

    @Override
    Object max(RangeField.DateTime rangeField) {
        LocalDateTime max = rangeField.getMax();
        if (max == null) {
            return null;
        }
        return DATE_FORMATTER.format(max);
    }
}
