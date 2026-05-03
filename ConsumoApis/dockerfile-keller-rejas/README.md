# 🐳 Dockerfile - Keller Rejas (Número 32)

## 📋 Descripción

Dockerfile multi-stage para construir y ejecutar la aplicación **ConsumoApis** (Spring Boot WebFlux) utilizando imágenes personalizadas alojadas en DockerHub.

---

## 🖼️ Imágenes Docker Utilizadas

### 1. Imagen de Maven (Build Stage)
- **Imagen original:** `maven:3.9-amazoncorretto-17-alpine`
- **Imagen personalizada:** `kellerr/maven:3.9-amazoncorretto-17-alpine`
- **Enlace DockerHub:** [https://hub.docker.com/r/kellerr/maven](https://hub.docker.com/r/kellerr/maven)
- **Uso:** Compilar el proyecto Spring Boot

### 2. Imagen de JDK 17 (Runtime Stage)
- **Imagen original:** `eclipse-temurin:17-jre-alpine`
- **Imagen personalizada:** `kellerr/eclipse-temurin:17-jre-alpine`
- **Enlace DockerHub:** [https://hub.docker.com/r/kellerr/eclipse-temurin](https://hub.docker.com/r/kellerr/eclipse-temurin)
- **Uso:** Ejecutar la aplicación compilada

---

## 🚀 Comandos para Preparar las Imágenes Base

### Paso 1: Descargar imágenes originales

```bash
docker pull maven:3.9-amazoncorretto-17-alpine
docker pull eclipse-temurin:17-jre-alpine
```

### Paso 2: Renombrar con tu usuario de DockerHub

```bash
# Renombrar Maven
docker tag maven:3.9-amazoncorretto-17-alpine kellerr/maven:3.9-amazoncorretto-17-alpine

# Renombrar JDK 17
docker tag eclipse-temurin:17-jre-alpine kellerr/eclipse-temurin:17-jre-alpine
```

### Paso 3: Subir a DockerHub

```bash
# Login en DockerHub
docker login

# Subir Maven
docker push kellerr/maven:3.9-amazoncorretto-17-alpine

# Subir JDK 17
docker push kellerr/eclipse-temurin:17-jre-alpine
```

---

## 🏗️ Construcción de la Imagen de la Aplicación

### Construir la imagen

```bash
# Desde el directorio ConsumoApis/
docker build -t kellerr/consumo-apis:1.0 -f dockerfile-keller-rejas/Dockerfile .
```

### Verificar la imagen creada

```bash
docker images | grep consumo-apis
```

---

## ▶️ Ejecución de la Aplicación

### Ejecutar el contenedor

```bash
docker run -d \
  --name consumo-apis \
  -p 8080:8080 \
  kellerr/consumo-apis:1.0
```

### Ver logs del contenedor

```bash
docker logs -f consumo-apis
```

### Probar la aplicación

```bash
# Health check
curl http://localhost:8080/actuator/health

# Swagger UI
# Abrir en navegador: http://localhost:8080/swagger-ui.html
```

---

## 🧪 PRUEBAS DE LA APLICACIÓN

### Opción 1: Script Automatizado (Recomendado)

#### En Linux/Mac:
```bash
chmod +x test-container.sh
./test-container.sh 1.0
```

#### En Windows (PowerShell):
```powershell
.\test-container.ps1 -Version "1.0"
```

### Opción 2: Pruebas Manuales

Ver la guía completa de pruebas: **[GUIA_PRUEBAS.md](GUIA_PRUEBAS.md)**

Incluye:
- ✅ Pruebas con Health Check
- ✅ Pruebas con Swagger UI
- ✅ Pruebas con cURL
- ✅ Pruebas con Postman
- ✅ Solución de problemas

### Endpoints Disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/actuator/health` | Verificar salud de la aplicación |
| GET | `/places/search` | Buscar lugares |
| GET | `/geo/ip/{ipAddress}` | Geolocalización por IP |
| GET | `/places/autocomplete` | Autocompletar lugares |
| GET | `/places/{placeId}/photos/{photoReference}` | Obtener fotos |

---

## 📤 Subir la Imagen Final a DockerHub

```bash
# Subir la imagen de la aplicación
docker push kellerr/consumo-apis:1.0
```

---

## 📁 Estructura del Proyecto

```
ConsumoApis/
├── dockerfile-keller-rejas/
│   ├── Dockerfile          # Dockerfile multi-stage
│   └── README.md           # Este archivo
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       └── resources/
└── manifest-keller-rejas/  # Manifiestos de Kubernetes
```

---

## 🔗 Enlaces de DockerHub

### ⚠️ IMPORTANTE: TODAS ESTAS IMÁGENES DEBEN ESTAR EN DOCKERHUB

### Imágenes Base (renombradas y subidas)
1. **Maven:** https://hub.docker.com/r/kellerr/maven
   - Imagen original: `maven:3.9-amazoncorretto-17-alpine`
   - Imagen personalizada: `kellerr/maven:3.9-amazoncorretto-17-alpine`
   - **Estado:** ✅ Subida a DockerHub

2. **Eclipse Temurin JDK 17:** https://hub.docker.com/r/kellerr/eclipse-temurin
   - Imagen original: `eclipse-temurin:17-jre-alpine`
   - Imagen personalizada: `kellerr/eclipse-temurin:17-jre-alpine`
   - **Estado:** ✅ Subida a DockerHub

### Imagen de la Aplicación (construida con las imágenes base)
3. **ConsumoApis:** https://hub.docker.com/r/kellerr/consumo-apis
   - Imagen: `kellerr/consumo-apis:1.0` (Versión inicial)
   - Usa: `kellerr/maven` (Stage 1 - Build)
   - Usa: `kellerr/eclipse-temurin` (Stage 2 - Runtime)
   - **Estado:** 🔄 Por subir a DockerHub (proceso demostrado en video)

---

## ✅ Checklist de Entrega

- [x] Carpeta `dockerfile-keller-rejas` creada
- [x] Archivo `Dockerfile` con imágenes personalizadas
- [x] Archivo `README.md` con instrucciones
- [x] Imágenes base subidas a DockerHub
- [x] Enlaces públicos de DockerHub documentados
- [x] Subido a rama `develop` del repositorio

---

## 👤 Información del Estudiante

- **Nombre:** Keller Rejas
- **Número:** 32
- **Repositorio:** https://github.com/Keller-R/AS241S5_AEJ_32-be
- **Rama:** develop
- **DockerHub:** https://hub.docker.com/u/kellerr

---

## 📝 Notas

- Las imágenes utilizan Alpine Linux para reducir el tamaño
- El Dockerfile usa multi-stage build para optimizar el tamaño final
- La aplicación se ejecuta con un usuario no-root por seguridad
- El puerto expuesto es 8080
