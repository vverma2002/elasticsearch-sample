package com.example.es.entity.request.facet;

import com.example.es.entity.request.RangeField;
import com.example.es.entity.request.SearchQueryPayload;
import com.example.es.entity.request.ValueField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacetsPayload implements SearchQueryPayload {
    private List<ValueField<?>> valueFields;
    private List<RangeField<?>> rangeFields;
    private List<ValueFacet> facets;
    private List<RangeFacet<?>> rangeFacets;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValueFacet {
        private String field;
        private String path;
    }
}
