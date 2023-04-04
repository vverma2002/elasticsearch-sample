package com.example.es.entity.db;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Gender {
    MALE("male"),
    FEMALE("female");

    private final String value;

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static Gender getRandom() {
        if (RANDOM.nextBoolean()) {
            return MALE;
        } else {
            return FEMALE;
        }
    }
}
