package org.randomizer.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class Config {

    private static final Properties settings = new Properties();

    public static String getProperty(String property) {
        return settings.getProperty(property);
    }
    
    public static void load(String propertiesConfig) throws IOException {
        settings.load(Config.class.getResourceAsStream(propertiesConfig));
        loadEnvValues();
    }

    private static void loadEnvValues() {
        for (Map.Entry<?, ?> entry: settings.entrySet()) {
            if (((String) entry.getValue()).startsWith("$")) {
                String envVariable = ((String) entry.getValue()).substring(1);
                settings.setProperty((String) entry.getKey(),
                                     System.getenv(envVariable));
            }
        }
    }
}
