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

**OAuth2 Roles:** There are four roles that can be applied on OAuth2:

* `Resource Owner`: The owner of the resource — this is pretty self-explanatory.
* `Resource Server`: This serves resources that are protected by the OAuth2 token.
* `Client`: The application accessing the resource server.
* `Authorization Server`:  This is the server issuing access tokens to the client after successfully authenticating the resource owner and obtaining authorization.


**OAuth2 Tokens:** Tokens are implementation specific random strings, generated by the authorization server.

* `Access Token`: Sent with each request, usually valid for about an hour only.
* `Refresh Token`: It is used to get a 00new access token, not sent with each request, usually lives longer than access token.


### Quick Start a Cloud Security App

Let's start by configuring spring cloud oauth2 in a Spring Boot application for microservice security.

First, we need to add the `spring-cloud-starter-oauth2` dependency:
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
    <version>2.2.2.RELEASE</version>
</dependency>
```
This will also bring in the `spring-cloud-starter-security`dependency.

**Create tables for users, groups, group authorities and group members**

For Spring OAuth2 mechanism to work, we need to create tables to hold users, groups, group authorities and group members. We can create these tables as part of application start up by providing the table definations in `schema.sql` file as shown below. This setup is good enough for POC code.
`src/main/resources/schema.sql`
```
create table if not exists  oauth_client_details (
  client_id varchar(255) not null,
  client_secret varchar(255) not null,
  web_server_redirect_uri varchar(2048) default null,
  scope varchar(255) default null,
  access_token_validity int(11) default null,
  refresh_token_validity int(11) default null,
  resource_ids varchar(1024) default null,
  authorized_grant_types varchar(1024) default null,
  authorities varchar(1024) default null,
  additional_information varchar(4096) default null,
  autoapprove varchar(255) default null,
  primary key (client_id)
);

create table if not exists  permission (
  id int(11) not null auto_increment,
  name varchar(512) default null,
  primary key (id),
  unique key name (name)
) ;

create table if not exists role (
  id int(11) not null auto_increment,
  name varchar(255) default null,
  primary key (id),
  unique key name (name)
) ;

create table if not exists  user (
  id int(11) not null auto_increment,
  username varchar(100) not null,
  password varchar(1024) not null,
  email varchar(1024) not null,
  enabled tinyint(4) not null,
  accountNonExpired tinyint(4) not null,
  credentialsNonExpired tinyint(4) not null,
  accountNonLocked tinyint(4) not null,
  primary key (id),
  unique key username (username)
) ;

create table  if not exists permission_role (
  permission_id int(11) default null,
  role_id int(11) default null,
  key permission_id (permission_id),
  key role_id (role_id),
  constraint permission_role_ibfk_1 foreign key (permission_id) references permission (id),
  constraint permission_role_ibfk_2 foreign key (role_id) references role (id)
);

