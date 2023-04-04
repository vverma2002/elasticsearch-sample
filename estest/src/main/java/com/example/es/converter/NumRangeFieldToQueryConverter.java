package com.example.es.converter;

import co.elastic.clients.json.JsonpMapper;
import com.example.es.entity.request.RangeField;
import org.springframework.stereotype.Service;

@Service
public class NumRangeFieldToQueryConverter extends AbstractRangeFieldToQueryConverter<Long, RangeField.Num> {
    public NumRangeFieldToQueryConverter(JsonpMapper mapper) {
        super(mapper);
    }

    @Override
    public Class<RangeField.Num> getType() {
        return RangeField.Num.class;
    }
}
