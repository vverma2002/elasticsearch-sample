package com.example.es.service.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.example.es.entity.search.document.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EsWildcard implements Es.Wildcard {
    private final ElasticsearchClient elasticsearchClient;

    @Override
    @SneakyThrows
    public List<UserEntity> getUsersByNameEn(String nameEn) {
        Query query = new Query(
                new WildcardQuery.Builder()
                        .caseInsensitive(true)
                        .field("nameEnCopy")
                        .wildcard(nameEn + "*")
                        .build()
        );
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("users-v1")
                .query(query)
                .build();
        SearchResponse<UserEntity> search = elasticsearchClient.search(searchRequest, UserEntity.class);
        HitsMetadata<UserEntity> hits = search.hits();
        List<Hit<UserEntity>> res = hits.hits();
        return res.stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
