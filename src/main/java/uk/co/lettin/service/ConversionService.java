package uk.co.lettin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.lettin.conversion.CurrencyConverter;
import uk.co.lettin.output.UserOutputWriter;
import uk.co.lettin.input.CurrencyInputDto;
import uk.co.lettin.input.UserInputReader;
import uk.co.lettin.repository.CurrencyConversionDataRepository;

@Service
public class ConversionService {

    private final UserInputReader userInputReader;
    private final CurrencyConverter currencyConverter;
    private final UserOutputWriter userOutputWriter;
    private final CurrencyConversionDataRepository repository;

    @Autowired
    public ConversionService(UserInputReader userInputReader, CurrencyConverter currencyConverter, UserOutputWriter userOutputWriter, CurrencyConversionDataRepository repository) {
        this.userInputReader = userInputReader;
        this.currencyConverter = currencyConverter;
        this.userOutputWriter = userOutputWriter;
        this.repository = repository;
    }

    public void start() {
        //Collect User Data
        CurrencyInputDto userInput = userInputReader.fetchInputData();

        //Convert to Domain Types
        QualifiedCurrency inputAmount = new QualifiedCurrency(userInput.amount(), repository.getForCode(userInput.sourceCode()));

        //Perform conversion
        QualifiedCurrency convertedAmount = currencyConverter.convert(inputAmount, userInput.targetCode());

        //Provide User Feedback
        userOutputWriter.writeOutput(convertedAmount);
    }


}
