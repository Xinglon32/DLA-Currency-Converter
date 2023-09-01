package uk.co.lettin.input;

import uk.co.lettin.service.CurrencyCodeValidator;

import java.math.BigDecimal;
import java.util.Scanner;

public class SimpleUserInputReader implements UserInputReader {

    private final Scanner inputScanner;
    private final CurrencyCodeValidator currencyCodeValidator;
    private final static String AMOUNT_REGEX = "^\\d+\\.\\d{2}$";

    // While this class could create its own System.in scanner, testing is easier if we inject it. The downside becomes
    //   the manual bean creation in BeanConfiguration, which code-styles could dictate exists anyway.
    public SimpleUserInputReader(Scanner inputScanner, CurrencyCodeValidator currencyCodeValidator) {
        this.inputScanner = inputScanner;
        this.currencyCodeValidator = currencyCodeValidator;
    }

    @Override
    public CurrencyInputDto fetchInputData() {
        BigDecimal inputAmount = new BigDecimal(readAmount());
        String sourceCode = readSourceCode();
        String targetCode = readTargetCode();
        return new CurrencyInputDto(inputAmount, sourceCode, targetCode);
    }

    private String readAmount() {
        System.out.println("Please enter the amount of currency you wish to convert. Use format \"x.yy\":");
        return readValueMatching(AMOUNT_REGEX);
    }

    private String readSourceCode() {
        System.out.println("Please enter the ISO 4217 currency code for your input amount: ");
        return readCurrencyCode();
    }

    private String readTargetCode() {
        System.out.println("Please enter the ISO 4217 currency code for your output amount: ");
        return readCurrencyCode();
    }

    private String readValueMatching(String regex) {
        String input = inputScanner.nextLine().trim();
        while (!input.matches(regex)) {
            System.err.println("Invalid format, please try again: ");
            input = inputScanner.nextLine().trim();
        }
        return input;
    }

    private String readCurrencyCode() {
        String toReturn;
        do {
            toReturn = readValueMatching(CurrencyCodeValidator.CURRENCY_CODE_REGEX);
            if (currencyCodeValidator.isAcceptableCode(toReturn)) {
                return toReturn;
            } else {
                System.err.println("Unrecognised Currency Code, please try again:");
            }
        } while (true);
    }

}
