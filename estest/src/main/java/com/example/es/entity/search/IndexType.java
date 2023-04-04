package com.example.es.entity.search;

import com.example.es.entity.search.document.UserEntity;
import com.example.es.entity.source.SourceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum IndexType {
    USERS(UserEntity.class, SourceType.USER);

    private final Class<?> type;
    private final SourceType sourceType;
}
