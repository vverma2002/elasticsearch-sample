package com.example.es.service.converter;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.es.converter.ValueFieldToQueryConverter;
import com.example.es.entity.request.ValueField;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ValueFieldConverterService<T, V extends ValueField<T>> implements Converter.Service.ValueField<T, V> {
    private final Map<Class<V>, ValueFieldToQueryConverter<T, V>> converters;

    public ValueFieldConverterService(List<ValueFieldToQueryConverter<T, V>> converters) {
        this.converters = converters.stream()
                .collect(Collectors.toMap(ValueFieldToQueryConverter::getType, Function.identity()));
    }

    @Override
    public Query convert(V valueField) {
        return converters.get(valueField.getClass())
                .convert(valueField);
    }

}
