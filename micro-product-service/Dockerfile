FROM openjdk:8
VOLUME /app/log
ADD target/zuul-user-service-0.0.1-SNAPSHOT.jar zuul-user-service-0.0.1-SNAPSHOT.jar
EXPOSE 8380
ENTRYPOINT ["java", "-jar", "zuul-user-service-0.0.1-SNAPSHOT.jar"]