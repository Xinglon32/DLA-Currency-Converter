package uk.co.lettin.repository;

import java.util.HashMap;
import java.util.Map;

/*
 * Hides the implementation details of the data provided to the conversion process,
 * allowing the source to be changed or updated without the contract with the service layer changing
 */
public abstract class CurrencyConversionDataRepository {

    protected final Map<String, CurrencyData> currencyData = new HashMap<>();
    public CurrencyData getForCode(String code) {
        return currencyData.get(code);
    }

}
