package com.example.es.entity.request.search;

import com.example.es.entity.request.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPayload implements SearchQueryPayload {
    private List<ValueField<?>> valueFields;
    private List<RangeField<?>> rangeFields;
    private List<Sort> sorts;
    private List<String> fields;
    @Builder.Default
    private Paging page = Paging.builder()
            .num(1)
            .size(10)
            .build();
}
