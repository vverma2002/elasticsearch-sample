package com.example.es.service.es;

import com.example.es.entity.search.IndexType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class EsIndexer implements Es.Indexer {
    private final Es.IndexOperations indexOperations;
    private final Es.IndexPopulator indexPopulator;
    private final Es.IndexInfo indexInfo;

    @Override
    public void index(IndexType indexType) {
        Class<?> documentType = indexType.getType();
        String indexName = null;
        try {
            indexName = indexOperations.createIndexWithAlias(documentType);
            indexPopulator.populate(indexType, indexName);
        } catch (Exception exception) {
            removeIndexOnFail(indexName);
            throw new RuntimeException("Indexing failed", exception);
        }
    }

    private void removeIndexOnFail(String indexName) {
        if (indexName == null) {
            return;
        }
        try {
            indexOperations.removeIndexes(List.of(indexName));
        } catch (IOException e) {
            throw new RuntimeException("Unable to remove index");
        }
    }

    @Override
    public void reindex(IndexType indexType) {
        Class<?> documentType = indexType.getType();
        List<String> oldIndexes = indexInfo.getIndexes(documentType);
        String indexName = null;
        try {
            indexName = indexOperations.createIndex(documentType);
            indexPopulator.populate(indexType, indexName);
        } catch (Exception exception) {
            removeIndexOnFail(indexName);
            throw new RuntimeException("Indexing failed", exception);
        }
        removeOldIndexesAndAssignAliasToNew(documentType, oldIndexes, indexName);
    }

    private void removeOldIndexesAndAssignAliasToNew(Class<?> documentType, List<String> oldIndexes, String newIndex) {
        try {
            indexOperations.putAliasOnIndex(documentType, newIndex);
            indexOperations.removeIndexes(oldIndexes);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to assign alias to new index and remove old indexes", exception);
        }
    }
}
