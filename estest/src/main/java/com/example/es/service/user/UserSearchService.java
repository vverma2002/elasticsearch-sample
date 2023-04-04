package com.example.es.service.user;

import com.example.es.entity.request.search.SearchPayload;
import com.example.es.entity.response.UsersResponse;
import com.example.es.entity.search.SearchResult;
import com.example.es.entity.search.document.UserEntity;
import com.example.es.service.es.Es;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserSearchService implements User.Service.Search {
    private final Es.Search esSearch;
    private final Converter<SearchResult<UserEntity>, UsersResponse> searchResultConverter;

    @Override
    public UsersResponse search(SearchPayload payload) {
        SearchResult<UserEntity> userEntities = searchUsers(payload);
        return searchResultConverter.convert(userEntities);
    }

    private SearchResult<UserEntity> searchUsers(SearchPayload payload) {
        try {
            return esSearch.search(payload, UserEntity.class);
        } catch (IOException e) {
            throw new RuntimeException("Users search error", e);
        }
    }
}
