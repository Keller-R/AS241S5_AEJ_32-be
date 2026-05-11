# 🎨 Frontend - Consumo APIs

Frontend del sistema de consumo de APIs con Angular.

## 🚀 Inicio Rápido

```bash
# Instalar dependencias
npm install

# Ejecutar en desarrollo
npm start
```

## 📍 URLs

- **Aplicación:** http://localhost:4200
- **Geolocalización IP:** http://localhost:4200/geo-location
- **Búsqueda de Lugares:** http://localhost:4200/places-search

## 🎯 Componentes

### 🌍 Geolocalización IP (`/geo-location`)
- Consultar ubicación por IP
- Listar todas las consultas
- Ver detalles de consulta
- Actualizar y eliminar consultas

### 🗺️ Búsqueda de Lugares (`/places-search`)
- Buscar lugares con Google Places
- Ver historial de búsquedas
- Ver detalles de lugares encontrados
- Actualizar y eliminar búsquedas

## ⚙️ Configuración

Edita `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1'
};
```

## 🎨 Características

- ✅ Diseño responsive
- ✅ Animaciones suaves
- ✅ Manejo de errores
- ✅ Loading states
- ✅ Confirmaciones de eliminación
- ✅ Navegación entre secciones

## 📚 Documentación Completa

Ver [README principal](../README.md) para documentación completa del proyecto.
