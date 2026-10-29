package com.edu.fpt.dto.response;

public record AccountReponse(
        Long id,
        String accountName,
        String accountEmail,
        Integer accountRole

) {
}
