# spring-boot-microservices-with-kafka
TODO:


!!!add integration test with testcontainer for kafka listeners

!!!check logs and traces (traceId and spanId)
logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]

!!!add postgresql to payment service?

https://testcontainers.com/guides/testing-spring-boot-kafka-listener-using-testcontainers/

!!!update github repo with tags and description
!!!diagrams + screenshots
!!!prepare presentation

!!!add postgresql - check postgresql driver in pom???
<dependency>
<groupId>org.postgresql</groupId>
<artifactId>postgresql</artifactId>
<scope>runtime</scope>
</dependency>

