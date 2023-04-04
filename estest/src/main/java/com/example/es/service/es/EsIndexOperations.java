package com.example.es.service.es;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.es.service.es.Es.IndexInfo;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.PutAliasRequest;

@Service
public class EsIndexOperations implements Es.IndexOperations {
    private final ElasticsearchIndicesClient elasticsearchIndexClient;
    private final Es.IndexInfo indexInfo;

    public EsIndexOperations(ElasticsearchClient elasticsearchlient, IndexInfo indexInfo) {
        this.elasticsearchIndexClient = elasticsearchlient.indices();
        this.indexInfo = indexInfo;
    }

    @Override
    public <T> String createIndexWithAlias(Class<T> documentType) throws IOException {
        String indexName = executeCreateIndexRequest(documentType);
        executePutAliasReques(documentType, indexName);
        elasticsearchIndexClient.flush();
        return indexName;
    }

    private <T> CreateIndexRequest createIndexRequest(Class<T> documentType, String indexName) {
        return new CreateIndexRequest.Builder()
                .index(indexName)
                .mappings(indexInfo.getMapping(documentType))
                .settings(indexInfo.getSettings(documentType))
                .build();
    }

    private <T> PutAliasRequest putAliasRequest(Class<T> documentType, String indexName) {
        return new PutAliasRequest.Builder()
                .index(indexName)
                .name(indexInfo.getAlias(documentType))
                .build();
    }

    @Override
    public <T> String createIndex(Class<T> documentType) throws IOException {
        String indexName = executeCreateIndexRequest(documentType);
        elasticsearchIndexClient.flush();
        return indexName;
    }

    private <T> String executeCreateIndexRequest(Class<T> documentType) throws IOException {
        String indexName = indexInfo.getAlias(documentType) + "-" + System.currentTimeMillis();
        CreateIndexRequest createIndexRequest = createIndexRequest(documentType, indexName);
        elasticsearchIndexClient.create(createIndexRequest);
        return indexName;
    }

    @Override
    public <T> void putAliasOnIndex(Class<T> documentType, String indexName) throws IOException {
        executePutAliasReques(documentType, indexName);
        elasticsearchIndexClient.flush();
    }

    private <T> void executePutAliasReques(Class<T> documentType, String indexName) throws IOException {
        PutAliasRequest putAliasRequest = putAliasRequest(documentType, indexName);
        elasticsearchIndexClient.putAlias(putAliasRequest);
    }

    @Override
    public void removeIndexes(List<String> indexNames) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder()
                .index(indexNames)
                .build();
        elasticsearchIndexClient.delete(deleteIndexRequest);
        elasticsearchIndexClient.flush();
    }
}
