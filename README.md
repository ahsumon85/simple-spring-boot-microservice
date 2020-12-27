# Spring Boot, Spring Cloud Netflix Eureka, Spring CLoud Zuul, Spring Data JPA, MySQL.
![spring-cloud-routing-with-zuul-gateway-0](https://user-images.githubusercontent.com/31319842/98207878-8ac19600-1f66-11eb-82a4-f294e0f0125e.png)


## Overview
### The architecture is composed by four services:

   * [`micro-eureka-service`](https://github.com/ahsumon85/simple-spring-boot-microservice#eureka-service): Service **Discovery Server** created with Eureka
   * [`micro-gateway-service`](https://github.com/ahsumon85/simple-spring-boot-microservice#api-gateway-service): API Gateway created with Zuul that uses the discovery-service to send the requests to the services. It uses Ribbon as a Load Balancer
   * [`micro-item-service`](https://github.com/ahsumon85/simple-spring-boot-microservice#item-service): Simple REST service created with `Spring Boot, Spring Data JPA, MySQL` to use as a **resource service**
   * [`micro-sales-service`](https://github.com/ahsumon85/simple-spring-boot-microservice#sales-service): Simple REST service created with `Spring Boot, Spring Data JPA, MySQL` to use as a **resource service**

`Follow the link to see Oauth2 in microservice architecture`[`secure-spring-boot-microservice`](https://github.com/habibsumoncse/secure-spring-boot-microservice)

### Tools you will need
* Maven 3.0+ is your build tool

* Your favorite IDE but we will recommend `STS-4-4.4.1 version`. We use STS.

* MySQL Server

* JDK 1.8+

### Microservice Running Process:

- First we need to run `eureka service`
- Second  we need to run `item-servic` and `sales-service`
- Finally we need to run `gateway-service`, if we did run `gateway-service` before running `iteam-service, sales-service` then we have to wait approximately 10 second 



## Eureka Service

Eureka Server is an application that holds the information about all client-service applications. Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address. Eureka Server is also known as Discovery Server.

### Implementing a Eureka Server for service registry is as easy as

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

### How to run eureka service?


 `click right button on the project >Run As >Spring Boot App`

Eureka Discovery-Service URL: `http://localhost:8761`

## API Gateway Service

A common problem, when building microservices, is to provide a unique gateway to the client applications of your system. The fact that your services are split into small microservices apps that shouldn’t be visible to users otherwise it may result in substantial development/maintenance efforts. Also there are scenarios when whole ecosystem network traffic may be passing through a single point which could impact the performance of the cluster.

### Zuul Components

Zuul has mainly four types of filters that enable us to intercept the traffic in different timeline of the request processing for any particular transaction. We can add any number of filters for a particular url pattern.

- **pre filters** – are invoked before the request is routed.
- **post filters** – are invoked after the request has been routed.
- **route filters** – are used to route the request.
- **error filters** – are invoked when an error occurs while handling the request.

![Zull-filters](https://user-images.githubusercontent.com/31319842/101316221-49364a80-3886-11eb-8037-163dd77554c7.jpg)



### Enable Zuul Service Proxy

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
### Zuul routes configuration
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

### Add Zuul Filters

We will now add few filters as we have already described, Zuul supports 4 types of filters namely `pre`,`post`,`route`, `error` and `CORS` . Here we will create each type of filters.

**pre filter code** – We will add the below pre filter. Currently filters are doing nothing apart from a `println` for testing purpose. But actually those are powerful enough to do many important aspects as mentioned before.

```
public class PreFilter extends ZuulFilter {
	@Override
	public String filterType() {
		return "pre";
	}
	@Override
	public int filterOrder() {
		return 1;
	}
	@Override
	public boolean shouldFilter() {
		return true;
	}
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		System.out.println(
				"Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());

		if (request.getHeader("Authorization") != null) {
			ctx.addZuulRequestHeader("Authorization", request.getHeader("Authorization"));
		}
		return null;
	}
}
```

### post filter

```
public class PostFilter extends ZuulFilter {
  @Override
  public String filterType() {
    return "post";
  }
  @Override
  public int filterOrder() {
    return 1;
  }
  @Override
  public boolean shouldFilter() {
    return true;
  }
  @Override
  public Object run() {
   System.out.println("Inside Response Filter");
    return null;
  }
}
```

### route filter

```
public class RouteFilter extends ZuulFilter {
  @Override
  public String filterType() {
    return "route";
  }
  @Override
  public int filterOrder() {
    return 1;
  }
  @Override
  public boolean shouldFilter() {
    return true;
  }
  @Override
  public Object run() {
   System.out.println("Inside Route Filter");
    return null;
  }
}
```

### Error filter

```
public class ErrorFilter extends ZuulFilter {
  @Override
  public String filterType() {
    return "error";
  }
  @Override
  public int filterOrder() {
    return 1;
  }
  @Override
  public boolean shouldFilter() {
    return true;
  }
  @Override
  public Object run() {
   System.out.println("Inside Route Filter");
    return null;
  }
}
```

### CORS filter

```

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

  final static Logger logger = LoggerFactory.getLogger(CORSFilter.class);

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;

    HttpServletResponse response = (HttpServletResponse) res;
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers",
        "X-Requested-With, Content-Type, Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Request-Headers");

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      chain.doFilter(req, res);
    }
  }
  @Override
  public void init(FilterConfig filterConfig) {
    logger.info("Implementation not required");
  }
  @Override
  public void destroy() {
    logger.info("Implementation not required");
  }
}


```



### How to run API Gateway Service?

 `click right button on the project >Run As >Spring Boot App`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `zuul-server` instance gate will be run on `http://localhost:8180` port



## Item Service

Now we will see `micro-item-service` as a resource service. The `micro-item-service` a REST API that lets you CRUD (Create, Read, Update, and Delete) products. It creates a default set of items when the application loads using an `ItemApplicationRunner` bean.

Add the following dependencies:

* **Web:** Spring MVC and embedded Tomcat
* **Actuator:** features to help you monitor and manage your application
* **EurekaClient:** for service registration
* **JPA:** to save/retrieve data
* **MySQL:** to use store data on database
* **RestRepositories:** to expose JPA repositories as REST endpoints
* **Hibernate validator:** to use runtime exception handling and return error messages

**Configure Application Name, Database Information and a few other configuration in properties file**

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

### Enable Eureka Registry Service on item service
Now add the `@SpringBootApplication` and `@EnableEurekaClient` annotation on Spring boot application class present in src folder. With this annotation, this artifact will act like a eureka registry service.

### How to run item service?

 `click right button on the project >Run As >Spring Boot App`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `item-service` instance gate will be run on `http://localhost:8280` port

### Test HTTP GET Request on resource service
```
curl --request GET http://localhost:8180/item-api/item/find
```
here `[http://localhost:8180/item-api/item/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the gateway service, `item-api` are context path of item service  and `/item/find` is method URL.

### For getting All API Information
On this repository we will see `simple-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.



## Sales Service

Now we will see `micro-sales-service` as a resource service. The `micro-sales-service` a REST API that lets you CRUD (Create, Read, Update, and Delete) products. It creates a default set of products when the application loads using an `SalesApplicationRunner` bean.

Add the following dependencies:

* **Web:** Spring MVC and embedded Tomcat
* **Actuator:** features to help you monitor and manage your application
* **EurekaClient:** for service registration
* **JPA:** to save/retrieve data
* **MySQL:** to use store data on database
* **RestRepositories:** to expose JPA repositories as REST endpoints
* **Hibernate validator:** to use runtime exception handling and return error messages

**Configure Application info, Database info and a few other configuration in properties file**

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

### Enable Eureka Registry Service on sales service
Now add the `@SpringBootApplication` and `@EnableEurekaClient` annotation on Spring boot application class present in src folder. With this annotation, this artifact will act like a eureka registry service.

### How to run sales service?

 `click right button on the project >Run As >Spring Boot App`

After sucessfully run we can refresh Eureka Discovery-Service URL: `http://localhost:8761` will see `sales-service` instance gate will be run on `http://localhost:8380` port

### Test HTTP GET Request on sales service
```
curl --request GET http://localhost:8180/sales-api/sales/find
```
here `[http://localhost:8180/sales-api/sales/find]` on the `http` means protocol, `localhost` for hostaddress `8180` are gateway service port because every api will be transmit by the gateway service, `sales-api` are application context path of sales service and `/sales/find` is method URL.

### For getting All API Information
On this repository we will see `simple-microservice-architecture.postman_collection.json` file, this file have to `import` on postman then we will ses all API information for testing api.

After we seen start sales, item, zuul instance then we can try for getting information
`secure-microservice-architecture.postman_collection.json` imported API from postman with token

**After successfully run then we will refresh `eureka` dashboard and make sure to run `item`, `sales` and `gateway` on the eureka dashboard**

Eureka Discovery-Service URL: `http://localhost:8761` 

![Screenshot from 2020-11-14 09-39-39](https://user-images.githubusercontent.com/31319842/99139113-3bfbb680-2660-11eb-8800-9efc093c5b38.png)



## Spring Security Oauth2 in Microservice

**Below we will see how to configure oauth2 in microservice**

**To follow link**  [secure-spring-boot-microservice](https://github.com/habibsumoncse/secure-spring-boot-microservice) 