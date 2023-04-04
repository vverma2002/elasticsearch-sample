package com.example.es.service.provider;

import com.example.es.entity.search.document.Entity;
import com.example.es.entity.source.SourceType;
import com.example.es.service.source.Source;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SourceProvider<T extends Entity> implements Provider.Source<T> {
    private final Map<SourceType, Source.DbSource<T>> dbSources;

    public SourceProvider(List<Source.DbSource<T>> dbSources) {
        this.dbSources = dbSources.stream()
                .collect(Collectors.toMap(Source.DbSource::getSource, Function.identity()));
    }

    @Override
    public Source.DbSource<T> getDbSource(SourceType sourceType) {
        return dbSources.get(sourceType);
    }

}
