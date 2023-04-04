package com.example.es.service.facet;

import com.example.es.entity.request.facet.FacetsPayload;
import com.example.es.entity.response.FacetsResponse;
import com.example.es.service.es.Es;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacetSearchService implements Facet.Service.Search {
    private final Es.Search esSearch;

    @Override
    public <T> FacetsResponse facets(FacetsPayload payload, Class<T> documentType) throws IOException {
        List<FacetsResponse.FacetField> facets = esSearch.facets(payload, documentType)
                .stream()
                .map(facet -> FacetsResponse.FacetField.builder()
                        .facets(facet.getFacets()
                                .stream()
                                .map(f -> FacetsResponse.Facet.builder()
                                        .count(f.getCount())
                                        .value(f.getValue())
                                        .build())
                                .collect(Collectors.toList()))
                        .field(facet.getName())
                        .build())
                .collect(Collectors.toList());
        return FacetsResponse.builder()
                .facets(facets)
                .build();
    }
}
