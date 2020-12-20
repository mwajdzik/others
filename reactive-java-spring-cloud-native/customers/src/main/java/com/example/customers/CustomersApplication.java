package com.example.customers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication(proxyBeanMethods = false)
public class CustomersApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomersApplication.class, args);
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> ready(DatabaseClient dc, CustomerRepository repo) {
        return applicationReadyEvent -> {
            Mono<Integer> createTable = dc.sql("create table customer(id serial primary key not null, name varchar(255) not null)").fetch().rowsUpdated();

            Flux<Customer> names = Flux.just("Josh", "Marcin", "Olga", "Madhura", "Violetta", "Stephan", "Dr. Syer", "Yuxin")
                    .map(name -> new Customer(null, name))
                    .flatMap(repo::save);

            Flux<Customer> all = repo.findAll();

            createTable.thenMany(names)
                    .thenMany(all)
                    .subscribe(System.out::println);
        };
    }
}

interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}

@RestController
@RequiredArgsConstructor
class CustomerRestController {

    public final CustomerRepository customerRepository;

    @GetMapping("/customers")
    Flux<Customer> getAll() {
        return customerRepository.findAll();
    }
}

@RestController
@RequiredArgsConstructor
class LivenessRestController {

    public final ApplicationContext applicationContext;

    @PostMapping("/down")
    Mono<Void> down() {
        AvailabilityChangeEvent.publish(this.applicationContext, LivenessState.BROKEN);
        return Mono.empty();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {

    @Id
    private Integer id;

    private String name;
}
