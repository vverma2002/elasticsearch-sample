package com.example.es.service.parser;

import co.elastic.clients.json.JsonData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ParserService implements Parser.Service {
    private final Map<Class<?>, Parser<?>> parsers;

    public ParserService(List<Parser<?>> parsers) {
        this.parsers = parsers.stream()
                .collect(Collectors.toMap(Parser::getType, Function.identity()));
    }

    @Override
    public <T> T parse(Map<String, JsonData> fields, Class<T> documentType) {
        return ((Parser<T>) parsers.get(documentType))
                .parse(fields);
    }
}
