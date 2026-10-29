package com.edu.fpt.dto.response;

public record ProfileResponse(
        Long id,
        String accountName,
        String accountEmail,
        Integer accountRole
) {
}
