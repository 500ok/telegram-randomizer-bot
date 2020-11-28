package org.randomizer.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;

import java.io.IOException;
import java.io.StringWriter;
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
        configureUnirest();
    }

    private static void configureUnirest() {
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
