package com.example.es.service.generator;

import com.example.es.entity.db.User;

import java.util.List;

public interface Generator {
    interface Random {
        List<User> generateUsers(int count);
    }
}
