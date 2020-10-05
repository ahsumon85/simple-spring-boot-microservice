## Spring Boot, Spring Cloud Oauth2, Spring Cloud Netflix Eureka, Spring CLoud Zuul, Hystrix Monitoring Dashboard, Spring Data JPA, MySQL




## Overview

The architecture is composed by five services:

   * `micro-eureka-server`: Service Discovery Server created with Eureka
   * `micro-api-getway`: API Gateway created with Zuul that uses the discovery-service to send the requests to the services. It uses Ribbon as Load Balancer
   * `micro-auth-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **authorization service**
   * `micro-product-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **resource service**
   * `micro-sales-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **resource service**
 
###
### micro-eureka-service

Eureka Server is an application that holds the information about all client-service applications. Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address. Eureka Server is also known as Discovery Server.

**Implementing a Eureka Server for service registry is as easy as:**

we need to add `@EnableEurekaServer` annotation. The `@EnableEurekaServer` annotation is used to make your Spring Boot application acts as a Eureka Server.

`@SpringBootApplication`
`@EnableEurekaServer`
`public class EurekaServerApplication {`
`    public static void main(String[] args) {`
`        SpringApplication.run(EurekaServerApplication.class, args);`
 `   }`
`}`


