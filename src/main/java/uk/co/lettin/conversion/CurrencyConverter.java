package uk.co.lettin.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.lettin.service.QualifiedCurrency;
import uk.co.lettin.repository.CurrencyConversionDataRepository;
import uk.co.lettin.repository.CurrencyData;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class CurrencyConverter {

    private final CurrencyConversionDataRepository repository;

    //Set significant figures value to a very high value, as setting it to 0 can cause issues with non-terminal values.
    //Since this is only used for interim rounding, rather than the final output, it's set to "Bankers Rounding" given
    // the context is currency.
    private static final MathContext HIGH_PRECISION_ROUNDING = new MathContext(65535, RoundingMode.HALF_EVEN);

    @Autowired
    public CurrencyConverter(CurrencyConversionDataRepository repository) {
        this.repository = repository;
    }

    public QualifiedCurrency convert(QualifiedCurrency toConvert, String targetCode) {
        BigDecimal amountToReturn = toConvert.amount();

        //Multiply "up" to GBP using the rate included in the provided currency
        amountToReturn = amountToReturn.multiply(toConvert.currencyData().rate(), HIGH_PRECISION_ROUNDING);

        //Divide "down" from GBP using the target rate
        CurrencyData targetCurrencyData = repository.getForCode(targetCode);
        amountToReturn = amountToReturn.divide(targetCurrencyData.rate(), HIGH_PRECISION_ROUNDING).setScale(2, RoundingMode.CEILING);

        return new QualifiedCurrency(amountToReturn, targetCurrencyData);
    }

}
