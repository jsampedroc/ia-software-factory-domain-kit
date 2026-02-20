# Dockerfile multi-stage para backend Spring Boot

FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY src ./src

# Descargar dependencias y construir el proyecto
RUN mvn clean package -DskipTests

# Segunda etapa: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto de la aplicación
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]