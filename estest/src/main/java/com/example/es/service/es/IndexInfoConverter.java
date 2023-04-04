package com.example.es.service.es;

import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.json.JsonpDeserializer;
import co.elastic.clients.json.JsonpMapper;
import com.example.es.annotation.search.IndexVersion;
import com.example.es.entity.search.IndexInfo;
import com.example.es.service.resource.Resource;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonParser;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IndexInfoConverter implements Es.IndexInfoConverter {
    private final Resource.Service resourceService;
    private final JsonpMapper jsonpMapper;

    @Override
    public IndexInfo convert(Class<?> indexType) {
        return Optional.ofNullable(indexType)
                .map(IndexAnnotations::new)
                .map(this::convertToIndex)
                .orElseThrow(RuntimeException::new);
    }

    private IndexInfo convertToIndex(IndexAnnotations indexAnnotations) {
        Document document = indexAnnotations.document;
        String indexName = document.indexName();
        Mapping mapping = indexAnnotations.mapping;
        String mappingPath = mapping.mappingPath();
        TypeMapping typeMapping = fromFile(TypeMapping._DESERIALIZER, mappingPath);
        Setting setting = indexAnnotations.setting;
        String settingPath = setting.settingPath();
        IndexSettings settings = fromFile(IndexSettings._DESERIALIZER, settingPath);
        IndexVersion indexVersion = indexAnnotations.indexVersion;
        String version = indexVersion.value();
        String versionSuffix = version != null ? "-" + version : "";
        return IndexInfo.builder()
                .aliasName(indexName + versionSuffix)
                .mapping(typeMapping)
                .settings(settings)
                .build();
    }

    private <T> T fromFile(JsonpDeserializer<T> deserializer, String filePath) {
        InputStream mappingFile = resourceService.readResourceFile(filePath);
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();
        JsonParser parser = jsonProvider.createParser(mappingFile);
        return deserializer.deserialize(parser, jsonpMapper);
    }

    private static class IndexAnnotations {
        private final Document document;
        private final Mapping mapping;
        private final Setting setting;
        private final IndexVersion indexVersion;

        private IndexAnnotations(Class<?> type) {
            this.document = type.getAnnotation(Document.class);
            Objects.requireNonNull(document, type.getSimpleName() + " should be Document");
            this.mapping = type.getAnnotation(Mapping.class);
            Objects.requireNonNull(document, type.getSimpleName() + " should have Mapping");
            this.setting = type.getAnnotation(Setting.class);
            Objects.requireNonNull(document, type.getSimpleName() + " should have Settings");
            this.indexVersion = type.getAnnotation(IndexVersion.class);
            Objects.requireNonNull(document, type.getSimpleName() + " should have IndexVersion");
        }
    }
}
