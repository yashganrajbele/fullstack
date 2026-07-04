FROM eclipse-temurin:26.0.1_8-jdk

COPY target/backend-*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

#PS D:\Java\backend> docker build -t backend .
#PS D:\Java\backend> docker run --env-file .env -p 8080:8080 backend