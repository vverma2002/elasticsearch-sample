package com.example.es.dao;

import com.example.es.entity.db.User;
import com.example.es.entity.response.UsersResponse;
import com.example.es.entity.source.RowLimit;

import java.util.List;
import java.util.Map;

public interface Dao {
    interface Country {
        Map<Long, com.example.es.entity.db.Country> getCountriesForUsers(List<Long> userIds);
    }

    interface UserInserter {
        void insert(User user);

        void insertAll(List<User> users);
    }

    interface Wildcard {
        List<UsersResponse.User> getUsersByNameEn(String nameEn);
    }

    interface UserReader {
        List<User> getUsers(RowLimit rowLimit);

        List<User> getUsers(List<Long> ids);

        int getUserCount();
    }
}
