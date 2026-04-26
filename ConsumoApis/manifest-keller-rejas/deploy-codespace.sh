#!/bin/bash
# =============================================================
# Script de despliegue para GitHub Codespace - Keller Rejas 32
# Uso: bash deploy-codespace.sh [dockerhub-user]
# =============================================================

DOCKERHUB_USER=${1:-kellerrejas}
IMAGE_NAME="consumo-apis"
IMAGE_TAG="latest"
FULL_IMAGE="$DOCKERHUB_USER/$IMAGE_NAME:$IMAGE_TAG"
NAMESPACE="keller-rejas-32"
MANIFEST_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$MANIFEST_DIR")"

echo "============================================="
echo " DESPLIEGUE EN CODESPACE - Keller Rejas 32"
echo " Imagen: $FULL_IMAGE"
echo "============================================="

# ---- Verificar dependencias ----
echo ""
echo "[0/5] Verificando entorno..."
command -v mvn   >/dev/null 2>&1 || { echo "ERROR: Maven no encontrado"; exit 1; }
command -v docker >/dev/null 2>&1 || { echo "ERROR: Docker no encontrado"; exit 1; }
command -v kubectl >/dev/null 2>&1 || { echo "ERROR: kubectl no encontrado"; exit 1; }

# Verificar que k3s esté corriendo
kubectl get nodes >/dev/null 2>&1 || {
  echo "ERROR: Kubernetes no está listo. Espera unos segundos y vuelve a intentar."
  echo "       Verifica con: kubectl get nodes"
  exit 1
}
echo "✅ Entorno OK"

# ---- 1. BUILD JAR ----
echo ""
echo "[1/5] Compilando proyecto Spring Boot..."
cd "$PROJECT_DIR" || exit 1
mvn clean package -DskipTests -B -q
echo "✅ JAR generado: target/consumo-apis-1.0.0.jar"

# ---- 2. BUILD DOCKER IMAGE ----
echo ""
echo "[2/5] Construyendo imagen Docker: $FULL_IMAGE"
docker build -t "$FULL_IMAGE" .
echo "✅ Imagen Docker construida"

# ---- 3. IMPORTAR IMAGEN A k3s ----
echo ""
echo "[3/5] Importando imagen a k3s (sin necesidad de Docker Hub)..."
docker save "$FULL_IMAGE" | sudo k3s ctr images import -
echo "✅ Imagen importada a k3s"

# Actualizar deployment para usar imagePullPolicy: Never (imagen local)
sed -i 's|imagePullPolicy:.*||g' "$MANIFEST_DIR/keller-rejas-32-deployment.yml" 2>/dev/null || true

# ---- 4. APLICAR MANIFIESTOS ----
echo ""
echo "[4/5] Desplegando en Kubernetes..."
cd "$MANIFEST_DIR" || exit 1

kubectl apply -f keller-rejas-32-namespace.yml
kubectl apply -f keller-rejas-32-secret.yml
kubectl apply -f keller-rejas-32-service.yml
kubectl apply -f keller-rejas-32-deployment.yml

echo "✅ Manifiestos aplicados"

# ---- 5. VERIFICAR ----
echo ""
echo "[5/5] Esperando que los pods levanten..."
kubectl rollout status deployment/keller-rejas-32-deployment \
  -n $NAMESPACE --timeout=180s

echo ""
echo "--- Pods ---"
kubectl get pods -n $NAMESPACE

echo ""
echo "--- Service ---"
kubectl get service -n $NAMESPACE

echo ""
echo "============================================="
echo " ✅ DESPLIEGUE COMPLETADO"
echo ""
echo " Para acceder a la app en Codespace:"
echo " kubectl port-forward service/keller-rejas-32-service 8080:8080 -n $NAMESPACE &"
echo " curl http://localhost:8080/actuator/health"
echo " Swagger: http://localhost:8080/swagger-ui.html"
echo "============================================="
