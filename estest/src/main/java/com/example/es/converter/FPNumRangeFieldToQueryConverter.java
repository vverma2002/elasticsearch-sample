package com.example.es.converter;

import co.elastic.clients.json.JsonpMapper;
import com.example.es.entity.request.RangeField;
import org.springframework.stereotype.Service;

@Service
public class FPNumRangeFieldToQueryConverter extends AbstractRangeFieldToQueryConverter<Double, RangeField.FPNum> {
    public FPNumRangeFieldToQueryConverter(JsonpMapper mapper) {
        super(mapper);
    }

    @Override
    public Class<RangeField.FPNum> getType() {
        return RangeField.FPNum.class;
    }
}