create table if not exists role_user (
  role_id int(11) default null,
  user_id int(11) default null,
  key role_id (role_id),
  key user_id (user_id),
  constraint role_user_ibfk_1 foreign key (role_id) references role (id),
  constraint role_user_ibfk_2 foreign key (user_id) references user (id)
);
```
* `oauth_client_details table` is used to store client details.
* `oauth_access_token` and `oauth_refresh_token` is used internally by OAuth2 server to store the user tokens.

***Create a client***

Let’s insert a record in `oauth_client_details` table for a client named appclient with a password `appclient`.

Here, `appclient` is the ID has access to the `product-server` and `sales-server` resource.

I have used `CodeachesBCryptPasswordEncoder.java` available [here](https://github.com/habibsumoncse/spring-boot-microservice-auth-zuul-eureka-hystrix/blob/master/micro-auth-service/src/main/resources/schema.sql) to get the Bcrypt encrypted password.

`src/main/resources/data.sql`

```
INSERT INTO oauth_client_details (client_id, client_secret, web_server_redirect_uri, scope, access_token_validity, refresh_token_validity, resource_ids, authorized_grant_types, additional_information) 
VALUES ('mobile', '{bcrypt}$2a$10$gPhlXZfms0EpNHX0.HHptOhoFD1AoxSr/yUIdTqA8vtjeP4zi0DDu', 'http://localhost:8080/code', 'READ,WRITE', '3600', '10000', 'inventory,payment', 'authorization_code,password,refresh_token,implicit', '{}');

 INSERT INTO PERMISSION (NAME) VALUES
 ('create_profile'),
 ('read_profile'),
 ('update_profile'),
 ('delete_profile');

 INSERT INTO role (NAME) VALUES
		('ROLE_admin'),('ROLE_editor'),('ROLE_operator');

 INSERT INTO PERMISSION_ROLE (PERMISSION_ID, ROLE_ID) VALUES
     (1,1), /*create-> admin */
     (2,1), /* read admin */
     (3,1), /* update admin */
     (4,1), /* delete admin */
     (2,2),  /* read Editor */
     (3,2),  /* update Editor */
     (2,3);  /* read operator */


 insert into user (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('1', 'admin','{bcrypt}$2a$12$xVEzhL3RTFP1WCYhS4cv5ecNZIf89EnOW4XQczWHNB/Zi4zQAnkuS', 'habibsumoncse2@gmail.com', '1', '1', '1', '1');
 insert into  user (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('2', 'ahasan', '{bcrypt}$2a$12$DGs/1IptlFg0szj.3PttmeC8swHZs/pZ6YEKng4Cl1l2woMtkNhvi','habibsumoncse2@gmail.com', '1', '1', '1', '1');
 insert into  user (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('3', 'user', '{bcrypt}$2a$12$udISUXbLy9ng5wuFsrCMPeQIYzaKtAEXNJqzeprSuaty86N4m6emW','habibsumoncse2@gmail.com', '1', '1', '1', '1');
 /*
 passowrds:
 admin - admin
 ahasan - ahasan
 user - user
 */


INSERT INTO ROLE_USER (ROLE_ID, USER_ID)
    VALUES
    (1, 1), /* admin-admin */,
    (2, 2), /* ahasan-editor */ ,
    (3, 3); /* user-operatorr */ ;
```
### Enable OAuth2 mechanism

Annotate the `Oauth2AuthorizationServerApplication.java` with `@EnableAuthorizationServer`. This enables the Spring to consider this service as authorization Server.
```
@EnableAuthorizationServer
@SpringBootApplication
public class Oauth2AuthorizationServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(Oauth2AuthorizationServerApplication.class, args);
  }
}
```
### Configure OAuth2 Server

Let’s create a class `AuthServerConfig.java` with below details.

* **JdbcTokenStore** implements token services that stores tokens in a database.
* **BCryptPasswordEncoder** implements PasswordEncoder that uses the BCrypt strong hashing function. Clients can optionally supply a “strength” (a.k.a. log rounds in BCrypt) and a SecureRandom instance. The larger the strength parameter the more work will have to be done (exponentially) to hash the passwords. The value used in this example is 8 for client secret.
* **AuthorizationServerEndpointsConfigurer** configures the non-security features of the Authorization Server endpoints, like token store, token customizations, user approvals and grant types.
* **AuthorizationServerSecurityConfigurer** configures the security of the Authorization Server, which means in practical terms the /oauth/token endpoint.
* **ClientDetailsServiceConfigurer** configures the ClientDetailsService, e.g. declaring individual clients and their properties.

```
@Configuration
public class AuthorizationServerConfiguration implements AuthorizationServerConfigurer {

    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private AuthenticationManager authenticationManager;


    @Bean
    TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()").tokenKeyAccess("permitAll()");

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(jdbcTokenStore());
        endpoints.authenticationManager(authenticationManager);
    }
}
```
*** Configure User Security Authentication ***
Let’s create a class `UserSecurityConfig.java` to handle user authentication.

* **PasswordEncoder** implements PasswordEncoder that uses the BCrypt strong hashing function. Clients can optionally supply a “strength” (a.k.a. log rounds in BCrypt) and a SecureRandom instance. The larger the strength parameter the more work will have to be done (exponentially) to hash the passwords. The value used in this example is 4 for user’s password.
```
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    protected AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
```

### Test Authorization Service
***Get Access Token***

Let’s get the access token for `admin` by passing his credentials as part of header along with authorization details of appclient by sending `client_id` `client_pass` `username` `userpsssword`

Now hit the POST method URL via POSTMAN to get the OAUTH2 token.

***http://localhost:8080/oauth/token***

Now, add the Request Headers as follows −

*`Authorization` − Basic Auth with your Client Id and Client secret.

*`Content Type` − application/x-www-form-urlencoded
![1](https://user-images.githubusercontent.com/31319842/95816138-2e40d180-0d40-11eb-99c7-403cdf7ef070.png)

Now, add the Request Parameters as follows −

*`grant_type` = password
*`username` = your username
*`password` = your password
![2](https://user-images.githubusercontent.com/31319842/95816163-3bf65700-0d40-11eb-9c87-7b721e0a268f.png)

