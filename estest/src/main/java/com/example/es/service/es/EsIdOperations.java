package com.example.es.service.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.DeleteOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.core.bulk.UpdateAction;
import com.example.es.entity.search.IndexType;
import com.example.es.entity.search.document.Entity;
import com.example.es.entity.search.update.UpdateOperation;
import com.example.es.service.populator.Populator;
import com.example.es.service.provider.Provider;
import com.example.es.service.source.Source;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EsIdOperations<T extends Entity> implements Es.IdOperations {
    private final ElasticsearchClient elasticsearchClient;

    private final Provider.Source<T> sourceProvider;
    private final Provider.Populator<T> populatorProvider;
    private final EsIndexInfo indexInfo;

    @Override
    public void delete(List<Long> ids, IndexType indexType) throws IOException {
        List<BulkOperation> deleteOperations = ids.stream()
                .map(id -> new BulkOperation.Builder()
                        .delete(new DeleteOperation.Builder()
                                .id(String.valueOf(id))
                                .build())
                        .build())
                .collect(Collectors.toList());
        BulkRequest bulkRequest = new BulkRequest.Builder()
                .index(indexInfo.getAlias(indexType.getType()))
                .operations(deleteOperations)
                .build();
        elasticsearchClient.bulk(bulkRequest);
    }

    @Override
    public void create(List<Long> ids, IndexType indexType) throws IOException {
        Source.DbSource<T> dbSource = sourceProvider.getDbSource(indexType.getSourceType());
        List<Populator<T>> populators = populatorProvider.getPopulators(indexType);
        List<T> rows = dbSource.getRows(ids);
        populators.forEach(populator -> populator.populate(ids, rows));
        List<BulkOperation> createRequest = rows.stream()
                .map(row -> new BulkOperation.Builder()
                        .index(new IndexOperation.Builder<T>()
                                .document(row)
                                .id(String.valueOf(row.getId()))
                                .build())
                        .build())
                .collect(Collectors.toList());
        BulkRequest bulkRequest = new BulkRequest.Builder()
                .operations(createRequest)
                .index(indexInfo.getAlias(indexType.getType()))
                .build();
        elasticsearchClient.bulk(bulkRequest);
    }

    @Override
    public void update(List<UpdateOperation> updateOperations, IndexType indexType) throws IOException {
        List<BulkOperation> updates = updateOperations.stream()
                .map(operation -> new BulkOperation.Builder()
                        .update(new co.elastic.clients.elasticsearch.core.bulk.UpdateOperation.Builder<>()
                                .id(String.valueOf(operation.getId()))
                                .action(new UpdateAction.Builder<>()
                                        .doc(operation.getUpdateValue())
                                        .build())
                                .build())
                        .build())
                .collect(Collectors.toList());
        BulkRequest bulkRequest = new BulkRequest.Builder()
                .index(indexInfo.getAlias(indexType.getType()))
                .operations(updates)
                .build();
        elasticsearchClient.bulk(bulkRequest);
    }
}
