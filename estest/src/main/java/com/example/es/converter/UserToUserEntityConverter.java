package com.example.es.converter;

import com.example.es.entity.db.User;
import com.example.es.entity.search.document.UserEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class UserToUserEntityConverter implements Converter<User, UserEntity> {
    @Override
    public UserEntity convert(User user) {
        if (user == null) {
            return null;
        }
        return UserEntity
                .builder()
                .id(user.getId())
                .gender(user.getGender())
                .favouriteNumber(user.getFavouriteNumber())
                .dateOfBirth(user.getDateOfBirth())
                .nameAr(user.getNameAr())
                .nameEn(user.getNameEn())
                .nameEnCopy(user.getNameEn())
                .build();
    }
}
