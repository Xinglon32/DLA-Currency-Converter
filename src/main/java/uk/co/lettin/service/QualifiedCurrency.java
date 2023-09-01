package uk.co.lettin.service;

import uk.co.lettin.repository.CurrencyData;
import java.math.BigDecimal;

public record QualifiedCurrency (BigDecimal amount, CurrencyData currencyData) {}