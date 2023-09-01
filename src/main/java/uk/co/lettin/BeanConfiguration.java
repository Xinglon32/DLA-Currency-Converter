package uk.co.lettin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.lettin.service.CurrencyCodeValidator;
import uk.co.lettin.repository.CsvCurrencyConversionDataRepository;
import uk.co.lettin.repository.CurrencyConversionDataRepository;
import uk.co.lettin.output.SimpleUserOutputWriter;
import uk.co.lettin.output.UserOutputWriter;
import uk.co.lettin.input.SimpleUserInputReader;
import uk.co.lettin.input.UserInputReader;

import java.util.Scanner;

@Configuration
public class BeanConfiguration {

    @Bean
    public UserInputReader userInputReceiver(CurrencyCodeValidator currencyCodeValidator) {
        return new SimpleUserInputReader(new Scanner(System.in), currencyCodeValidator);
    }

    @Bean
    public UserOutputWriter userOutputWriter() {
        return new SimpleUserOutputWriter(System.out);
    }

    //While not explicitly required by Spring, I generally find it easier to read which implementations of interfaces
    //  "Adapter-like" patterns are adopting by declaring them all manually like this, even though only the first two
    //  serve a proper function.
    @Bean
    public CurrencyConversionDataRepository currencyConversionDataRepository(ConfigVault configVault) {
        return new CsvCurrencyConversionDataRepository(configVault);
    }

}
