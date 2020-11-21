## Spring Boot, Spring Cloud Netflix Eureka, Spring CLoud Zuul, Spring Data JPA, MySQL.
![spring-cloud-routing-with-zuul-gateway-0](https://user-images.githubusercontent.com/31319842/98207878-8ac19600-1f66-11eb-82a4-f294e0f0125e.png)


# Overview
The architecture is composed by four services:

   * [`micro-eureka-service`](https://github.com/habibsumoncse/simple-spring-boot-microservice#eureka-service): Service **Discovery Server** created with Eureka
   * [`micro-item-service`](https://github.com/habibsumoncse/simple-spring-boot-microservice#item-service): Simple REST service created with `Spring Boot, Spring Data JPA, MySQL` to use as a **resource service**
   * [`micro-sales-service`](https://github.com/habibsumoncse/simple-spring-boot-microservice#sales-service): Simple REST service created with `Spring Boot, Spring Data JPA, MySQL` to use as a **resource service**
   * [`micro-gateway-service`](https://github.com/habibsumoncse/simple-spring-boot-microservice#api-gateway-service): API Gateway created with Zuul that uses the discovery-service to send the requests to the services. It uses Ribbon as a Load Balancer
   
`Follow the link to see Oauth2 in microservice architecture`[`secure-spring-boot-microservice`](https://github.com/habibsumoncse/secure-spring-boot-microservice)

### tools you will need
* Maven 3.0+ is your build tool
* Your favorite IDE but we will recommend `STS-4-4.4.1 version`. We use STS.
* MySQL Server
* JDK 1.8+


##
# Eureka Service

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

![Screenshot from 2020-11-14 09-39-39](https://user-images.githubusercontent.com/31319842/99139113-3bfbb680-2660-11eb-8800-9efc093c5b38.png)



##
# API Gateway Service

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

# Disable accessing services using service name (i.e. user-service).
# They should be only accessed through the path defined below.
zuul.ignored-services=*

# Map paths to item service
zuul.routes.item-server.path=/item-api/**
zuul.routes.item-server.serviceId=item-server
zuul.routes.item-server.stripPrefix=false

# Map paths to sales service
zuul.routes.sales-server.path=/sales-api/**
zuul.routes.sales-server.serviceId=sales-server
zuul.routes.sales-server.stripPrefix=false

eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
eureka.instance.preferIpAddress=true
eureka.instance.lease-expiration-duration-in-seconds=1
eureka.instance.lease-renewal-interval-in-seconds=2

ribbon.eager-load.enabled= true
ribbon.ConnectTimeout= 30000
ribbon.ReadTimeout= 30000
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


##
# Item Service

Now we will see `micro-item-service` as a resource service. The `micro-item-service` a REST API that lets you CRUD (Create, Read, Update, and Delete) products. It creates a default set of items when the application loads using an `ItemApplicationRunner` bean.

Add the following dependencies:

* **Web:** Spring MVC and embedded Tomcat
* **Actuator:** features to help you monitor and manage your application
* **EurekaClient:** for service registration
* **JPA:** to save/retrieve data
* **MySQL:** to use store data on database
* **RestRepositories:** to expose JPA repositories as REST endpoints
* **Hibernate validator:** to use runtime exception handling and return error messages


***Configure Application Name, Database Information and a few other configuration in properties file***
```
server.port=8280
spring.application.name=item-server
server.servlet.context-path=/item-api

spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/item_service?useSSL=false&createDatabaseIfNotExist=true
spring.datasource.username=[username]
spring.datasource.password=[password]

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL57Dialect
spring.jpa.generate-ddl=true
spring.jpa.show-sql=true

#eureka server url
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.preferIpAddress=true
eureka.instance.lease-expiration-duration-in-seconds=1
eureka.instance.lease-renewal-interval-in-seconds=2
```

***Enable Eureka Registry Service on item service***
Now add the `@SpringBootApplication` and `@EnableEurekaClient` annotation on Spring boot application class present in src folder. With this annotation, this artifact will act like a eureka registry service.

## How to run item service?

### Build Project
Now, you can create an executable JAR file, and run the Spring Boot application by using the Maven or Gradle commands shown below −
For Maven, use the command as shown below −

**Project import in sts4 IDE** 
```File > import > maven > Existing maven project > Root Directory-Browse > Select project form root folder > Finish```

### Run project 

After “BUILD SUCCESSFUL”, you can find the JAR file under the build/libs directory.
Now, run the JAR file by using the following command −

 `java –jar <JARFILE> `
 Run on sts IDE
 `click right button on the project >Run As >Spring Boot App`
 
Eureka Discovery-Service URL: `http://localhost:8761`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `item-service` instance gate will be run on `http://localhost:8280` port

### Test HTTP GET Request on resource service
```
curl --request GET http://localhost:8180/item-api/item/find
```
here `[http://localhost:8180/item-api/item/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the gateway service, `item-api` are context path of item service  and `/item/find` is method URL.

### For getting All API Information
On this repository we will see `simple-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.


##
# Sales Service

Now we will see `micro-sales-service` as a resource service. The `micro-sales-service` a REST API that lets you CRUD (Create, Read, Update, and Delete) products. It creates a default set of products when the application loads using an `SalesApplicationRunner` bean.

Add the following dependencies:

* **Web:** Spring MVC and embedded Tomcat
* **Actuator:** features to help you monitor and manage your application
* **EurekaClient:** for service registration
* **JPA:** to save/retrieve data
* **MySQL:** to use store data on database
* **RestRepositories:** to expose JPA repositories as REST endpoints
* **Hibernate validator:** to use runtime exception handling and return error messages

***Configure Application info, Database info and a few other configuration in properties file***
```
server.port=8380
spring.application.name=sales-server
server.servlet.context-path=/sales-api

spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/sales_service?useSSL=false&createDatabaseIfNotExist=true
spring.datasource.username=[usrename]
spring.datasource.password=[password]

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL57Dialect
spring.jpa.generate-ddl=true
spring.jpa.show-sql=true

#eureka server url configuration
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.preferIpAddress=true
eureka.instance.lease-expiration-duration-in-seconds=1
eureka.instance.lease-renewal-interval-in-seconds=2
```

***Enable Eureka Registry Service on sales service***
Now add the `@SpringBootApplication` and `@EnableEurekaClient` annotation on Spring boot application class present in src folder. With this annotation, this artifact will act like a eureka registry service.

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `sales-server` instance gate will be run on `http://localhost:8380` port

### Test HTTP GET Request on sales service
```
curl --request GET http://localhost:8180/sales-api/sales/find
```
here `[http://localhost:8180/sales-api/sales/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the gateway service, `sales-api` are application context path of sales service and `/sales/find` is method URL.

### For getting All API Information
On this repository we will see `simple-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.


#
### Spring Security Oauth2 in Microservice
#
***Below we will see how to configure oauth2 in microservice***

***To follow link***  [secure-spring-boot-microservice](https://github.com/habibsumoncse/secure-spring-boot-microservice)
