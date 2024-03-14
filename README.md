# spring-boot-microservices-with-kafka
TODO:

+users - reuse

!order-service  <--- implement base without kafka
order[List of Products(id, quantity)]->order->service
-> save to repo (order in progress) - > send to kafka -> 
update order (processing)
(listen payments topic and update and notify user
that payment was succsfull - wait for delivery)

!spring gateway
!auth service jwt
!add kafka to order-service

!payment-service <-- implement base
payment service -> get from kafka order - > 
save to another db (postgres/mongo) ->
send to kafka to another topic payments

!add kafka to payment-service

!generator orders
!add UI for kafka/DB orders

!description
!diagram
!presentation

!bonus: zipkin, docker