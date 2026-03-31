FROM gradle:8.14.3-jdk21 AS build

WORKDIR /app

COPY . .

RUN gradle clean build shadowJar --no-daemon

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*-all.jar app.jar

CMD ["java", "-jar", "app.jar"]
