package com.example.es.service.provider;

import com.example.es.entity.search.IndexType;
import com.example.es.entity.search.document.Entity;
import com.example.es.service.populator.Populator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopulatorProvider<T extends Entity> implements Provider.Populator<T> {
    private final List<Populator<T>> populators;

    @Override
    public List<Populator<T>> getPopulators(IndexType indexType) {
        return populators.stream()
                .filter(populator -> populator.supports(indexType))
                .collect(Collectors.toList());
    }
}
