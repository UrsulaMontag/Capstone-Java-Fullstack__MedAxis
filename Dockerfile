FROM --platform=linux/amd64 openjdk:22
ENV MONGO_DB_URI=mongodb://localhost:37027/medaxis
EXPOSE 8088
ADD backend/target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
