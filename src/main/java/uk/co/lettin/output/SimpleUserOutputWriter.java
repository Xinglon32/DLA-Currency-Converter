package uk.co.lettin.output;

import uk.co.lettin.service.QualifiedCurrency;

import java.io.PrintStream;

public class SimpleUserOutputWriter implements UserOutputWriter {

    private final PrintStream printStream;

    // While this class could create its own PrintStream, testing is easier if we inject it. The downside becomes
    //   the manual bean creation in BeanConfiguration, which code-styles could dictate exists anyway.
    public SimpleUserOutputWriter(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void writeOutput(QualifiedCurrency qualifiedCurrency) {
        printStream.println(qualifiedCurrency.amount() + " " + qualifiedCurrency.currencyData().country() + " " + qualifiedCurrency.currencyData().name());
    }
}
