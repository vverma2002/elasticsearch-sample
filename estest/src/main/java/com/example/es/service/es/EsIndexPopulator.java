package com.example.es.service.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import com.example.es.entity.search.IndexType;
import com.example.es.entity.search.document.Entity;
import com.example.es.entity.source.RowLimit;
import com.example.es.service.populator.Populator;
import com.example.es.service.provider.Provider;
import com.example.es.service.source.Source;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.StopWatch;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EsIndexPopulator<T extends Entity> implements Es.IndexPopulator {
    private final static int BATCH_SIZE = 10000;

    private final Provider.Source<T> sourceProvider;
    private final Provider.Populator<T> populatorProvider;
    private final ElasticsearchClient elasticsearchClient;
    private final ExecutorService taskExecutor;

    @Override
    public void populate(IndexType indexType, String indexName) {
        Source.DbSource<T> dbSource = sourceProvider.getDbSource(indexType.getSourceType());
        List<Populator<T>> populators = populatorProvider.getPopulators(indexType);
        int count = dbSource.count();
        List<RowLimit> rowLimits = populationPartitions(count);
        List<Runnable> populateExecutions = rowLimits.stream()
                .map(rowLimit -> (Runnable) () -> {
                    List<T> rows = dbSource.getRows(rowLimit);
                    List<Long> ids = rows.stream()
                            .map(Entity::getId)
                            .collect(Collectors.toList());
                    populators.forEach(populator -> populator.populate(ids, rows));
                    populateIndex(rows, indexName);
                })
                .collect(Collectors.toList());
        execute(populateExecutions);
    }

    private static List<RowLimit> populationPartitions(int count) {
        List<RowLimit> partitions = new ArrayList<>();
        int numberOfPartitions = count / BATCH_SIZE;
        for (int i = 0; i < numberOfPartitions; i++) {
            partitions.add(RowLimit.builder()
                    .from(i * BATCH_SIZE)
                    .to((i + 1) * BATCH_SIZE)
                    .build());
        }
        int restCount = count - BATCH_SIZE * numberOfPartitions;
        if (restCount > 0) {
            partitions.add(RowLimit.builder()
                    .from(BATCH_SIZE * numberOfPartitions)
                    .to(count)
                    .build());
        }
        return partitions;
    }

    private void populateIndex(List<T> documents, String indexName) {
        List<BulkOperation> indexRequests = documents.stream()
                .map(doc -> new BulkOperation.Builder()
                        .index(new IndexOperation.Builder<T>()
                                .document(doc)
                                .id(String.valueOf(doc.getId()))
                                .build())
                        .build())
                .collect(Collectors.toList());
        BulkRequest bulkRequest = new BulkRequest.Builder()
                .operations(indexRequests)
                .index(indexName)
                .build();
        try {
            elasticsearchClient.bulk(bulkRequest);
        } catch (IOException e) {
            throw new RuntimeException("Filling index error", e);
        }
    }

    private void execute(List<Runnable> commands) {
        log.info("---------------------Index population start------------------------");
        StopWatch populationTime = new StopWatch().start();
        AtomicInteger counter = new AtomicInteger();
        List<Future<?>> futures = commands.stream()
                .map(c -> taskExecutor.submit(() -> {
                    int id = counter.incrementAndGet();
                    log.info("Population portion - {} start", id);
                    StopWatch populationPortionTime = new StopWatch().start();
                    c.run();
                    log.info("Population portion - {} finish: {}", id,
                            populationPortionTime.stop().totalTime().getMillis());
                }))
                .collect(Collectors.toList());
        try {
            for (Future<?> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            for (Future<?> future : futures) {
                future.cancel(true);
            }
            throw new RuntimeException("Population failed", e);
        }
        log.info("------------------Index population finished: {} ------------------------------",
                populationTime.stop().totalTime().getMillis());
    }
}
