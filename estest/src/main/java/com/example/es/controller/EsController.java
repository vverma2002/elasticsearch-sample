package com.example.es.controller;

import com.example.es.entity.search.IndexType;
import com.example.es.service.es.Es;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("es")
@AllArgsConstructor
public class EsController {
    private final Es.Indexer indexer;
    private final Es.IndexOperations indexOperations;

    @SneakyThrows
    @PostMapping("create/index/{entity}")
    public ResponseEntity<Void> createIndex(@PathVariable("entity") String entity) {
        IndexType indexType = IndexType.valueOf(entity);
        indexOperations.createIndexWithAlias(indexType.getType());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("index/{entity}")
    public ResponseEntity<Void> index(@PathVariable("entity") String entity) {
        IndexType indexType = IndexType.valueOf(entity);
        indexer.index(indexType);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("reindex/{entity}")
    public ResponseEntity<Void> reindex(@PathVariable("entity") String entity) {
        IndexType indexType = IndexType.valueOf(entity);
        indexer.reindex(indexType);
        return ResponseEntity.noContent().build();
    }
}
