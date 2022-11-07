package com.go.to.shoplist.ProductList.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.go.to.shoplist.ProductList.entity.ProductEntry;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.Set;

@WritingConverter
@RequiredArgsConstructor
@Slf4j
public class ObjectToJsonMapper implements Converter<Set<ProductEntry>, Json>{
    private final ObjectMapper objectMapper;

    @Override
    public Json convert(Set<ProductEntry> source) {
        try {
            return Json.of(objectMapper.writeValueAsString(source));
        } catch (JsonProcessingException e) {
            log.error("Error occurred while serializing object to JSON: {}", source, e);
        }
        return Json.of("");
    }
}
