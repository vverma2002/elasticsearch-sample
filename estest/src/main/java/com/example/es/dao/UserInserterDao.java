package com.example.es.dao;

import com.example.es.entity.db.User;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@AllArgsConstructor
public class UserInserterDao implements Dao.UserInserter {
        private static final String INSERT_USER = "INSERT INTO USERS(NAME_EN, NAME_AR, DATE_OF_BIRTH, GENDER, FAVOURITE_NUMBER, PLACE_OF_BIRTH)"
                        + " VALUES(:nameEn, :nameAr, :dop, :gender, :fn, :pob)";
        private final NamedParameterJdbcTemplate jdbcTemplate;

        @Override
        public void insert(User user) {
                SqlParameterSource parameters = new MapSqlParameterSource()
                                .addValue("nameEn", user.getNameEn())
                                .addValue("nameAr", user.getNameAr())
                                .addValue("dop", user.getDateOfBirth())
                                .addValue("gender", user.getGender())
                                .addValue("fn", user.getFavouriteNumber())
                                .addValue("pob", user.getPlaceOfBirth());
                jdbcTemplate.update(INSERT_USER, parameters);
        }

        @Override
        @Transactional
        public void insertAll(List<User> users) {
                SqlParameterSource[] parameters = users.stream()
                                .map(user -> new MapSqlParameterSource()
                                                .addValue("nameEn", user.getNameEn())
                                                .addValue("nameEnCopy", user.getNameEn())
                                                .addValue("nameAr", user.getNameAr())
                                                .addValue("dop", user.getDateOfBirth())
                                                .addValue("gender", user.getGender())
                                                .addValue("fn", user.getFavouriteNumber())
                                                .addValue("pob", user.getPlaceOfBirth()))
                                .toArray(SqlParameterSource[]::new);
                jdbcTemplate.batchUpdate(INSERT_USER, parameters);
        }
}
