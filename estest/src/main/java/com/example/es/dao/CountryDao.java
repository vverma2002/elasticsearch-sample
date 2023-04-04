package com.example.es.dao;

import com.example.es.entity.db.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CountryDao implements Dao.Country {
    private static final String SELECT_COUNTRIES_FOR_USERS
            = "SELECT u.id, c.name_en, c.name_ar "
            + "FROM users u "
            + "INNER JOIN countries c ON u.place_of_birth = c.id "
            + "WHERE u.id = ANY(ARRAY[ :userIds ])";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Map<Long, Country> getCountriesForUsers(List<Long> userIds) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userIds", userIds);
        Map<Long, Country> result = new HashMap<>();
        jdbcTemplate.query(SELECT_COUNTRIES_FOR_USERS, parameters, rs -> {
            Country country = Country.builder()
                    .nameEn(rs.getString("name_en"))
                    .nameAr(rs.getString("name_ar"))
                    .build();
            result.put(rs.getLong("id"), country);
        });
        return result;
    }
}
