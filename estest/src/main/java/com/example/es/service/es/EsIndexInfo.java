package com.example.es.service.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.GetAliasRequest;
import co.elastic.clients.elasticsearch.indices.GetAliasResponse;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.indices.get_alias.IndexAliases;
import com.example.es.entity.search.IndexInfo;
import com.example.es.entity.search.IndexType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EsIndexInfo implements Es.IndexInfo {
    private final Map<Class<?>, IndexInfo> indexInfoByDocumentType;
    private final Es.IndexInfoConverter indexInfoConverter;
    private final ElasticsearchIndicesClient indicesClient;

    public EsIndexInfo(Es.IndexInfoConverter indexInfoConverter, ElasticsearchClient client) {
        this.indexInfoConverter = indexInfoConverter;
        this.indicesClient = client.indices();
        this.indexInfoByDocumentType = Stream.of(IndexType.values())
                .collect(Collectors.toMap(IndexType::getType, this::getIndexInfo));
    }

    private IndexInfo getIndexInfo(IndexType indexType) {
        return indexInfoConverter.convert(indexType.getType());
    }

    @Override
    public String getAlias(Class<?> documentType) {
        return getPropertyForIndex(documentType, IndexInfo::getAliasName);
    }

    @Override
    public List<String> getIndexes(Class<?> documentType) {
        Function<IndexInfo, List<String>> indexNameGetter = indexInfo -> {
            GetAliasRequest aliasRequest = getAliasRequest(indexInfo);
            GetAliasResponse aliasResponse = queryForAlias(aliasRequest);
            return getIndexNames(aliasResponse);
        };
        return getPropertyForIndex(documentType, indexNameGetter);
    }

    private static GetAliasRequest getAliasRequest(IndexInfo indexInfo) {
        return new GetAliasRequest.Builder()
                .name(indexInfo.getAliasName())
                .build();
    }

    private GetAliasResponse queryForAlias(GetAliasRequest request) {
        try {
            return indicesClient.getAlias(request);
        } catch (IOException e) {
            throw new RuntimeException("Unable to find alias: " + request.name(), e);
        }
    }

    private static List<String> getIndexNames(GetAliasResponse aliasResponse) {
        Map<String, IndexAliases> result = aliasResponse.result();
        if (result == null || result.isEmpty()) {
            throw new RuntimeException("No data for for alias");
        }
        return new ArrayList<>(result.keySet());
    }

    @Override
    public TypeMapping getMapping(Class<?> documentType) {
        return getPropertyForIndex(documentType, IndexInfo::getMapping);
    }

    @Override
    public IndexSettings getSettings(Class<?> documentType) {
        return getPropertyForIndex(documentType, IndexInfo::getSettings);
    }

    private <T> T getPropertyForIndex(Class<?> documentType, Function<IndexInfo, T> propertyGetter) {
        IndexInfo indexInfo = indexInfoByDocumentType.get(documentType);
        if (indexInfo == null) {
            throw new IllegalArgumentException("Unable to find index for type: " + documentType.getSimpleName());
        }
        return propertyGetter.apply(indexInfo);
    }
}
