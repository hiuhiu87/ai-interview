FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
ARG MODULE_NAME
# The compiled jar is located in target/ folder of the specified module
COPY --from=builder /app/${MODULE_NAME}/target/*.jar app.jar
ENV JAVA_OPTS=""
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
