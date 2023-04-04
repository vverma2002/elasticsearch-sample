package com.example.es.service.resource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ResourceService implements Resource.Service {
    @Override
    public InputStream readResourceFile(String filePath) {
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        try {
            return classPathResource.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
