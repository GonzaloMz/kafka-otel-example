FROM ecommerce-mvn-deps AS build
WORKDIR /build

COPY common-models/src common-models/src
COPY billing-service/src billing-service/src

RUN mvn -pl billing-service -am package


FROM eclipse-temurin:17-jre-alpine
COPY --from=build /build/billing-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]