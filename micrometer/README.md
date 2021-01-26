
---

http://localhost:8080/actuator
http://localhost:8080/actuator/prometheus

Spring Boot app running at http://192.168.99.1:8080/actuator/prometheus

---
docker run -p 9090:9090 -v /Users/sg0218817/Downloads/micrometer/src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus

http://192.168.99.1:9090/graph?g0.expr=go_memstats_heap_alloc_bytes&g0.tab=0

---

docker run -d --name=grafana -p 3000:3000 grafana/grafana

---
