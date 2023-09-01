package uk.co.lettin.input;

/*
 * Hides the implementation details of the user input to the conversion process,
 * allowing the source to be changed or updated without the contract with the service layer changing
 */
public interface UserInputReader {

    CurrencyInputDto fetchInputData();

}
