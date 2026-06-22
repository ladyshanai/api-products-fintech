package products.api.fintech.dto;

import products.api.fintech.enums.FixedTermDepositStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FixedTermDepositResponse(
        Long id,
        Long accountId,
        BigDecimal amount,
        Integer termInDays,
        BigDecimal annualInterestRate,
        BigDecimal expectedReturn,
        LocalDate startDate,
        LocalDate maturityDate,
        FixedTermDepositStatus status,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}