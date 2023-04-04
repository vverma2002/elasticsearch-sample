package com.example.es.converter;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.DateRangeAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.DateRangeExpression;
import co.elastic.clients.elasticsearch._types.aggregations.FieldDateMath;
import com.example.es.entity.request.facet.RangeFacet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DateRangeFacetToAggregationConverter extends AbstractRangeFacetToAggregationConverter<String, RangeFacet.Date> {
    @Override
    Aggregation rangeAggregation(RangeFacet.Date rangeFacet) {
        String field = rangeFacet.getPath() == null ? rangeFacet.getField()
                : rangeFacet.getPath() + "." + rangeFacet.getField();
        List<DateRangeExpression> ranges = rangeFacet.getRanges()
                .stream()
                .map(this::range)
                .collect(Collectors.toList());
        return new Aggregation.Builder()
                .dateRange(new DateRangeAggregation.Builder()
                        .field(field)
                        .ranges(ranges)
                        .build())
                .build();
    }

    private DateRangeExpression range(RangeFacet.Range<String> range) {
        DateRangeExpression.Builder builder = new DateRangeExpression.Builder();
        String from = range.getFrom();
        if (from != null) {
            FieldDateMath fromDate = new FieldDateMath.Builder()
                    .expr(from)
                    .build();
            builder.from(fromDate);
        }
        String to = range.getTo();
        if (to != null) {
            FieldDateMath toDate = new FieldDateMath.Builder()
                    .expr(to)
                    .build();
            builder.to(toDate);
        }
        return builder.build();
    }

    @Override
    public Class<RangeFacet.Date> getType() {
        return RangeFacet.Date.class;
    }
}
