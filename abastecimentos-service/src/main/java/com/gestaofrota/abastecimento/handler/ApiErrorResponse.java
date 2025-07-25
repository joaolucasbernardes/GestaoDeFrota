package com.gestaofrota.abastecimento.handler;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        List<String> messages,
        String path
) {
}