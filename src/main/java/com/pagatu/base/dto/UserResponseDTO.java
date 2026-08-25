package com.pagatu.base.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String fullName,
        String email,
        String phone,
        String accountNumber,
        String accountType,
        BigDecimal balance,
        String status,
        Long roleId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
