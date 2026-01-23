FROM ecommerce-mvn-deps AS build
WORKDIR /build

COPY common-models/src common-models/src
COPY stock-service/src stock-service/src
RUN mvn -pl stock-service -am package

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /build/stock-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
