package org.amw061.dockerwebapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DockerWebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerWebappApplication.class, args);
    }

    @RestController
    public class DockerRestController {

        @GetMapping
        public Person route() {
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
