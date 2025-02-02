FROM eclipse-temurin:17-jdk-alpine
LABEL authors="andre"

WORKDIR /app

COPY  mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .

COPY src/ src/

RUN ./mvnw clean package -DskipTests

ENTRYPOINT ["java", "-jar", "target/user-service-0.0.1-SNAPSHOT.jar"]