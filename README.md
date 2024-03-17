# spring-boot-microservices-with-kafka
TODO:

+users - reuse

!!!order-service  
   !add JWT auth <---
   !add tests for kafka after payment service is done

!!!payment-service <-- 
   !add consumer to get OrderEvent from kafka
      -> 1) made payment
         2) cancel payment for cancelled
   !save data to payments table 
   !send to kafka to another topic orders_processed
   !add JWT auth
   !add tests for kafka

!!!auth service jwt
!!!order-client
!!!spring gateway

!!!update github repo with tags and description
!!!diagrams + screenshots
!!!prepare presentation

!generator orders?
