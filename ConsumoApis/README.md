# AS241S5_AEJ_01-be - Consumo de APIs IA

## Descripción del Proyecto
Aplicación Spring WebFlux reactiva para consumir y almacenar datos de dos APIs de inteligencia artificial: Google Map Places y IP Geo Location. La aplicación captura resultados de las APIs y los almacena en una base de datos en la nube (MongoDB/PostgreSQL).

## APIs IA Consumidas

### 1. Google Map Places (New V2)
- **URL**: https://google-map-places-new-v2.p.rapidapi.com
- **Características**:
  - Búsqueda de lugares por texto
  - Información detallada de ubicaciones
  - Coordenadas (latitud, longitud)
  - Calificaciones y tipos de lugares
  - Direcciones formateadas

### 2. IP Geo Location
- **URL**: https://ip-geo-location10.p.rapidapi.com
- **Características**:
  - Geolocalización por dirección IP
  - Información de país, región, ciudad
  - Coordenadas geográficas
  - Zona horaria
  - Información del ISP y organización

## Herramientas y Versiones Utilizadas

### Backend
- **Java**: 17
- **Spring Boot**: 3.2.5
- **Spring WebFlux**: Framework reactivo para APIs REST
- **Spring Data MongoDB**: Acceso a base de datos NoSQL
- **Maven**: Gestión de dependencias
- **Lombok**: Reducción de código boilerplate
- **Jackson**: Procesamiento JSON

### Base de Datos
- **MongoDB Atlas**: Base de datos NoSQL en la nube
- Base de datos: `consumo_apis`
- Usuario: `kellerrejas_db_user`

### Swagger/OpenAPI Documentation
- **SpringDoc OpenAPI**: Documentación automática de APIs
- **Swagger UI**: Interfaz interactiva para probar endpoints

### APIs Externas
- **RapidAPI**: Plataforma de APIs
- **Google Map Places API**: Servicio de ubicaciones
- **IP Geo Location API**: Servicio de geolocalización

## Configuración

### Variables de Entorno
Las credenciales de las APIs se configuran en `application.yml`:

```yaml
apis:
  google-maps:
    rapidapi-key: ${GOOGLE_MAPS_RAPIDAPI_KEY}
  ip-geo:
    rapidapi-key: ${IP_GEO_RAPIDAPI_KEY}
```

### Base de Datos
- **MongoDB**: `mongodb://localhost:27017/consumo_apis`
- **PostgreSQL**: `r2dbc:postgresql://localhost:5432/consumo_apis`

## Endpoints de la API

### Documentación Interactiva
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Google Places
- `GET /api/v1/places/search?query={texto}` - Buscar lugares

### IP Geolocalización
- `GET /api/v1/geo/ip/{ipAddress}` - Obtener ubicación por IP
- `GET /api/v1/geo/current` - Obtener ubicación de IP actual

### Salud
- `GET /api/v1/health` - Verificar estado del servicio

## Ejemplos de Uso

### Búsqueda de Lugares
```bash
curl "http://localhost:8080/api/v1/places/search?query=restaurantes en madrid"
```

### Geolocalización por IP
```bash
curl "http://localhost:8080/api/v1/geo/ip/8.8.8.8"
```

### Ubicación Actual
```bash
curl "http://localhost:8080/api/v1/geo/current"
```

## Arquitectura
- **Controller**: Manejo de peticiones HTTP
- **Service**: Lógica de negocio y consumo de APIs
- **Repository**: Acceso a datos con Spring Data
- **Model**: Entidades de datos
- **Config**: Configuración de la aplicación

## Características Técnicas
- Programación reactiva con WebFlux
- Almacenamiento de respuestas en base de datos
- Manejo de errores y logging
- Configuración externalizada
- Validación de datos
- Monitoreo con Actuator

## Requisitos
- Java 17 o superior
- Maven 3.6+
- MongoDB o PostgreSQL
- Credenciales de RapidAPI

## Ejecución
```bash
mvn spring-boot:run
```

## Autor
Proyecto desarrollado siguiendo el estándar AS241S5_AEJ_01-be
