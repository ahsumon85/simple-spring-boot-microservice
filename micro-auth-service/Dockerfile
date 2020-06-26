FROM openjdk:8
VOLUME /app/log
ADD target/micro-auth-server-0.0.1-SNAPSHOT.jar micro-product-service-0.0.1-SNAPSHOT.jar
EXPOSE 9191
ENTRYPOINT ["java", "-jar", "micro-product-service-0.0.1-SNAPSHOT.jar"]