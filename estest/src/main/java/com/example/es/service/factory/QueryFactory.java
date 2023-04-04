package com.example.es.service.factory;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.NestedSortValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldAndFormat;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import com.example.es.entity.request.*;
import com.example.es.entity.request.facet.FacetsPayload;
import com.example.es.entity.request.facet.RangeFacet;
import com.example.es.entity.request.search.SearchPayload;
import com.example.es.entity.search.Page;
import com.example.es.service.converter.FacetConverterService;
import com.example.es.service.converter.RangeFieldConverterService;
import com.example.es.service.converter.ValueFieldConverterService;
import com.example.es.service.es.Es;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class QueryFactory {
    private final ValueFieldConverterService<?, ValueField<?>> valueFieldConverterService;
    private final RangeFieldConverterService<?, RangeField<?>> rangeFieldConverterService;
    private final FacetConverterService<?, RangeFacet<?>> facetConverterService;
    private final Es.IndexInfo indexInfo;

    public <T> SearchRequest createSearchRequest(SearchPayload searchPayload, Class<T> documentType) {
        Page page = page(searchPayload.getPage());
        List<SortOptions> sortOptions = sort(searchPayload.getSorts());
        Query query = searchQuery(searchPayload);
        List<FieldAndFormat> fields = fields(searchPayload.getFields());
        boolean doSourceFetch = fields == null;
        SearchRequest.Builder builder = new SearchRequest.Builder()
                .source(new SourceConfig.Builder()
                        .fetch(doSourceFetch)
                        .build())
                .index(indexInfo.getAlias(documentType))
                .from(page.getFrom())
                .size(page.getSize())
                .sort(sortOptions)
                .query(query);
        if (fields != null) {
            builder.fields(fields);
        }
        return builder.build();
    }

    private static Page page(Paging paging) {
        int pageNum = paging.getNum();
        int pageSize = paging.getSize();
        return Page.builder()
                .from(pageSize * pageNum - pageSize)
                .size(pageSize)
                .build();
    }

    private static List<SortOptions> sort(List<Sort> sorts) {
        if (sorts == null) {
            return List.of();
        }

        return sorts
                .stream()
                .map(sort -> new SortOptions.Builder()
                        .field(sortedField(sort))
                        .build())
                .collect(Collectors.toList());
    }

    private static FieldSort sortedField(Sort sort) {
        String path = sort.getPath();
        if (path == null) {
            return new FieldSort.Builder()
                    .field(sort.getField())
                    .order(order(sort.getSortOperation()))
                    .build();
        } else {
            return new FieldSort.Builder()
                    .field(path + "." + sort.getField())
                    .nested(new NestedSortValue.Builder()
                            .path(path)
                            .build())
                    .order(order(sort.getSortOperation()))
                    .build();
        }
    }

    private static SortOrder order(SortOperation sortOperation) {
        return Stream.of(SortOrder.values())
                .filter(sort -> sortOperation.getOrder().equals(sort.jsonValue()))
                .findAny()
                .orElse(SortOrder.Asc);
    }

    private Query searchQuery(SearchQueryPayload searchQueryPayload) {
        List<ValueField<?>> valueFields = searchQueryPayload.getValueFields();
        List<Query> valueQueries = Optional.ofNullable(valueFields)
                .stream()
                .flatMap(Collection::stream)
                .map(valueFieldConverterService::convert)
                .collect(Collectors.toList());

        List<RangeField<?>> rangeFields = searchQueryPayload.getRangeFields();
        List<Query> rangeQueries = Optional.ofNullable(rangeFields)
                .stream()
                .flatMap(Collection::stream)
                .map(rangeFieldConverterService::convert)
                .collect(Collectors.toList());

        if (valueQueries.isEmpty() && rangeQueries.isEmpty()) {
            return null;
        }
        BoolQuery.Builder builder = new BoolQuery.Builder();
        builder.must(valueQueries);
        builder.must(rangeQueries);
        return new Query.Builder()
                .bool(builder.build())
                .build();
    }

    private static List<FieldAndFormat> fields(List<String> fields) {
        if (fields == null) {
            return null;
        }
        return fields.stream()
                .map(field -> new FieldAndFormat.Builder()
                        .field(field)
                        .build())
                .collect(Collectors.toList());
    }

    public <T> SearchRequest createAggregationRequest(FacetsPayload facetsPayload, Class<T> documentType) {
        Query query = searchQuery(facetsPayload);
        SearchRequest.Builder builder = new SearchRequest.Builder()
                .source(new SourceConfig.Builder()
                        .fetch(false)
                        .build())
                .index(indexInfo.getAlias(documentType))
                .query(query)
                .aggregations(aggregations(facetsPayload));
        return builder.build();
    }

    private Map<String, Aggregation> aggregations(FacetsPayload facetsPayload) {
        Map<String, Aggregation> aggregations = new HashMap<>();
        List<FacetsPayload.ValueFacet> facets = Optional.ofNullable(facetsPayload.getFacets())
                .orElseGet(List::of);
        for (FacetsPayload.ValueFacet facet : facets) {
            String name = facet.getPath() == null ? facet.getField() : facet.getPath() + "." + facet.getField();
            aggregations.put(name, facetConverterService.convert(facet));
        }

        List<RangeFacet<?>> rangeFacets = Optional.ofNullable(facetsPayload.getRangeFacets())
                .orElseGet(List::of);
        for (RangeFacet<?> facet : rangeFacets) {
            String name = facet.getPath() == null ? facet.getField() : facet.getPath() + "." + facet.getField();
            aggregations.put("range" + "." + name, facetConverterService.convert(facet));
        }
        return aggregations;
    }
}
