package com.example.es.service.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import com.example.es.entity.request.facet.FacetsPayload;
import com.example.es.entity.request.search.SearchPayload;
import com.example.es.entity.search.SearchResult;
import com.example.es.entity.search.facet.FieldFacets;
import com.example.es.service.factory.QueryFactory;
import com.example.es.service.parser.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EsSearch implements Es.Search {
    private final ElasticsearchClient elasticsearchClient;
    private final QueryFactory queryFactory;
    private final Parser.Service parserService;
    private final Parser.Aggregation aggregationParser;

    @Override
    public <T> SearchResult<T> search(SearchPayload searchPayload, Class<T> documentType) throws IOException {
        SearchRequest searchRequest = queryFactory.createSearchRequest(searchPayload, documentType);
        SearchResponse<T> response = elasticsearchClient.search(searchRequest, documentType);
        return convertResponseToSearchResult(response, documentType);
    }

    private <T> SearchResult<T> convertResponseToSearchResult(SearchResponse<T> response, Class<T> documentType) {
        HitsMetadata<T> hits = response.hits();
        if (hits == null) {
            return SearchResult.<T>builder().build();
        }
        List<T> documents = Optional.ofNullable(hits.hits())
                .stream()
                .flatMap(Collection::stream)
                .map(hit -> parse(hit, documentType))
                .collect(Collectors.toList());

        long totalCount = Optional.ofNullable(hits.total())
                .map(TotalHits::value)
                .orElse(0L);

        return SearchResult.<T>builder()
                .documents(documents)
                .totalCount(totalCount)
                .build();
    }

    private <T> T parse(Hit<T> hit, Class<T> documentType) {
        T source = hit.source();
        if (source == null) {
            return parserService.parse(hit.fields(), documentType);
        } else {
            return source;
        }
    }

    @Override
    public <T> List<FieldFacets> facets(FacetsPayload facetsPayload, Class<T> documentType) throws IOException {
        SearchRequest aggregationRequest = queryFactory.createAggregationRequest(facetsPayload, documentType);
        SearchResponse<T> search = elasticsearchClient.search(aggregationRequest, documentType);
        return aggregationParser.parse(search.aggregations());
    }

}
