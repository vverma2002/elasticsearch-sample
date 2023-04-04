package com.example.es.converter;

import com.example.es.entity.response.UsersResponse;
import com.example.es.entity.search.SearchResult;
import com.example.es.entity.search.document.LocaleEntity;
import com.example.es.entity.search.document.UserEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchResultToUsersResponseConverter implements Converter<SearchResult<UserEntity>, UsersResponse> {
    @Override
    public UsersResponse convert(SearchResult<UserEntity> searchResult) {
        long totalCount = searchResult.getTotalCount();
        List<UsersResponse.User> users = searchResult.getDocuments()
                .stream()
                .map(SearchResultToUsersResponseConverter::convertUser)
                .collect(Collectors.toList());
        return UsersResponse.builder()
                .totalCount(totalCount)
                .users(users)
                .build();
    }

    private static UsersResponse.User convertUser(UserEntity user) {
        LocaleEntity placeOfBirth = user.getPlaceOfBirth();
        UsersResponse.User.UserBuilder userBuilder = UsersResponse.User.builder()
                .id(user.getId())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .nameEn(user.getNameEn())
                .nameAr(user.getNameAr())
                .favouriteNumber(user.getFavouriteNumber());
        if (placeOfBirth != null) {
            userBuilder.placeOfBirthEn(placeOfBirth.getEn())
                    .placeOfBirthAr(placeOfBirth.getAr());
        }
        return userBuilder.build();
    }
}
