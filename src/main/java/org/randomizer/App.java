package org.randomizer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@ComponentScan
@SpringBootApplication
public class App {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(App.class, args);
    }
}
