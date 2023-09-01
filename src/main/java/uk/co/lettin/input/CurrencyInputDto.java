package uk.co.lettin.input;

import java.math.BigDecimal;

public record CurrencyInputDto (BigDecimal amount, String sourceCode, String targetCode) {}