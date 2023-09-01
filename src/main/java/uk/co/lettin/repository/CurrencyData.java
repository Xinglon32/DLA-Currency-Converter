package uk.co.lettin.repository;

import java.math.BigDecimal;

public record CurrencyData(String country, String name, String code, BigDecimal rate) {}
