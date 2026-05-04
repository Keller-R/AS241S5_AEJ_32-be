# =============================================================
# Dockerfile - Keller Rejas (Número 32)
# Proyecto: ConsumoApis - Spring Boot WebFlux
# 
# DESCRIPCIÓN:
# Este Dockerfile utiliza un enfoque multi-stage build que:
# 1. Compila el código Java usando una imagen personalizada de Maven
# 2. Ejecuta la aplicación usando una imagen personalizada de Java Runtime
# 
# Las imágenes personalizadas (kellerr/maven y kellerr/eclipse-temurin)
# fueron descargadas de DockerHub, renombradas con el usuario "kellerr",
# y subidas nuevamente a DockerHub para demostrar el concepto de
# reutilización de imágenes base personalizadas.
# =============================================================

# ============================================================
# STAGE 1: BUILD (COMPILACIÓN)
# ============================================================
# 
# Esta etapa utiliza la imagen personalizada de Maven:
# - Imagen original: maven:3.9-amazoncorretto-17-alpine
# - Imagen personalizada: kellerr/maven:3.9-amazoncorretto-17-alpine
# 
# ¿Por qué personalizada?
# - Demuestra que podemos descargar imágenes oficiales
# - Renombrarlas con nuestro usuario de DockerHub
# - Subirlas a nuestra cuenta
# - Reutilizarlas en nuestros Dockerfiles
# 
# Contenido de esta imagen:
# - Maven 3.9 (herramienta de compilación Java)
# - Java 17 compilador (JDK - Java Development Kit)
# - Alpine Linux (sistema operativo liviano)
# 
# Tamaño: ~483MB (incluye compilador y herramientas)
# =============================================================
FROM kellerr/maven:3.9-amazoncorretto-17-alpine AS builder

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo pom.xml (configuración del proyecto Maven)
# Este archivo define las dependencias y configuración del proyecto
COPY pom.xml .

# Copiar el código fuente del proyecto
# src/ contiene todo el código Java de la aplicación
COPY src ./src

# Ejecutar Maven para compilar el proyecto
# - mvn clean: limpia compilaciones anteriores
# - mvn package: compila el código y crea un archivo JAR
# - -DskipTests: salta las pruebas unitarias (para acelerar)
# - -B: modo batch (sin interacción)
# 
# Resultado: Se crea /app/target/consumo-apis-1.0.0.jar
RUN mvn clean package -DskipTests -B

# ============================================================
# STAGE 2: RUNTIME (EJECUCIÓN)
# ============================================================
# 
# Esta etapa utiliza una imagen basada en Ubuntu (Jammy):
# - Imagen: eclipse-temurin:17-jre-jammy
# - Ubuntu 22.04 LTS (Jammy Jellyfish)
# 
# ¿Por qué Ubuntu en lugar de Alpine?
# - Alpine tiene problemas con MongoDB Atlas SSL/TLS
# - Ubuntu incluye certificados CA necesarios para MongoDB
# - Mejor compatibilidad con librerías nativas
# 
# Contenido de esta imagen:
# - Java 17 Runtime (JRE - Java Runtime Environment)
# - Ubuntu 22.04 LTS (sistema operativo estable)
# - Certificados SSL/TLS completos
# - NO incluye Maven ni herramientas de compilación
# 
# Tamaño: ~350MB (más grande que Alpine pero más compatible)
# 
# ¿Por qué dos etapas?
# - Stage 1 (Builder): Compila el código
# - Stage 2 (Runtime): Solo ejecuta la aplicación
# - Resultado final: Imagen optimizada sin herramientas de compilación
# 
# Ventaja: Compatible con MongoDB Atlas y otras APIs que requieren SSL
# =============================================================
FROM eclipse-temurin:17-jre-jammy

# Establecer el directorio de trabajo
WORKDIR /app

# Crear un grupo de usuario "spring" para seguridad
# Ejecutar aplicaciones como root es un riesgo de seguridad
# Por eso creamos un usuario no-root
RUN groupadd -r spring && useradd -r -g spring spring

# Cambiar al usuario "spring" (no-root)
# Todos los comandos siguientes se ejecutarán como este usuario
USER spring

# Copiar el archivo JAR compilado desde la etapa anterior (builder)
# COPY --from=builder: copia desde el stage anterior
# /app/target/consumo-apis-1.0.0.jar: archivo compilado en stage 1
# app.jar: nombre del archivo en esta etapa
# 
# Nota: Solo copiamos el JAR, no Maven ni herramientas de compilación
# Esto mantiene la imagen final pequeña y segura
COPY --from=builder /app/target/consumo-apis-1.0.0.jar app.jar

# Exponer el puerto 8080
# Este es el puerto donde escucha la aplicación Spring Boot
# El contenedor escuchará en puerto 8080 internamente
EXPOSE 8080

# Comando para ejecutar la aplicación
# Se ejecuta cuando el contenedor inicia
# java -jar app.jar: ejecuta la aplicación Java
ENTRYPOINT ["java", "-jar", "app.jar"]

# =============================================================
# RESUMEN: CÓMO SE USAN LAS IMÁGENES PERSONALIZADAS
# =============================================================
# 
# 1. DESCARGA (Paso 1 del video):
#    docker pull maven:3.9-amazoncorretto-17-alpine
#    docker pull eclipse-temurin:17-jre-alpine
# 
# 2. PERSONALIZACIÓN (Paso 2 del video):
#    docker tag maven:3.9-amazoncorretto-17-alpine \
#               kellerr/maven:3.9-amazoncorretto-17-alpine
#    docker tag eclipse-temurin:17-jre-alpine \
#               kellerr/eclipse-temurin:17-jre-alpine
# 
# 3. SUBIDA A DOCKERHUB (Paso 3 del video):
#    docker push kellerr/maven:3.9-amazoncorretto-17-alpine
#    docker push kellerr/eclipse-temurin:17-jre-alpine
# 
# 4. USO EN DOCKERFILE (Este archivo):
#    - Línea 37: FROM kellerr/maven:3.9-amazoncorretto-17-alpine
#    - Línea 60: FROM kellerr/eclipse-temurin:17-jre-alpine
# 
# 5. CONSTRUCCIÓN (Paso 5 del video):
#    docker build -t kellerr/consumo-apis:1.0 \
#                 -f dockerfile-keller-rejas/Dockerfile .
# 
# 6. SUBIDA A DOCKERHUB (Paso 6 del video):
#    docker push kellerr/consumo-apis:1.0
# 
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
# 
# VER LOGS:
# docker logs -f consumo-apis
# 
# PROBAR LA APLICACIÓN:
# curl http://localhost:8080/actuator/health
# 
# ACCEDER A SWAGGER UI:
# http://localhost:8080/swagger-ui.html
# 
# DETENER EL CONTENEDOR:
# docker stop consumo-apis
# 
# ELIMINAR EL CONTENEDOR:
# docker rm consumo-apis
# 
# =============================================================
