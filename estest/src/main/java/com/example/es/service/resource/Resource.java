package com.example.es.service.resource;

import java.io.InputStream;

public interface Resource {
    interface Service {
        InputStream readResourceFile(String filePath);
    }
}
