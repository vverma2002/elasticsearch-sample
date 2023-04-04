package com.example.es.service.provider;

import com.example.es.entity.search.IndexType;
import com.example.es.entity.search.document.Entity;
import com.example.es.entity.source.SourceType;

import java.util.List;

public interface Provider {
    interface Source<T extends Entity> {
        com.example.es.service.source.Source.DbSource<T> getDbSource(SourceType sourceType);
    }

    interface Populator<T extends Entity> {
        List<com.example.es.service.populator.Populator<T>> getPopulators(IndexType indexType);
    }
}
