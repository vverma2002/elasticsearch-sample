package com.example.es.service.converter;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.es.converter.RangeFieldToQueryConverter;
import com.example.es.entity.request.RangeField;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RangeFieldConverterService<T, V extends RangeField<T>> implements Converter.Service.RangeField<T, V> {
    private final Map<Class<V>, RangeFieldToQueryConverter<T, V>> converters;

    public RangeFieldConverterService(List<RangeFieldToQueryConverter<T, V>> converters) {
        this.converters = converters.stream()
                .collect(Collectors.toMap(RangeFieldToQueryConverter::getType, Function.identity()));
    }

    @Override
    public Query convert(V rangeField) {
        return converters.get(rangeField.getClass())
                .convert(rangeField);
    }
}
