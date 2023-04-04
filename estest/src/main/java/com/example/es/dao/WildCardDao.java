package com.example.es.dao;

import com.example.es.entity.response.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WildCardDao implements Dao.Wildcard {
    private static final String WC_SELECT = "SELECT u.id, "
            + "u.name_en, "
            + "u.name_ar, "
            + "u.date_of_birth, "
            + "u.gender, "
            + "u.favourite_number, "
            + "c.name_en as c_en, "
            + "c.name_ar as c_ar "
            + "FROM users u "
            + "INNER JOIN countries c ON u.place_of_birth = c.id "
            + "WHERE lower(u.name_en) LIKE :nameEn";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<UsersResponse.User> getUsersByNameEn(String nameEn) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("nameEn", "%" + nameEn.toLowerCase() + "%");
        return jdbcTemplate.query(WC_SELECT, parameters, (rs, rowNum) -> UsersResponse.User.builder()
                .id(rs.getLong("id"))
                .nameEn(rs.getString("name_en"))
                .nameAr(rs.getString("name_ar"))
                .dateOfBirth(rs.getDate("date_of_birth").toLocalDate())
                .gender(rs.getString("gender"))
                .favouriteNumber(rs.getInt("favourite_number"))
                .placeOfBirthAr(rs.getString("c_ar"))
                .placeOfBirthEn(rs.getString("c_en"))
                .build());
    }
}
