package com.rpissarra.exception;

import java.time.LocalDateTime;

public record ApiError(
        String path,
        String description,
        int responseCode,
        LocalDateTime localDateTime
) {
}
