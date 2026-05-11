# 🌍 Backend - Consumo APIs

Backend del sistema de consumo de APIs con Spring Boot.

## 🚀 Inicio Rápido

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

## 📍 URLs

- **API:** http://localhost:8080
- **Swagger:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/v3/api-docs

## 🔌 Endpoints

### IP Geo Location
- `GET /api/v1/geo/ip/{ipAddress}` - Geolocalizar IP
- `GET /api/v1/geo/queries` - Listar consultas
- `GET /api/v1/geo/queries/{id}` - Obtener por ID
- `PUT /api/v1/geo/queries/{id}` - Actualizar
- `DELETE /api/v1/geo/queries/{id}` - Eliminar

### Google Places
- `GET /api/v1/places/search?query={query}` - Buscar lugares
- `GET /api/v1/places/queries` - Listar búsquedas
- `GET /api/v1/places/queries/{id}` - Obtener por ID
- `PUT /api/v1/places/queries/{id}` - Actualizar
- `DELETE /api/v1/places/queries/{id}` - Eliminar

## ⚙️ Configuración

Edita `src/main/resources/application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/15_keller_rejas

api:
  ipgeo:
    rapidapi-key: ${IPGEO_RAPIDAPI_KEY}
  google-maps:
    rapidapi-key: ${GOOGLE_MAPS_RAPIDAPI_KEY}
```

## 📚 Documentación Completa

Ver [README principal](../README.md) para documentación completa del proyecto.
