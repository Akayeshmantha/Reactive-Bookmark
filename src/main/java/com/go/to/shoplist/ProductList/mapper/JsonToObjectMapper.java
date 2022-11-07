package com.go.to.shoplist.ProductList.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.go.to.shoplist.ProductList.entity.ProductEntry;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ReadingConverter
@RequiredArgsConstructor
@Slf4j
public class JsonToObjectMapper implements Converter<Json, Set<ProductEntry>>{
    private final ObjectMapper objectMapper;
    @Override
    public Set<ProductEntry> convert(final Json source) {
        try {
            return objectMapper.readValue(source.asString(), new TypeReference<>() {});
        } catch (IOException e) {
            log.error("Problem while parsing JSON: {}", source, e);
        }
        return new HashSet<>();
    }
}
