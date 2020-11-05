## Spring Boot, Spring Cloud Netflix Eureka, Spring CLoud Zuul, Spring Data JPA, MySQL


# Overview

The architecture is composed by five services:

   * `micro-eureka-server`: Service **Discovery Server** created with Eureka
   * `micro-product-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **resource service**
   * `micro-sales-service`: Simple REST service created with `Spring Boot, Spring Cloud Oauth2, Spring Data JPA, MySQL` to use as an **resource service**
   * `micro-api-getway`: API Gateway created with Zuul that uses the discovery-service to send the requests to the services. It uses Ribbon as a Load Balancer
 
##
# micro-eureka-service

Eureka Server is an application that holds the information about all client-service applications. Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address. Eureka Server is also known as Discovery Server.

**Implementing a Eureka Server for service registry is as easy as**

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

## How to run eureka service?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven or Gradle commands shown below −
For Maven, use the command as shown below −

`mvn clean install`
or

**Project import in sts4 IDE** 
```File > import > maven > Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

 `java –jar <JARFILE> `

 Run on sts IDE
 
 `click right button on the project >Run As >Spring Boot App`
 
Eureka Discovery-Service URL: `http://localhost:8761`



#
### micro-product-service

Now we will see  `micro-product-service` as a resource service 

### HTTP GET Request
```
curl --request GET http://localhost:8180/product-api/product/find
```
##
# Zuul API Gateway

***Enable Zuul Service Proxy***
Now add the `@EnableZuulProxy` and `@EnableEurekaClient` annotation on Spring boot application class present in src folder. With this annotation, this artifact will act like a Zuul service proxy and will enable all the features of a API gateway layer as described before. We will then add some filters and route configurations.
```
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ZuulApiGetWayRunner {

	public static void main(String[] args) {
		SpringApplication.run(ZuulApiGetWayRunner.class, args);
		System.out.println("Zuul server is running...");
	}

	@Bean
	public PreFilter preFilter() {
		return new PreFilter();
	}

	@Bean
	public PostFilter postFilter() {
		return new PostFilter();
	}

	@Bean
	public ErrorFilter errorFilter() {
		return new ErrorFilter();
	}

	@Bean
	public RouteFilter routeFilter() {
		return new RouteFilter();
	}
}
```
***Zuul routes configuration***
Open application.properties and add below entries-
```
#Will start the gateway server @8180
server.port=8180
spring.application.name=zuul-server

eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
eureka.instance.preferIpAddress=true

# A prefix that can added to beginning of all requests. 
#zuul.prefix=/api

# Disable accessing services using service name (i.e. user-service).
# They should be only accessed through the path defined below.
zuul.ignored-services=*

zuul.routes.secound.id=sales-server
zuul.routes.first.id=product-server

# Map paths to employee service
zuul.routes.product-server.path=/product-api/**
zuul.routes.product-server.serviceId=product-server
zuul.routes.product-server.stripPrefix=false

# Map paths to sales service
zuul.routes.sales-server.path=/sales-api/**
zuul.routes.sales-server.serviceId=sales-server
zuul.routes.sales-server.stripPrefix=false

eureka.instance.lease-expiration-duration-in-seconds=1
eureka.instance.lease-renewal-interval-in-seconds=2
#eureka.client.healthcheck.enabled=true
#logging.level.zuul.api.getway=DEBUG


#Set the Hystrix isolation policy to the thread pool
zuul.ribbon-isolation-strategy=thread



#each route uses a separate thread pool
zuul.thread-pool.use-separate-thread-pools=true
```

## How to run API Gateway Service?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven or Gradle commands shown below −
For Maven, use the command as shown below −

`mvn clean install`
or

**Project import in sts4 IDE** 
```File > import > maven > Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

 `java –jar <JARFILE> `

 Run on sts IDE
 
 `click right button on the project >Run As >Spring Boot App`
 
After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `zuul-server` instance gate will be run on `http://localhost:8180` port
