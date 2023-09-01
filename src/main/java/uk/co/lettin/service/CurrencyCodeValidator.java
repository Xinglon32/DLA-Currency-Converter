package uk.co.lettin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.lettin.repository.CurrencyConversionDataRepository;

@Component
public class CurrencyCodeValidator {

    public static final String CURRENCY_CODE_REGEX = "^[A-Z]{3}$";
    private final CurrencyConversionDataRepository currencyConversionDataRepository;

    @Autowired
    public CurrencyCodeValidator(CurrencyConversionDataRepository currencyConversionDataRepository) {
        this.currencyConversionDataRepository = currencyConversionDataRepository;
    }

    //This method was the only place any other method on the repository would have been required, so even though creating
    //  a key check route would be better (rather than have it dredge up the whole record), this is simpler\shorter.
    public boolean isAcceptableCode(String code) {
        return (currencyConversionDataRepository.getForCode(code) != null);
    }

}
