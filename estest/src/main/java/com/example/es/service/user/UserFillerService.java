package com.example.es.service.user;

import com.example.es.dao.Dao;
import com.example.es.service.generator.Generator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.StopWatch;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserFillerService implements User.Service.Filler {
    private static final int MAX_BATCH_SIZE = 5_000;
    private final Dao.UserInserter userInserter;
    private final Generator.Random random;

    @Override
    public void saveRandomUsers(int count) {
        int leftCount = count;
        while (leftCount > 0) {
            int batchSize = Math.min(leftCount, MAX_BATCH_SIZE);
            generateUsersAndSave(batchSize);
            leftCount = leftCount - batchSize;
        }
    }

    private void generateUsersAndSave(int batchSize) {
        List<com.example.es.entity.db.User> users = random.generateUsers(batchSize);
        timer(() -> userInserter.insertAll(users));
    }

    private static void timer(Runnable f) {
        log.info("INSERT");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        f.run();
        stopWatch.stop();
        log.info("Finish INSERT: TIME: {}", stopWatch.totalTime().getMillis());
    }
}
