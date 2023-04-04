package com.example.es.service.generator;

import com.example.es.entity.db.Gender;
import com.example.es.entity.db.User;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RandomGenerator implements Generator.Random {

    public static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    public static final int COUNTRIES_COUNT = 250;
    public static final int MAX_FAVOURITE_NUMBER = 101;
    private final Faker enFaker;
    private final Faker arFaker;

    @Autowired
    public RandomGenerator() {
        this.enFaker = new Faker();
        this.arFaker = new Faker(new Locale("ar"));
    }

    @Override
    public List<User> generateUsers(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> createUser())
                .collect(Collectors.toList());
    }

    private User createUser() {
        return User.builder()
                .nameEn(enFaker.name().fullName())
                .nameAr(arFaker.name().nameWithMiddle())
                .gender(Gender.getRandom().getValue())
                .placeOfBirth(RANDOM.nextInt(COUNTRIES_COUNT) + 1)
                .dateOfBirth(LocalDate.ofInstant(enFaker.date().birthday().toInstant(), ZoneId.systemDefault()))
                .favouriteNumber(RANDOM.nextInt(0, MAX_FAVOURITE_NUMBER))
                .build();
    }
}
