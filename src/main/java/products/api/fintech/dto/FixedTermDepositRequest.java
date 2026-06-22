package products.api.fintech.dto;

import java.math.BigDecimal;

public record FixedTermDepositRequest(
        Long accountId,
        BigDecimal amount,
        Integer termInDays,
        BigDecimal annualInterestRate
) {
}
