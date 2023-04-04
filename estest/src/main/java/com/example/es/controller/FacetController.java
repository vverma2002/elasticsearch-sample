package com.example.es.controller;

import com.example.es.entity.request.facet.FacetsPayload;
import com.example.es.entity.response.FacetsResponse;
import com.example.es.entity.search.IndexType;
import com.example.es.service.facet.Facet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("facets")
public class FacetController {
    private final Facet.Service.Search facetService;

    @PostMapping("{entity}")
    public ResponseEntity<FacetsResponse> facets(@RequestBody FacetsPayload facetsPayload, @PathVariable("entity") String entity) throws IOException {
        log.info("Payload: {}", facetsPayload);
        IndexType indexType = IndexType.valueOf(entity);
        return ResponseEntity.ok(facetService.facets(facetsPayload, indexType.getType()));
    }
}
