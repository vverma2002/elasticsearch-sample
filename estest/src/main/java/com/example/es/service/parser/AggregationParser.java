package com.example.es.service.parser;

import co.elastic.clients.elasticsearch._types.aggregations.*;
import com.example.es.entity.search.facet.Facet;
import com.example.es.entity.search.facet.FieldFacets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AggregationParser implements Parser.Aggregation {
    @Override
    public List<FieldFacets> parse(Map<String, Aggregate> aggregates) {
        return aggregates.entrySet().stream()
                .map(parseAggregation())
                .collect(Collectors.toList());
    }

    private Function<Map.Entry<String, Aggregate>, FieldFacets> parseAggregation() {
        return entry -> FieldFacets.builder()
                .name(entry.getKey())
                .facets(Parser.findParserForAggregate(entry.getValue()).parser.apply(entry.getValue()))
                .build();
    }

    @RequiredArgsConstructor
    private enum Parser {
        L_TERM(Aggregate::isLterms, aggregate -> {
            LongTermsAggregate lterms = aggregate.lterms();
            Buckets<LongTermsBucket> buckets = lterms.buckets();
            return buckets.array()
                    .stream()
                    .map(bucket -> Facet.builder()
                            .value(bucket.keyAsString() == null ? bucket.key() : bucket.keyAsString())
                            .count(bucket.docCount())
                            .build())
                    .collect(Collectors.toList());
        }),
        S_TERM(Aggregate::isSterms, aggregate -> {
            StringTermsAggregate sterms = aggregate.sterms();
            Buckets<StringTermsBucket> buckets = sterms.buckets();
            return buckets.array()
                    .stream()
                    .map(bucket -> Facet.builder()
                            .value(bucket.key())
                            .count(bucket.docCount())
                            .build())
                    .collect(Collectors.toList());
        }),
        NESTED(Aggregate::isNested, aggregate -> {
            NestedAggregate nested = aggregate.nested();
            Map<String, Aggregate> aggregations = nested.aggregations();
            return aggregations.entrySet()
                    .stream()
                    .flatMap(entry -> Parser.findParserForAggregate(entry.getValue())
                            .parser.apply(entry.getValue()).stream())
                    .collect(Collectors.toList());
        }),
        RANGE(Aggregate::isRange, aggregate -> {
            RangeAggregate range = aggregate.range();
            Buckets<RangeBucket> buckets = range.buckets();
            return buckets.array()
                    .stream()
                    .map(bucket -> Facet.builder()
                            .value(bucket.key())
                            .count(bucket.docCount())
                            .build())
                    .collect(Collectors.toList());
        }),
        DATE_RANGE(Aggregate::isDateRange, aggregate -> {
            DateRangeAggregate dateRangeAggregate = aggregate.dateRange();
            Buckets<RangeBucket> buckets = dateRangeAggregate.buckets();
            return buckets.array()
                    .stream()
                    .map(bucket -> Facet.builder()
                            .value(bucket.key())
                            .count(bucket.docCount())
                            .build())
                    .collect(Collectors.toList());
        });

        private final Predicate<Aggregate> predicate;
        private final Function<Aggregate, List<Facet>> parser;

        private static Parser findParserForAggregate(Aggregate aggregate) {
            return Stream.of(values())
                    .filter(v -> v.predicate.test(aggregate))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Unable to find parser"));
        }
    }
}
