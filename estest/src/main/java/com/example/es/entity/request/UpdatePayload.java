package com.example.es.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdatePayload {
    private List<Update> updates;

    @Data
    public static class Update {
        private Long id;
        private UpdateField updateField;
    }

    @Data
    public static class UpdateField {
        private String field;
        private Object value;
    }
}
