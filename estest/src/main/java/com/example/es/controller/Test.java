package com.example.es.controller;


import com.example.es.entity.request.ValueField;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("test")
public class Test {

    @PostMapping
    public ResponseEntity<Void> test(@RequestBody Payload payload) {
        System.out.println(payload);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class Payload {
        List<ValueField<?>> valueFields;
    }
}
