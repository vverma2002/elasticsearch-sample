package com.example.es.service.converter;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.NestedAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import com.example.es.converter.RangeFacetToAggregationConverter;
import com.example.es.entity.request.facet.FacetsPayload;
import com.example.es.entity.request.facet.RangeFacet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FacetConverterService<T, V extends RangeFacet<T>> implements Converter.Service.ValueFacet,
        Converter.Service.RangeFacet<T, V> {

    private final Map<Class<V>, RangeFacetToAggregationConverter<T, V>> converters;

    public FacetConverterService(List<RangeFacetToAggregationConverter<T, V>> converters) {
        this.converters = converters.stream()
                .collect(Collectors.toMap(RangeFacetToAggregationConverter::getType, Function.identity()));
    }

    @Override
    public Aggregation convert(V rangeFacet) {
        return converters.get(rangeFacet.getClass())
                .convert(rangeFacet);
    }

    @Override
    public Aggregation convert(FacetsPayload.ValueFacet rangeFacet) {
        String path = rangeFacet.getPath();
        String field = path == null ? rangeFacet.getField() : path + "." + rangeFacet.getField();
        Aggregation aggregation = new Aggregation.Builder()
                .terms(new TermsAggregation.Builder()
                        .field(field)
                        .build())
                .build();
        if (path == null) {
            return aggregation;
        } else {
            return new Aggregation.Builder()
                    .nested(new NestedAggregation.Builder()
                            .path(path)
                            .build())
                    .aggregations(path, aggregation)
                    .build();
        }
    }
}
