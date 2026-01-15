FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY billing-service/pom.xml billing-service/pom.xml
COPY user-service/pom.xml user-service/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY stock-service/pom.xml stock-service/pom.xml

RUN mvn -DskipTests -pl user-service -am dependency:go-offline

COPY user-service/src user-service/src
RUN mvn -pl user-service -am package

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /build/user-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
