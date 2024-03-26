# spring-boot-microservices-with-kafka
TODO:

!!!check logs and traces (traceId and spanId)
not working!!!
logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]

!!!add postgresql to payment service?

!!!update github repo with tags and description
!!!diagrams + screenshots
!!!prepare presentation

!!!add postgresql - check postgresql driver in pom???
<dependency>
<groupId>org.postgresql</groupId>
<artifactId>postgresql</artifactId>
<scope>runtime</scope>
</dependency>

