package br.com.hexagonal.architecture.journey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("br.com")
public class AdapterManagementJourneyApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdapterManagementJourneyApplication.class, args);
    }

}
