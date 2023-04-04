package com.example.es.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UsersResponse {
    List<User> users;
    long totalCount;

    @Value
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class User {
        Long id;
        String nameEn;
        String nameAr;
        String gender;
        LocalDate dateOfBirth;
        Integer favouriteNumber;
        String placeOfBirthEn;
        String placeOfBirthAr;
    }
}
