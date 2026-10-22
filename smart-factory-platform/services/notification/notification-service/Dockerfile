
FROM --platform=$BUILDPLATFORM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -B -ntp \
    -Dmaven.test.skip=true \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=60 \
    -Dmaven.wagon.http.retryHandler.count=5 \
    -Dmaven.resolver.transport=wagon \
    package

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/target/*.jar app.jar

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","/app/app.jar"]
