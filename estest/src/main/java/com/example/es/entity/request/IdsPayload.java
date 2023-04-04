package com.example.es.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class IdsPayload {
    private List<Long> ids;
}
