package com.example.es.service.facet;

import com.example.es.entity.request.facet.FacetsPayload;
import com.example.es.entity.response.FacetsResponse;

import java.io.IOException;

public interface Facet {
    interface Service {
        interface Search {
            <T> FacetsResponse facets(FacetsPayload payload, Class<T> documentType) throws IOException;
        }
    }
}
