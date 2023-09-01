package uk.co.lettin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class ConfigVault {

    @Value("")
    String csvCurrencyDataPath;

    public String getCsvCurrencyDataPath() {
        return csvCurrencyDataPath;
    }

    public void setCsvCurrencyDataPath(String csvCurrencyDataPath) {
        this.csvCurrencyDataPath = csvCurrencyDataPath;
    }


}
