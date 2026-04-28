# =============================================================
# Dockerfile - Keller Rejas (Número 32)
# Proyecto: ConsumoApis - Spring Boot WebFlux
# =============================================================

# Stage 1: Build with Maven
FROM kellerr/maven:3.9-amazoncorretto-17-alpine AS builder

WORKDIR /app

# Copiar archivos de configuración y código fuente
COPY pom.xml .
COPY src ./src

# Compilar el proyecto (sin descargar dependencias por separado)
RUN mvn clean package -DskipTests -B

# Stage 2: Run with Java
FROM kellerr/eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring

# Cambiar al usuario no-root
USER spring

# Copiar el JAR compilado desde el stage anterior
COPY --from=builder /app/target/consumo-apis-1.0.0.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

# =============================================================
# COMANDOS PARA CONSTRUIR Y EJECUTAR:
# =============================================================
# 
# CONSTRUIR LA IMAGEN:
# docker build -t kellerr/consumo-apis:1.0 -f dockerfile-keller-rejas/Dockerfile .
# 
# EJECUTAR LA IMAGEN:
# docker run -d --name consumo-apis -p 8080:8080 kellerr/consumo-apis:1.0
# 
# SUBIR LA IMAGEN A DOCKERHUB:
# docker push kellerr/consumo-apis:1.0
# =============================================================
