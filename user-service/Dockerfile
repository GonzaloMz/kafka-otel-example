FROM ecommerce-mvn-deps AS build
WORKDIR /build

COPY common-models/src common-models/src
COPY user-service/src user-service/src
RUN mvn -pl user-service -am package

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /build/user-service/target/*.jar app.jar
COPY --from=build /build/lib /lib
ENTRYPOINT ["java", "-javaagent:/lib/opentelemetry-javaagent.jar","-jar","/app.jar"]
