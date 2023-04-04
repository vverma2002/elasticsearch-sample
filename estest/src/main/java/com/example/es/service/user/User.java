package com.example.es.service.user;

import com.example.es.entity.request.search.SearchPayload;
import com.example.es.entity.response.UsersResponse;

public interface User {

    interface Service {
        interface Filler {
            void saveRandomUsers(int count);
        }

        interface Search {
            UsersResponse search(SearchPayload payload);
        }
    }
}
