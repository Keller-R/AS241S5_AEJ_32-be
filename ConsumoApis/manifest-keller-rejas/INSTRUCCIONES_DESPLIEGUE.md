# Instrucciones de Despliegue - Keller Rejas 32

## 📋 Requisitos Previos

1. **Docker Desktop** abierto y corriendo (con Kubernetes habilitado)
2. Cuenta en **Docker Hub** (usuario: `kellerrejas`)
3. `kubectl` configurado apuntando a Docker Desktop

## 🚀 Despliegue Completo (todo en un comando)

Desde la carpeta `ConsumoApis/`:

```bash
# Opción A: script automático
bash manifest-keller-rejas/deploy.sh kellerrejas

# Opción B: paso a paso (ver abajo)
```

---

## 🔧 Paso a Paso Manual

### 1. Compilar el proyecto
```bash
# Desde ConsumoApis/
mvn clean package -DskipTests
```

### 2. Construir imagen Docker
```bash
docker build -t kellerrejas/consumo-apis:latest .
```

### 3. Subir imagen a Docker Hub
```bash
docker login
docker push kellerrejas/consumo-apis:latest
```

### 4. Aplicar manifiestos Kubernetes
```bash
cd manifest-keller-rejas/

kubectl apply -f keller-rejas-32-namespace.yml
kubectl apply -f keller-rejas-32-secret.yml
kubectl apply -f keller-rejas-32-service.yml
kubectl apply -f keller-rejas-32-deployment.yml
```

### 5. Verificar despliegue
```bash
# Ver pods
kubectl get pods -n keller-rejas-32

# Esperar que estén Running
kubectl rollout status deployment/keller-rejas-32-deployment -n keller-rejas-32

# Ver service
kubectl get service -n keller-rejas-32

# Ver logs
kubectl logs -f deployment/keller-rejas-32-deployment -n keller-rejas-32
```

### 6. Acceder a la aplicación
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health**: http://localhost:8080/actuator/health
- **Places**: http://localhost:8080/api/v1/places/search?query=restaurantes
- **Geo IP**: http://localhost:8080/api/v1/geo/ip/8.8.8.8

---

## 🎥 Demo para Video de Entrega

### Demostrar que el deployment depende del secret:

```bash
# Script automático:
bash manifest-keller-rejas/demo-secret-dependency.sh

# O manualmente:

# 1. Eliminar el secret
kubectl delete secret keller-rejas-32-secret -n keller-rejas-32

# 2. Eliminar y recrear el deployment
kubectl delete deployment keller-rejas-32-deployment -n keller-rejas-32
kubectl apply -f keller-rejas-32-deployment.yml

# 3. Ver que los pods fallan (CreateContainerConfigError)
kubectl get pods -n keller-rejas-32
kubectl describe pods -n keller-rejas-32

# Resultado esperado: pods en estado Error por falta del secret
```

---

## 📁 Estructura de Archivos

```
manifest-keller-rejas/
├── keller-rejas-32-namespace.yml       # Namespace aislado
├── keller-rejas-32-secret.yml          # Secretos BD y APIs (Base64)
├── keller-rejas-32-service.yml         # Service LoadBalancer puerto 8080
├── keller-rejas-32-deployment.yml      # Deployment con 2 réplicas
├── deploy.sh                           # Script despliegue completo
├── demo-secret-dependency.sh           # Script demo para video
└── INSTRUCCIONES_DESPLIEGUE.md         # Este archivo
```

## 🔧 Configuración Técnica

| Parámetro       | Valor                                      |
|-----------------|--------------------------------------------|
| Aplicación      | Spring WebFlux (reactivo)                  |
| Base de Datos   | MongoDB Atlas (nube)                       |
| API 1           | Google Map Places New V2 (RapidAPI)        |
| API 2           | IP Geo Location (RapidAPI)                 |
| Réplicas        | 2                                          |
| Puerto          | 8080                                       |
| Health Check    | /actuator/health                           |
| RAM             | 256Mi - 512Mi                              |
| CPU             | 250m - 500m                                |

## ⚠️ Notas

- Los secretos están codificados en Base64
- Sin el secret, los pods quedan en `CreateContainerConfigError`
- Habilitar Kubernetes en Docker Desktop: Settings → Kubernetes → Enable Kubernetes
