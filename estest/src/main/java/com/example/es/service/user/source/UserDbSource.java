package com.example.es.service.user.source;

import com.example.es.dao.Dao;
import com.example.es.entity.db.User;
import com.example.es.entity.search.document.UserEntity;
import com.example.es.entity.source.RowLimit;
import com.example.es.entity.source.SourceType;
import com.example.es.service.source.Source;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDbSource implements Source.DbSource<UserEntity> {
    private final Dao.UserReader userReader;
    private final Converter<User, UserEntity> converter;

    @Override
    public List<UserEntity> getRows(RowLimit rowLimit) {
        return userReader.getUsers(rowLimit)
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserEntity> getRows(List<Long> ids) {
        return userReader.getUsers(ids)
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public int count() {
        return userReader.getUserCount();
    }

    @Override
    public SourceType getSource() {
        return SourceType.USER;
    }
}
