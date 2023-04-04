package com.example.es.dao;

import com.example.es.entity.db.User;
import com.example.es.entity.source.RowLimit;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserReaderDao implements Dao.UserReader {
    private static final String SELECT_USERS
            = "SELECT u.id, "
            + "u.name_en, "
            + "u.name_ar, "
            + "u.date_of_birth, "
            + "u.gender, "
            + "u.favourite_number, "
            + "row_number() over(order by u.id) as rnum "
            + "FROM users u";

    private static final String SELECT_USERS_LIMITED_ROW_NUMS
            = "SELECT * "
            + "FROM (" + SELECT_USERS + ") users "
            + "WHERE users.rnum BETWEEN :from AND :to";

    private static final String SELECT_USERS_BY_IDS = SELECT_USERS
            + " WHERE u.id in (:ids)";

    private static final String SELECT_USERS_COUNT = "SELECT count(id) from users";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<User> getUsers(RowLimit rowLimit) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("from", rowLimit.getFrom() + 1)
                .addValue("to", rowLimit.getTo());
        return jdbcTemplate.query(SELECT_USERS_LIMITED_ROW_NUMS, parameters, userMapper());

    }

    private RowMapper<User> userMapper() {
        return (rs, rowNum) -> User.builder()
                .id(rs.getInt("id"))
                .nameEn(rs.getString("name_en"))
                .nameAr(rs.getString("name_ar"))
                .dateOfBirth(rs.getDate("date_of_birth").toLocalDate())
                .gender(rs.getString("gender"))
                .favouriteNumber(rs.getInt("favourite_number"))
                .build();
    }

    @Override
    public List<User> getUsers(List<Long> ids) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ids", ids);
        return jdbcTemplate.query(SELECT_USERS_BY_IDS, parameters, userMapper());
    }

    @Override
    public int getUserCount() {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_USERS_COUNT, Map.of(), Integer.class))
                .orElse(0);
    }
}
