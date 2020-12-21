package com.example.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    WebClient http(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        return builder.tcp("localhost", 8181);
    }

    @Bean
    RouteLocator gateway(RouteLocatorBuilder rlb) {
        return rlb.routes()
                .route(routeSpec -> routeSpec
                        .path("/proxy")
                        .filters(filterSpec -> filterSpec.setPath("/customers"))
                        .uri("http://localhost:8082")
                )
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> httpEndpoints(CrmClient crmClient) {
        return route()
                .GET("/customersAndOrders", serverRequest -> {
                    Flux<CustomerOrders> customerOrders = crmClient.getCustomerOrders();
                    return ServerResponse.ok().body(customerOrders, CustomerOrders.class);
                })
                .build();
    }
}

@Component
@AllArgsConstructor
class CrmClient {

    // reactive nonblocking web client
    private final WebClient http;

    // reactive nonblocking RSocket client
    private final RSocketRequester rSocket;

    Flux<Customer> getCustomers() {
        // hedging - ask 3 same services and use only one response, cancelling the others
        // Flux.firstWithSignal();

        return http.get().uri("http://localhost:8082/customers")
                .retrieve()
                .bodyToFlux(Customer.class)
                .retryWhen(Retry.backoff(10, Duration.ofSeconds(1)))
                .onErrorResume(ex -> Flux.empty())
                .timeout(Duration.ofSeconds(10));
    }

    Flux<Order> getOrdersFor(Integer customerId) {
        return rSocket.route("orders.{customerId}", customerId)
                .retrieveFlux(Order.class);
    }

    Flux<CustomerOrders> getCustomerOrders() {
        return getCustomers()
                .flatMap(customer -> Flux.zip(Mono.just(customer), getOrdersFor(customer.getId()).collectList()))
                .map(tuple -> new CustomerOrders(tuple.getT1(), tuple.getT2()));
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class CustomerOrders {
    private Customer customer;
    private List<Order> orders;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Order {

    private Integer id;
    private Integer customerId;
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {

    private Integer id;

    private String name;
}
