# Getting Started

The edge service - the first port of calls, it intercepts requests coming in from the outside world, it's where we handle cross-cutting kinds of concerns things like security, routing, rate limiting, compression, circuit breaking  

http://localhost:9999/proxy

http://localhost:9999/customersAndOrders

Distributed transactions make no sense in the world of microservices, instead we tell microservices know about the changes in the world, and they create their own view of this data (tell/don't ask pattern).
We use Spring Cloud Stream to send messages to microservices.

