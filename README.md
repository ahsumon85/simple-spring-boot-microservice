## Spring Boot, Spring Cloud Oauth2, Spring Cloud Netflix Eureka, Spring CLoud Zuul, Hystrix Monitoring Dashboard, Spring Data JPA, MySQL

![image](https://user-images.githubusercontent.com/31319842/95044212-b00d7b00-0700-11eb-96eb-431999d05194.jpg)


## Overview

The architecture is composed by five services:
     * `micro-eureka-server`: Service Discovery Server created with Eureka
   * `micro-api-getway`: API Gateway created with Zuul that uses the discovery-service to send the requests to the services. It uses Ribbon as Load Balancer
   * `micro-auth-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **authorization service**
   * `micro-product-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **resource service**
   * `micro-sales-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **resource service**

