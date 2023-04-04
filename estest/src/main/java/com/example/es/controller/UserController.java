package com.example.es.controller;

import com.example.es.dao.Dao;
import com.example.es.entity.request.IdsPayload;
import com.example.es.entity.request.UpdatePayload;
import com.example.es.entity.request.search.SearchPayload;
import com.example.es.entity.response.UsersResponse;
import com.example.es.entity.search.IndexType;
import com.example.es.entity.search.document.UserEntity;
import com.example.es.entity.search.update.UpdateOperation;
import com.example.es.service.es.Es;
import com.example.es.service.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.StopWatch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("users")
public class UserController {
    private final User.Service.Search searchService;
    private final Es.IdOperations idsOperations;
    private final Es.Wildcard esW;
    private final Dao.Wildcard esD;

    @PostMapping("search")
    public ResponseEntity<UsersResponse> search(@RequestBody SearchPayload searchPayload) {
        log.info("Payload: {}", searchPayload);
        return ResponseEntity.ok(timer(() -> searchService.search(searchPayload), "Normal"));
    }

    @GetMapping("search/db/{nameEn}")
    public ResponseEntity<List<UsersResponse.User>> searchDb(@PathVariable("nameEn") String nameEn) {
        return ResponseEntity.ok(timer(() -> esD.getUsersByNameEn(nameEn), "wildcard db"));
    }

    @GetMapping("search/es/{nameEn}")
    public ResponseEntity<List<UserEntity>> searchEs(@PathVariable("nameEn") String nameEn) {
        return ResponseEntity.ok(timer(() -> esW.getUsersByNameEn(nameEn), "wildcard es"));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody IdsPayload idsPayload) throws IOException {
        idsOperations.create(idsPayload.getIds(), IndexType.USERS);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody IdsPayload idsPayload) throws IOException {
        idsOperations.delete(idsPayload.getIds(), IndexType.USERS);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping
    public ResponseEntity<Void> update(@RequestBody UpdatePayload updatePayload) throws IOException {
        List<UpdateOperation> updateOperations = updatePayload.getUpdates()
                .stream()
                .map(upd -> UpdateOperation.builder()
                        .id(upd.getId())
                        .updateValue(Map.of(upd.getUpdateField().getField(), upd.getUpdateField().getValue()))
                        .build())
                .collect(Collectors.toList());
        idsOperations.update(updateOperations, IndexType.USERS);
        return ResponseEntity.accepted().build();
    }

    private static <T> T timer(Supplier<T> f, String type) {
        log.info("-----------------------------------------------------------");
        log.info("Start search: {}", type);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        T res = f.get();
        stopWatch.stop();
        log.info("Finish search: {}. Result: {}", type, stopWatch.totalTime().getMillis());
        log.info("-----------------------------------------------------------");
        return res;
    }
}
