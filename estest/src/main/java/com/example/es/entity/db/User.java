package com.example.es.entity.db;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    long id;
    String nameEn;
    String nameAr;
    String gender;
    LocalDate dateOfBirth;
    int favouriteNumber;
    int placeOfBirth;
}
