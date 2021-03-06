package org.pyatsotok.randomizer.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringWriter;

@Slf4j
@Configuration
public class UnirestConfig {


    @PostConstruct
    public void initUnirest() {
        ObjectMapper jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false
        );

        Unirest.config().reset();
        log.debug("Loading unirest configuration");

        Unirest.config()
                .enableCookieManagement(false)
                .setObjectMapper(new kong.unirest.ObjectMapper() {
                    @Override
                    public <T> T readValue(String value, Class<T> valueType) {
                        try {
                            return jacksonMapper.readValue(value, valueType);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public String writeValue(Object value) {
                        StringWriter json = new StringWriter();
                        try {
                            jacksonMapper.writeValue(json, value);
                            return json.toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return json.toString();
                        }
                    }
                }).cacheResponses(false);

        log.debug("Unirest configuration loaded");
    }
}
