package com.example.es.entity.request;

import java.util.List;

public interface SearchQueryPayload {
    List<ValueField<?>> getValueFields();
    List<RangeField<?>> getRangeFields();
}
