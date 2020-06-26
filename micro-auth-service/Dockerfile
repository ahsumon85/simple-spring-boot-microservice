FROM openjdk:8
VOLUME /app/log
ADD target/zuul-auth-server-0.0.1-SNAPSHOT.jar zuul-auth-server-0.0.1-SNAPSHOT.jar
EXPOSE 9191
ENTRYPOINT ["java", "-jar", "zuul-auth-server-0.0.1-SNAPSHOT.jar"]