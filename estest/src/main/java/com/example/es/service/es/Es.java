package com.example.es.service.es;

import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import com.example.es.entity.request.facet.FacetsPayload;
import com.example.es.entity.request.search.SearchPayload;
import com.example.es.entity.search.IndexType;
import com.example.es.entity.search.SearchResult;
import com.example.es.entity.search.document.UserEntity;
import com.example.es.entity.search.facet.FieldFacets;
import com.example.es.entity.search.update.UpdateOperation;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.util.List;

public interface Es {
    interface IndexOperations {
        <T> String createIndexWithAlias(Class<T> documentType) throws IOException;

        <T> String createIndex(Class<T> documentType) throws IOException;

        <T> void putAliasOnIndex(Class<T> documentType, String indexName) throws IOException;

        void removeIndexes(List<String> indexNames) throws IOException;
    }

    interface IdOperations {
        void delete(List<Long> ids, IndexType indexType) throws IOException;

        void create(List<Long> ids, IndexType indexType) throws IOException;

        void update(List<UpdateOperation> updateOperations, IndexType indexType) throws IOException;
    }

    interface IndexPopulator {
        void populate(IndexType indexType, String indexName);
    }

    interface IndexInfo {
        String getAlias(Class<?> documentType);

        List<String> getIndexes(Class<?> documentType);

        TypeMapping getMapping(Class<?> documentType);

        IndexSettings getSettings(Class<?> documentType);
    }

    interface Indexer {
        void index(IndexType indexType);

        void reindex(IndexType indexType);
    }

    interface IndexInfoConverter extends Converter<Class<?>, com.example.es.entity.search.IndexInfo> {

    }

    interface Search {
        <T> SearchResult<T> search(SearchPayload searchPayload, Class<T> documentType) throws IOException;

        <T> List<FieldFacets> facets(FacetsPayload facetsPayload, Class<T> documentType) throws IOException;
    }

    interface Wildcard {
        List<UserEntity> getUsersByNameEn(String nameEn);
    }
}
