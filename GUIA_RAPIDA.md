# ⚡ Guía Rápida - Consumo APIs

## 🎯 Inicio en 3 Pasos

### 1️⃣ Iniciar MongoDB
```bash
# Windows
net start MongoDB

# Linux/Mac
sudo systemctl start mongod
```

### 2️⃣ Iniciar Backend
```bash
cd ConsumoApis
mvn spring-boot:run
```
✅ Backend listo en: http://localhost:8080

### 3️⃣ Iniciar Frontend
```bash
cd frontend-consumo-apis
npm start
```
✅ Frontend listo en: http://localhost:4200

---

## 🌐 URLs Importantes

| Servicio | URL |
|----------|-----|
| **Frontend** | http://localhost:4200 |
| **Geolocalización IP** | http://localhost:4200/geo-location |
| **Búsqueda de Lugares** | http://localhost:4200/places-search |
| **Backend API** | http://localhost:8080 |
| **Swagger UI** | http://localhost:8080/swagger-ui.html |

---

## 🔌 Endpoints Principales

### IP Geo Location
```bash
# Geolocalizar IP
curl http://localhost:8080/api/v1/geo/ip/8.8.8.8

# Listar todas
curl http://localhost:8080/api/v1/geo/queries
```

### Google Places
```bash
# Buscar lugares
curl "http://localhost:8080/api/v1/places/search?query=restaurantes+en+Lima"

# Listar todas
curl http://localhost:8080/api/v1/places/queries
```

---

## 🎨 Navegación Frontend

```
┌─────────────────────────────────────────────┐
│  🌍 Consumo APIs - Sistema Completo         │
│                                              │
│  [🌍 Geolocalización IP] [🗺️ Búsqueda]     │
└─────────────────────────────────────────────┘
```

**Geolocalización IP:** Consulta ubicaciones por dirección IP  
**Búsqueda de Lugares:** Busca restaurantes, hoteles, etc.

---

## ⚙️ Configuración Rápida

### Backend (`application.yml`)
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/15_keller_rejas
```

### Frontend (`environment.ts`)
```typescript
export const environment = {
  apiUrl: 'http://localhost:8080/api/v1'
};
```

---

## 🐛 Solución de Problemas

### Backend no inicia
```bash
# Recompilar
cd ConsumoApis
mvn clean install
mvn spring-boot:run
```

### Frontend no carga datos
1. Verifica que el backend esté corriendo
2. Revisa la consola del navegador (F12)
3. Verifica la URL en `environment.ts`

### MongoDB no conecta
```bash
# Verificar que esté corriendo
mongosh
```

---

## 📚 Documentación Completa

Ver [README.md](README.md) para documentación detallada.

---

**¡Listo para usar! 🚀**
