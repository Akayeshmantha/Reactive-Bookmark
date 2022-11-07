package com.go.to.shoplist.ProductList.exception;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import com.go.to.shoplist.ProductList.dto.Error;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Slf4j
@Component
@Order(-2)
@RequiredArgsConstructor
@EnableWebFlux
public class GlobalHandler implements WebExceptionHandler {
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable e) {
        log.warn("error => ", e);
        if (e instanceof IllegalArgumentException || e instanceof  NullPointerException){
            final Error err = Error.invalidRequest(e.getMessage());
            err.setErrorAt(LocalDateTime.now());
            err.setErrorTraceId(UUID.randomUUID().toString());
            return produceJson(err, exchange);
        }else if(e instanceof ProductListNotFoundException) {
            final Error err = Error.notFound(e.getMessage());
            err.setErrorAt(LocalDateTime.now());
            err.setErrorTraceId(UUID.randomUUID().toString());
            return produceJson(err, exchange);
        }
        final Error err = Error.serverError(e.getMessage());
        err.setErrorAt(LocalDateTime.now());
        err.setErrorTraceId(UUID.randomUUID().toString());
        return produceJson(err, exchange);
    }

    private void setHeaders(final Error err, final ServerHttpResponse response){
        final HttpHeaders headers = response.getHeaders();
        response.setStatusCode(HttpStatus.valueOf(err.getErrorStatus()));
        try {
            headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        } catch (UnsupportedOperationException e) {

        }
    }

    public Mono<Void> produceJson(final Error err, final ServerWebExchange exchange) {
        return Mono.defer(() -> {
            try {
                final ServerHttpResponse response = exchange.getResponse();
                setHeaders(err, response);
                final String json = objectMapper.writeValueAsString(err);
                final DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(buffer))
                        .doOnError(e -> DataBufferUtils.release(buffer));
            } catch (final Exception e) {
                return Mono.error(e);
            }
        });
    }
}
