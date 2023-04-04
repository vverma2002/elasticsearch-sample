package com.example.es.converter;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.RangeAggregation;
import com.example.es.entity.request.facet.RangeFacet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NumRangeFacetToAggregationConverter extends AbstractRangeFacetToAggregationConverter<Long, RangeFacet.Num> {
    @Override
    Aggregation rangeAggregation(RangeFacet.Num rangeFacet) {
        String field = rangeFacet.getPath() == null ? rangeFacet.getField()
                : rangeFacet.getPath() + "." + rangeFacet.getField();
        List<AggregationRange> ranges = rangeFacet.getRanges()
                .stream()
                .map(this::range)
                .collect(Collectors.toList());
        return new Aggregation.Builder()
                .range(new RangeAggregation.Builder()
                        .field(field)
                        .ranges(ranges)
                        .build())
                .build();
    }

    private AggregationRange range(RangeFacet.Range<Long> range) {
        AggregationRange.Builder builder = new AggregationRange.Builder();
        Long from = range.getFrom();
        if (from != null) {
            builder.from(String.valueOf(from));
        }
        Long to = range.getTo();
        if (to != null) {
            builder.to(String.valueOf(to));
        }
        return builder.build();
    }

    @Override
    public Class<RangeFacet.Num> getType() {
        return RangeFacet.Num.class;
    }
}
