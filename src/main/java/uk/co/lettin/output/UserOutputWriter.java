package uk.co.lettin.output;

import uk.co.lettin.service.QualifiedCurrency;

/*
 * Hides the implementation details of the user output from the conversion process,
 * allowing the source to be changed or updated without the contract with the service layer changing
 */
public interface UserOutputWriter {

    void writeOutput(QualifiedCurrency qualifiedCurrency);

}