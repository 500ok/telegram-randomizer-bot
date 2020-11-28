package org.randomizer.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

public class Config {

    private static final Properties settings = new Properties();
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    public static String getProperty(String property) {
        return settings.getProperty(property);
    }



    public static void load(String propertiesConfig) throws IOException {
        LOGGER.debug("Loading configuration");
        settings.load(Config.class.getResourceAsStream(propertiesConfig));
        loadEnvValues();
        configureUnirest();
        LOGGER.debug("Configuration loaded");
    }

    private static void configureUnirest() {
        LOGGER.debug("Loading unirest configuration");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false
        );

        Unirest.config()
                .enableCookieManagement(false)
                .concurrency(10, 5)
                .setObjectMapper(new kong.unirest.ObjectMapper() {
                    @Override
                    public <T> T readValue(String value, Class<T> valueType) {
                        try {
                            return mapper.readValue(value, valueType);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public String writeValue(Object value) {
                        StringWriter json = new StringWriter();
                        try {
                            mapper.writeValue(json, value);
                            return json.toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return json.toString();
                        }
                    }
                }).cacheResponses(false);
        LOGGER.debug("Unirest configuration loaded");
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
