#!/bin/bash
# =============================================================
# Script de despliegue completo - Keller Rejas 32
# Uso: bash deploy.sh <tu-usuario-dockerhub>
# Ejemplo: bash deploy.sh kellerrejas
# =============================================================

DOCKERHUB_USER=${1:-kellerrejas}
IMAGE_NAME="consumo-apis"
IMAGE_TAG="latest"
FULL_IMAGE="$DOCKERHUB_USER/$IMAGE_NAME:$IMAGE_TAG"
NAMESPACE="keller-rejas-32"
MANIFEST_DIR="$(dirname "$0")"
PROJECT_DIR="$(dirname "$MANIFEST_DIR")"

echo "============================================="
echo " DESPLIEGUE - Keller Rejas 32"
echo " Imagen: $FULL_IMAGE"
echo "============================================="

# ---- 1. BUILD JAR ----
echo ""
echo "[1/5] Compilando proyecto Spring Boot..."
cd "$PROJECT_DIR" || exit 1
mvn clean package -DskipTests -B
if [ $? -ne 0 ]; then
  echo "ERROR: Falló la compilación Maven"
  exit 1
fi
echo "✅ JAR generado correctamente"

# ---- 2. BUILD DOCKER IMAGE ----
echo ""
echo "[2/5] Construyendo imagen Docker: $FULL_IMAGE"
docker build -t "$FULL_IMAGE" .
if [ $? -ne 0 ]; then
  echo "ERROR: Falló el build de Docker"
  exit 1
fi
echo "✅ Imagen Docker construida"

# ---- 3. PUSH A DOCKER HUB ----
echo ""
echo "[3/5] Subiendo imagen a Docker Hub..."
echo "      (Si no estás logueado, ejecuta: docker login)"
docker push "$FULL_IMAGE"
if [ $? -ne 0 ]; then
  echo "ERROR: Falló el push. Asegúrate de estar logueado: docker login"
  exit 1
fi
echo "✅ Imagen subida a Docker Hub"

# ---- 4. APLICAR MANIFIESTOS KUBERNETES ----
echo ""
echo "[4/5] Desplegando en Kubernetes..."
cd "$MANIFEST_DIR" || exit 1

kubectl apply -f keller-rejas-32-namespace.yml
kubectl apply -f keller-rejas-32-secret.yml
kubectl apply -f keller-rejas-32-service.yml
kubectl apply -f keller-rejas-32-deployment.yml

if [ $? -ne 0 ]; then
  echo "ERROR: Falló el despliegue en Kubernetes"
  exit 1
fi
echo "✅ Manifiestos aplicados"

# ---- 5. VERIFICAR ----
echo ""
echo "[5/5] Verificando despliegue..."
echo ""
echo "--- Namespace ---"
kubectl get namespace $NAMESPACE

echo ""
echo "--- Pods (esperando que estén Running) ---"
kubectl rollout status deployment/keller-rejas-32-deployment -n $NAMESPACE --timeout=180s

echo ""
echo "--- Pods ---"
kubectl get pods -n $NAMESPACE

echo ""
echo "--- Service ---"
kubectl get service -n $NAMESPACE

echo ""
echo "============================================="
echo " ✅ DESPLIEGUE COMPLETADO"
echo " Swagger UI: http://localhost:8080/swagger-ui.html"
echo " Health:     http://localhost:8080/actuator/health"
echo "============================================="
