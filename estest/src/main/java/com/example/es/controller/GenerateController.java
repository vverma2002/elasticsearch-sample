package com.example.es.controller;

import com.example.es.service.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.StopWatch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("generate")
public class GenerateController {
    private final User.Service.Filler filler;

    @PostMapping("/users/{count}")
    public ResponseEntity<Void> generateUsers(@PathVariable("count") int count) {
        new Thread(() -> timer(() -> filler.saveRandomUsers(count))).start();
        return ResponseEntity.noContent().build();
    }

    private static void timer(Runnable f) {
        log.info("-----------------------------------------------------------");
        log.info("Start users");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        f.run();
        stopWatch.stop();
        log.info("Finish users: TIME: {}", stopWatch.totalTime().getMillis());
        log.info("-----------------------------------------------------------");
    }
}
