package com.example.es.service.user.populator;

import com.example.es.dao.Dao;
import com.example.es.entity.db.Country;
import com.example.es.entity.search.IndexType;
import com.example.es.entity.search.document.LocaleEntity;
import com.example.es.entity.search.document.UserEntity;
import com.example.es.service.populator.Populator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CountryPopulator implements Populator<UserEntity> {
    private final Dao.Country countryDao;

    @Override
    public void populate(List<Long> ids, List<UserEntity> rows) {
        Map<Long, Country> countriesForUsers = countryDao.getCountriesForUsers(ids);
        rows.forEach(row -> {
            Country country = countriesForUsers.get(row.getId());
            LocaleEntity placeOfBirth = LocaleEntity.builder()
                    .en(country.getNameEn())
                    .ar(country.getNameAr())
                    .build();
            row.setPlaceOfBirth(placeOfBirth);
        });
    }

    @Override
    public boolean supports(IndexType indexType) {
        return IndexType.USERS == indexType;
    }
}
