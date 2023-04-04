package com.example.es.service.populator;

import com.example.es.entity.search.IndexType;
import com.example.es.entity.search.document.Entity;

import java.util.List;

public interface Populator<T extends Entity> {
    void populate(List<Long> ids, List<T> rows);

    boolean supports(IndexType indexType);
}
