package ca.bc.gov.open.jagefilingapi.qa.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadConfig {

    Properties prop;

    public ReadConfig() throws IOException {
            File src = new File("src/main/java/ca/bc/gov/open/jagefilingapi/qa/config/config.properties");

                FileInputStream fis = new FileInputStream(src);
                prop = new Properties();
                prop.load(fis);
        }

        public String getBaseUrl() {
            return prop.getProperty("baseUrl");
        }

        public String getBaseUri() {
        return prop.getProperty("baseUri");
    }

        public String getResource() {
        return prop.getProperty("resource");
    }

        public String getBrowser() {
            return prop.getProperty("browser");
    }
}

