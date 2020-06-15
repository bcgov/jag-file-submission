package ca.bc.gov.open.jagefilingapi.qa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadConfig {

    public static Properties prop;

    public ReadConfig() {
            File src = new File("src\\main\\java\\ca\\bc\\gov\\open\\jagefilingapi\\qa\\config\\config.properties");

            try {
                FileInputStream fis = new FileInputStream(src);
                prop = new Properties();
                prop.load(fis);
            } catch  (FileNotFoundException e) {
                System.out.println("Exception is " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getBaseUrl() {
            return prop.getProperty("baseUrl");
        }

        public String getBrowser() {
        return prop.getProperty("browser");
    }
    }


