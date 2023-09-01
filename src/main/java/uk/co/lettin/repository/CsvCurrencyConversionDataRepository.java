package uk.co.lettin.repository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import uk.co.lettin.ConfigVault;

import java.io.*;
import java.math.BigDecimal;

public class CsvCurrencyConversionDataRepository extends CurrencyConversionDataRepository {

    public CsvCurrencyConversionDataRepository(ConfigVault configVault) {
        //String fileName = configVault.getCsvCurrencyDataPath();
        //In theory, we'd pull this value from the config above, but as I don't know how this will be executed by the
        // interviewer, I've left this fixed, assuming it will be run in an IDE.
        // The config should be set to pull in this way
        String fileName = "src\\main\\resources\\data.txt";
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            //Given this is not a user-supplied file, I have opted out of validating the contents
            reader.readAll().stream()
                    .map(line -> new CurrencyData(line[0].trim(), line[1].trim(), line[2].trim(), new BigDecimal(line[3].trim())))
                    //.peek(System.out::println)
                    .forEach(record -> currencyData.put(record.code(), record));
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("CSV file is Malformed", e);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

}
