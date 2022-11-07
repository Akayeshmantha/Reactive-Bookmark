package com.go.to.shoplist.ProductList.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    @JsonProperty("error")
    private String error;

    @JsonProperty("error_status")
    private int errorStatus;

    @JsonProperty("error_description")
    private String errorDescription;

    @JsonProperty("error_at")
    private LocalDateTime errorAt;

    @JsonProperty("error_trace_id")
    private String errorTraceId;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Field {

        private String name;

        private String code;

        private String description;
    }

    public static Error invalidRequest(String message) {
        return builder()
                .error("invalid_request")
                .errorDescription(message)
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    public static Error notFound(String message) {
        return builder()
                .error("not_found")
                .errorDescription(message)
                .errorStatus(HttpStatus.NOT_FOUND.value())
                .build();
    }

    public static Error serverError(String message) {
        return builder()
                .error("server_error")
                .errorDescription(message)
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }
}
