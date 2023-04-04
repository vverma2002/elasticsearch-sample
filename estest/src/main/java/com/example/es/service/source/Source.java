package com.example.es.service.source;

import com.example.es.entity.search.document.Entity;
import com.example.es.entity.source.RowLimit;
import com.example.es.entity.source.SourceType;

import java.util.List;

public interface Source {

    interface DbSource<T extends Entity> {
        List<T> getRows(RowLimit rowLimit);

        List<T> getRows(List<Long> ids);

        int count();

        SourceType getSource();
    }
}
