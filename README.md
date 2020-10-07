## Spring Boot, Spring Cloud Oauth2, Spring Cloud Netflix Eureka, Spring CLoud Zuul, Hystrix Monitoring Dashboard, Spring Data JPA, MySQL




# Overview

The architecture is composed by five services:

   * `micro-eureka-server`: Service **Discovery Server** created with Eureka
   * `micro-api-getway`: API Gateway created with Zuul that uses the discovery-service to send the requests to the services. It uses Ribbon as Load Balancer
   * `micro-auth-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **authorization service**
   * `micro-product-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **resource service**
   * `micro-sales-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **resource service**
 
##
# micro-eureka-service

Eureka Server is an application that holds the information about all client-service applications. Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address. Eureka Server is also known as Discovery Server.

**Implementing a Eureka Server for service registry is as easy as:**

we need to add `@EnableEurekaServer` annotation. The `@EnableEurekaServer` annotation is used to make your Spring Boot application acts as a Eureka Server.

```
@SpringBootApplication
@EnableEurekaServer // Enable eureka server
public class EurekaServerRunner {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerRunner.class, args);
		System.out.println("Eureka Server Started....!!");
	}
}
```

Make sure Spring cloud Eureka server dependency is added in your build configuration file.
The code for Maven user dependency is shown below −
```
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

By default, the Eureka Server registers itself into the discovery. You should add the below given configuration into your `application.properties` file or `application.yml` file.

```
# Give a name to the eureka server
spring.application.name=eureka-server

# default port for eureka server
server.port=8761

# eureka by default will register itself as a client. So, we need to set it to false.
# What's a client server? See other microservices (employee, user, etc).
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

## How to run?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven or Gradle commands shown below −
For Maven, use the command as shown below −

`mvn clean install`
or

**Project import in sts4 IDE** 
```File >import >maven >Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

 `java –jar <JARFILE> `

 Run on sts IDE
 
 `click right button on the project >Run As >Spring Boot App`
 
 Discovery-Service URL: `http://localhost:8761`



# micro-auth-service
Whenever we think of microservices and distributed applications, the first point that comes to mind is security. Obviously, in distributed architectures, it is really difficult to manage security as we do not have much control over the application. So in this situation, we always need to have a central entry point to this distributed architecture. This is the reason why, in microservices, we have a separate and dedicated layer for all these purposes. This layer is known as the API Gateway. It is an entry point for a microservice's architecture.

To maintain security, the first necessary condition is to restrict direct microservice calls for outside callers. All calls should only go through the API Gateway. The API Gateway is mainly responsible for authentication and authorization of the API requests made by external callers. Also, this layer performs the routing of API requests that come from external clients to respective microservices. This allows the API Gateway to act as an entry point for all its respective microservices. So, we can say the API Gateway is mainly responsible for the security of microservices.

## Oauth2
In this Spring security oauth2 tutorial, learn to build an authorization server to authenticate your identity to provide access_token, which you can use to request data from resource server.

***Introduction to OAuth 2***
OAuth 2 is an authorization method to provide access to protected resources over the HTTP protocol. Primarily, oauth2 enables a third-party application to obtain limited access to an HTTP service –

* either on behalf of a resource owner by orchestrating an approval interaction between the resource owner and the HTTP service
* or by allowing the third-party application to obtain access on its own behalf.
