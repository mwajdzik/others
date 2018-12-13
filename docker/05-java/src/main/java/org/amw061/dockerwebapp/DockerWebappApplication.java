package org.amw061.dockerwebapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@SpringBootApplication
public class DockerWebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerWebappApplication.class, args);
    }

    @RestController
    public class DockerRestController {

        @GetMapping("/person")
        public Person route() {
            log.info("Got a new request to send a person object");
            return new Person("Jakub", 23);
        }
    }

    @Data
    @AllArgsConstructor
    private static class Person {
        private String name;
        private int age;
    }
}
