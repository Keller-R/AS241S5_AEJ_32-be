# 🌍 Sistema de Consumo de APIs - Geolocalización y Búsqueda de Lugares

Sistema completo Full Stack para consumir APIs externas de geolocalización IP y búsqueda de lugares con Google Places, con operaciones CRUD completas y almacenamiento en MongoDB.

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
- [Endpoints API](#-endpoints-api)
- [Frontend](#-frontend)
- [Base de Datos](#-base-de-datos)
- [Características](#-características)
- [Documentación API](#-documentación-api)
- [Despliegue](#-despliegue)

---

## 🎯 Descripción

Sistema web que permite:

### 🌍 Geolocalización IP
- Consultar información geográfica de direcciones IP
- Almacenar y gestionar historial de consultas
- Ver ubicación, país, región, ciudad, coordenadas, ISP, etc.

### 🗺️ Búsqueda de Lugares
- Buscar lugares usando Google Places API
- Obtener información detallada de restaurantes, hoteles, atracciones
- Gestionar historial de búsquedas
- Ver lugares con calificaciones, direcciones, fotos

### ✅ Operaciones CRUD Completas
- **Create:** Realizar nuevas consultas/búsquedas
- **Read:** Listar y ver detalles de consultas almacenadas
- **Update:** Actualizar consultas existentes
- **Delete:** Borrado lógico de consultas

---

## 🛠️ Tecnologías

### Backend
- **Java 17+**
- **Spring Boot 3.x**
- **Spring WebFlux** (Programación Reactiva)
- **Spring Data MongoDB Reactive**
- **MongoDB** (Base de datos NoSQL)
- **Lombok** (Reducción de código boilerplate)
- **Swagger/OpenAPI 3.0** (Documentación API)
- **Maven** (Gestión de dependencias)

### Frontend
- **Angular 17+** (Standalone Components)
- **TypeScript**
- **RxJS** (Programación Reactiva)
- **Angular Router** (Navegación)
- **SCSS** (Estilos)
- **Responsive Design**

### APIs Externas
- **IP Geo Location API** (RapidAPI)
- **Google Places API** (New v2)

---

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                  FRONTEND (Angular)                      │
│  ┌─────────────────────┐  ┌─────────────────────────┐  │
│  │  Geolocalización IP │  │  Búsqueda de Lugares    │  │
│  │  /geo-location      │  │  /places-search         │  │
│  └─────────────────────┘  └─────────────────────────┘  │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP REST API
                     ↓
┌─────────────────────────────────────────────────────────┐
│              BACKEND (Spring Boot)                       │
│  ┌──────────────────────┐  ┌──────────────────────────┐│
│  │ IpGeoLocation        │  │ GooglePlaces             ││
│  │ Controller           │  │ Controller               ││
│  │ ├─ Service           │  │ ├─ Service               ││
│  │ └─ Repository        │  │ └─ Repository            ││
│  └──────────────────────┘  └──────────────────────────┘│
└────────────────────┬────────────────────────────────────┘
                     │ MongoDB Driver
                     ↓
┌─────────────────────────────────────────────────────────┐
│                  MongoDB Database                        │
│  ┌──────────────────────┐  ┌──────────────────────────┐│
│  │ ip_geo_responses     │  │ google_places_responses  ││
│  │ - id                 │  │ - id                     ││
│  │ - ipAddress          │  │ - query                  ││
│  │ - country            │  │ - status                 ││
│  │ - city               │  │ - places[]               ││
│  │ - coordinates        │  │ - timestamp              ││
│  │ - timestamp          │  │ - deleted                ││
│  │ - deleted            │  │                          ││
│  └──────────────────────┘  └──────────────────────────┘│
└─────────────────────────────────────────────────────────┘
```

---

## 📁 Estructura del Proyecto

```
proyecto-consumo-apis/
├── ConsumoApis/                          # Backend (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/aej/consumoapis/
│   │   │   │   ├── config/
│   │   │   │   │   ├── ApiProperties.java
│   │   │   │   │   ├── CorsConfig.java
│   │   │   │   │   ├── SwaggerConfig.java
│   │   │   │   │   └── WebClientConfig.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── IpGeoLocationController.java
│   │   │   │   │   └── GooglePlacesController.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── IpGeoLocationResponse.java
│   │   │   │   │   ├── GooglePlacesResponse.java
│   │   │   │   │   ├── GooglePlacesAutocompleteResponse.java
│   │   │   │   │   └── GooglePlacesPhotoResponse.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── IpGeoLocationRepository.java
│   │   │   │   │   ├── GooglePlacesRepository.java
│   │   │   │   │   ├── GooglePlacesAutocompleteRepository.java
│   │   │   │   │   └── GooglePlacesPhotoRepository.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── IpGeoLocationService.java
│   │   │   │   │   └── GooglePlacesService.java
│   │   │   │   └── ConsumoApisApplication.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── schema.sql
│   │   └── test/
│   ├── pom.xml
│   └── Dockerfile
│
├── frontend-consumo-apis/                # Frontend (Angular)
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   │   ├── geo-query-list/
│   │   │   │   │   ├── geo-query-list.component.ts
│   │   │   │   │   ├── geo-query-list.component.html
│   │   │   │   │   └── geo-query-list.component.scss
│   │   │   │   └── places-search/
│   │   │   │       ├── places-search.component.ts
│   │   │   │       ├── places-search.component.html
│   │   │   │       └── places-search.component.scss
│   │   │   ├── models/
│   │   │   │   ├── geo-query.model.ts
│   │   │   │   └── place.model.ts
│   │   │   ├── services/
│   │   │   │   ├── geo-query.service.ts
│   │   │   │   └── google-places.service.ts
│   │   │   ├── app.ts
│   │   │   ├── app.html
│   │   │   ├── app.scss
│   │   │   └── app.routes.ts
│   │   └── environments/
│   │       └── environment.ts
│   ├── package.json
│   └── angular.json
│
└── README.md                             # Este archivo
```

---

## 🚀 Instalación

### Prerrequisitos

- **Java 17+** ([Descargar](https://www.oracle.com/java/technologies/downloads/))
- **Maven 3.8+** ([Descargar](https://maven.apache.org/download.cgi))
- **Node.js 18+** ([Descargar](https://nodejs.org/))
- **MongoDB 6+** ([Descargar](https://www.mongodb.com/try/download/community))
- **Git** ([Descargar](https://git-scm.com/downloads))

### Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd proyecto-consumo-apis
```

### Instalar Dependencias Backend

```bash
cd ConsumoApis
mvn clean install
```

### Instalar Dependencias Frontend

```bash
cd frontend-consumo-apis
npm install
```

---

## ⚙️ Configuración

### 1. MongoDB

Asegúrate de que MongoDB esté corriendo:

```bash
# Windows
net start MongoDB

# Linux/Mac
sudo systemctl start mongod
```

Crea la base de datos:
```bash
mongosh
use 15_keller_rejas
```

### 2. Variables de Entorno Backend

Crea un archivo `.env` en `ConsumoApis/` (basado en `.env.example`):

```properties
# MongoDB
MONGODB_URI=mongodb://localhost:27017/15_keller_rejas

# IP Geo Location API (RapidAPI)
IPGEO_RAPIDAPI_KEY=tu_api_key_aqui
IPGEO_BASE_URL=https://ip-geo-location10.p.rapidapi.com

# Google Places API
GOOGLE_MAPS_RAPIDAPI_KEY=tu_api_key_aqui
GOOGLE_MAPS_BASE_URL=https://google-map-places-new-v2.p.rapidapi.com
```

O configura en `application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/15_keller_rejas

api:
  ipgeo:
    rapidapi-key: ${IPGEO_RAPIDAPI_KEY:tu_key}
    base-url: https://ip-geo-location10.p.rapidapi.com
  google-maps:
    rapidapi-key: ${GOOGLE_MAPS_RAPIDAPI_KEY:tu_key}
    base-url: https://google-map-places-new-v2.p.rapidapi.com
```

### 3. Configuración Frontend

Edita `frontend-consumo-apis/src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1'
};
```

---

## ▶️ Ejecución

### 1. Iniciar MongoDB

```bash
# Windows
net start MongoDB

# Linux/Mac
sudo systemctl start mongod
```

### 2. Iniciar Backend

```bash
cd ConsumoApis
mvn spring-boot:run
```

El backend estará disponible en: `http://localhost:8080`

### 3. Iniciar Frontend

```bash
cd frontend-consumo-apis
npm start
```

El frontend estará disponible en: `http://localhost:4200`

### 4. Acceder a la Aplicación

- **Frontend:** http://localhost:4200
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/v3/api-docs

---

## 🔌 Endpoints API

### 🌍 IP Geo Location API

**Base URL:** `/api/v1/geo`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/ip/{ipAddress}` | Geolocalizar una IP |
| GET | `/queries` | Listar todas las consultas |
| GET | `/queries/{id}` | Obtener consulta por ID |
| PUT | `/queries/{id}?newIpAddress={ip}` | Actualizar consulta |
| DELETE | `/queries/{id}` | Eliminar consulta (lógico) |

**Ejemplo:**
```bash
# Geolocalizar IP
curl http://localhost:8080/api/v1/geo/ip/8.8.8.8

# Listar todas las consultas
curl http://localhost:8080/api/v1/geo/queries
```

### 🗺️ Google Places API

**Base URL:** `/api/v1/places`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/search?query={query}` | Buscar lugares |
| GET | `/autocomplete?input={input}` | Autocompletar lugares |
| GET | `/{placeId}/photos/{photoRef}` | Obtener foto de lugar |
| GET | `/queries` | Listar todas las búsquedas |
| GET | `/queries/{id}` | Obtener búsqueda por ID |
| PUT | `/queries/{id}?newQuery={query}` | Actualizar búsqueda |
| DELETE | `/queries/{id}` | Eliminar búsqueda (lógico) |

**Ejemplo:**
```bash
# Buscar lugares
curl "http://localhost:8080/api/v1/places/search?query=restaurantes+en+Lima"

# Listar todas las búsquedas
curl http://localhost:8080/api/v1/places/queries
```

---

## 🎨 Frontend

### Rutas

| Ruta | Componente | Descripción |
|------|------------|-------------|
| `/` | Redirect | Redirige a `/geo-location` |
| `/geo-location` | GeoQueryListComponent | Gestión de consultas IP |
| `/places-search` | PlacesSearchComponent | Búsqueda de lugares |

### Navegación

El frontend incluye un menú de navegación en la parte superior:

```
┌─────────────────────────────────────────────────┐
│  🌍 Consumo APIs - Sistema Completo             │
│                                                  │
│  [🌍 Geolocalización IP] [🗺️ Búsqueda de Lugares]│
└─────────────────────────────────────────────────┘
```

### Características UI

- ✅ Diseño responsive (móvil, tablet, desktop)
- ✅ Animaciones suaves
- ✅ Loading states
- ✅ Manejo de errores
- ✅ Confirmaciones de eliminación
- ✅ Actualización en tiempo real

---

## 💾 Base de Datos

### Colecciones MongoDB

#### 1. `ip_geo_responses`

```javascript
{
  "_id": ObjectId("..."),
  "ipAddress": "8.8.8.8",
  "country": "United States",
  "region": "California",
  "city": "Mountain View",
  "latitude": 37.4056,
  "longitude": -122.0775,
  "timezone": "America/Los_Angeles",
  "isp": "Google LLC",
  "org": "Google LLC",
  "as": "AS15169 Google LLC",
  "timestamp": ISODate("2026-05-11T14:30:25.123Z"),
  "rawResponse": "{...}",
  "deleted": false
}
```

#### 2. `google_places_responses`

```javascript
{
  "_id": ObjectId("..."),
  "query": "restaurantes en Lima",
  "status": "OK",
  "places": [
    {
      "placeId": "ChIJ...",
      "name": "La Rosa Náutica",
      "formattedAddress": "Espigón 4, Circuito de Playas...",
      "latitude": -12.1234,
      "longitude": -77.5678,
      "rating": "4.5",
      "types": ["restaurant", "food", "point_of_interest"],
      "vicinity": "Miraflores",
      "photos": [
        {
          "photoReference": "places/ChIJ.../photos/...",
          "heightPx": 3024,
          "widthPx": 4032,
          "authorAttributions": "Juan Pérez"
        }
      ]
    }
  ],
  "timestamp": ISODate("2026-05-11T14:30:25.123Z"),
  "rawResponse": "{...}",
  "deleted": false
}
```

### Índices

```javascript
// ip_geo_responses
db.ip_geo_responses.createIndex({ "ipAddress": 1 })
db.ip_geo_responses.createIndex({ "timestamp": -1 })

// google_places_responses
db.google_places_responses.createIndex({ "query": 1 })
db.google_places_responses.createIndex({ "timestamp": -1 })
```

---

## ✨ Características

### Backend

- ✅ **Programación Reactiva** con Spring WebFlux
- ✅ **API RESTful** con mejores prácticas
- ✅ **Documentación automática** con Swagger/OpenAPI
- ✅ **Validación de datos** con Bean Validation
- ✅ **Manejo de errores** centralizado
- ✅ **Logging** completo con SLF4J
- ✅ **CORS** configurado
- ✅ **Borrado lógico** en todas las entidades
- ✅ **Operaciones CRUD** completas
- ✅ **Integración con APIs externas**

### Frontend

- ✅ **Componentes standalone** (Angular 17+)
- ✅ **Programación reactiva** con RxJS
- ✅ **Routing** con Angular Router
- ✅ **Formularios reactivos**
- ✅ **Diseño responsive**
- ✅ **Animaciones CSS**
- ✅ **Manejo de estados** (loading, error, success)
- ✅ **Confirmaciones** de acciones destructivas
- ✅ **Actualización automática** de listados

### Seguridad

- ✅ **CORS** configurado
- ✅ **Validación de entrada**
- ✅ **Manejo seguro de API keys**
- ✅ **Borrado lógico** (no se eliminan datos físicamente)

---

## 📚 Documentación API

### Swagger UI

Accede a la documentación interactiva en:

```
http://localhost:8080/swagger-ui.html
```

Características:
- Explorar todos los endpoints
- Probar endpoints directamente
- Ver esquemas de datos
- Ejemplos de request/response

### OpenAPI JSON

```
http://localhost:8080/v3/api-docs
```

---

## 🐳 Despliegue

### Docker

#### Backend

```bash
cd ConsumoApis
docker build -t consumo-apis-backend .
docker run -p 8080:8080 \
  -e MONGODB_URI=mongodb://host.docker.internal:27017/15_keller_rejas \
  -e IPGEO_RAPIDAPI_KEY=tu_key \
  -e GOOGLE_MAPS_RAPIDAPI_KEY=tu_key \
  consumo-apis-backend
```

#### Frontend

```bash
cd frontend-consumo-apis
docker build -t consumo-apis-frontend .
docker run -p 4200:80 consumo-apis-frontend
```

### Kubernetes

Los manifiestos de Kubernetes están en `ConsumoApis/manifest-keller-rejas/`:

```bash
cd ConsumoApis/manifest-keller-rejas
kubectl apply -f keller-rejas-32-namespace.yml
kubectl apply -f keller-rejas-32-secret.yml
kubectl apply -f keller-rejas-32-deployment.yml
kubectl apply -f keller-rejas-32-service.yml
```

---

## 🧪 Testing

### Backend

```bash
cd ConsumoApis
mvn test
```

### Frontend

```bash
cd frontend-consumo-apis
npm test
```

---

## 📊 Estadísticas del Proyecto

### Backend
- **Controladores:** 2
- **Servicios:** 2
- **Repositorios:** 4
- **Modelos:** 4
- **Endpoints:** 12
- **Líneas de código:** ~2,500

### Frontend
- **Componentes:** 2
- **Servicios:** 2
- **Modelos:** 2
- **Rutas:** 2
- **Líneas de código:** ~1,800

---

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📝 Licencia

Este proyecto es parte de una actividad académica de AEJ.

---

## 👤 Autor

**Keller Rejas**
- Actividad: AEJ - Consumo de APIs
- Año: 2026

---

## 🙏 Agradecimientos

- Spring Boot Team
- Angular Team
- MongoDB Team
- RapidAPI
- Google Places API

---

## 📞 Soporte

Si tienes problemas:

1. Verifica que MongoDB esté corriendo
2. Verifica que las API keys sean válidas
3. Revisa los logs del backend
4. Revisa la consola del navegador
5. Consulta Swagger UI para probar endpoints

---

## 🔄 Actualizaciones

### Versión 1.0.0 (Mayo 2026)
- ✅ Implementación inicial
- ✅ CRUD completo para ambas APIs
- ✅ Frontend con 2 apartados
- ✅ Documentación Swagger
- ✅ Despliegue con Docker y Kubernetes

---

## 📖 Recursos Adicionales

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Angular Documentation](https://angular.io/docs)
- [MongoDB Documentation](https://docs.mongodb.com/)
- [Swagger Documentation](https://swagger.io/docs/)
- [RapidAPI Documentation](https://docs.rapidapi.com/)

---

**¡Gracias por usar este sistema! 🚀**
