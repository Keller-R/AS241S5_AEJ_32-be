#!/bin/bash
# =============================================================
# Script de despliegue para Killercoda - Keller Rejas 32
# Uso: bash deploy.sh
# =============================================================

IMAGE_NAME="kellerrejas/consumo-apis"
IMAGE_TAG="latest"
FULL_IMAGE="$IMAGE_NAME:$IMAGE_TAG"
NAMESPACE="keller-rejas-32"
MANIFEST_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$MANIFEST_DIR")"

echo "============================================="
echo " DESPLIEGUE EN KILLERCODA - Keller Rejas 32"
echo " Imagen: $FULL_IMAGE"
echo "============================================="

# Verificar que estamos en el directorio correcto
if [ ! -f "$PROJECT_DIR/pom.xml" ]; then
    echo "❌ ERROR: No se encuentra pom.xml en $PROJECT_DIR"
    echo "   Ejecuta este script desde: ConsumoApis/manifest-keller-rejas/"
    exit 1
fi

echo "Directorio del proyecto: $PROJECT_DIR"
echo "Directorio de manifiestos: $MANIFEST_DIR"

# ---- 0. INSTALAR DEPENDENCIAS ----
echo ""
echo "[0/6] Instalando dependencias (Java 17 + Maven)..."

# Verificar si Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "Instalando Maven..."
    apt-get update -qq
    apt-get install -y maven -qq
fi

# Instalar Java 17 si no está disponible
if [ ! -d "/usr/lib/jvm/java-17-openjdk-amd64" ]; then
    echo "Instalando Java 17..."
    apt-get update -qq
    apt-get install -y openjdk-17-jdk -qq
fi

# Configurar Java 17 como predeterminado para este script
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Verificar instalación
echo "Versiones configuradas:"
echo "JAVA_HOME: $JAVA_HOME"
java -version
mvn -version

echo "✅ Dependencias instaladas"

# ---- 1. BUILD JAR ----
echo ""
echo "[1/6] Compilando proyecto Spring Boot..."
cd "$PROJECT_DIR" || {
    echo "❌ ERROR: No se puede acceder al directorio $PROJECT_DIR"
    exit 1
}
echo "Directorio actual: $(pwd)"
echo "Verificando pom.xml..."
ls -la pom.xml

mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ ERROR: Falló la compilación del proyecto"
    exit 1
fi
echo "✅ JAR generado: target/consumo-apis-1.0.0.jar"

# ---- 2. BUILD DOCKER IMAGE ----
echo ""
echo "[2/6] Construyendo imagen Docker: $FULL_IMAGE"
docker build -t "$FULL_IMAGE" .
if [ $? -ne 0 ]; then
    echo "❌ ERROR: Falló la construcción de la imagen Docker"
    exit 1
fi
echo "✅ Imagen Docker construida"

# ---- 3. IMPORTAR IMAGEN A k3s ----
echo ""
echo "[3/6] Importando imagen a k3s..."
docker save "$FULL_IMAGE" | ctr -n k8s.io images import -
if [ $? -ne 0 ]; then
    echo "❌ ERROR: Falló la importación de la imagen a k3s"
    exit 1
fi
echo "✅ Imagen importada a k3s"

# Verificar que la imagen esté disponible
echo "Verificando imagen en k3s:"
crictl images | grep consumo-apis

# ---- 4. APLICAR MANIFIESTOS ----
echo ""
echo "[4/6] Desplegando en Kubernetes..."
cd "$MANIFEST_DIR" || exit 1

kubectl apply -f keller-rejas-32-namespace.yml
kubectl apply -f keller-rejas-32-secret.yml
kubectl apply -f keller-rejas-32-service.yml
kubectl apply -f keller-rejas-32-deployment.yml

echo "✅ Manifiestos aplicados"

# ---- 5. VERIFICAR ----
echo ""
echo "[5/6] Esperando que los pods levanten..."
kubectl rollout status deployment/keller-rejas-32-deployment \
  -n $NAMESPACE --timeout=300s

# ---- 6. MOSTRAR ESTADO ----
echo ""
echo "[6/6] Estado del despliegue:"
echo ""
echo "--- Pods ---"
kubectl get pods -n $NAMESPACE -o wide

echo ""
echo "--- Service ---"
kubectl get service -n $NAMESPACE

echo ""
echo "--- Logs del primer pod ---"
POD_NAME=$(kubectl get pods -n $NAMESPACE -l app=consumo-apis -o jsonpath='{.items[0].metadata.name}' 2>/dev/null)
if [ -n "$POD_NAME" ]; then
    echo "Pod: $POD_NAME"
    kubectl logs $POD_NAME -n $NAMESPACE --tail=20
fi

echo ""
echo "============================================="
echo " ✅ DESPLIEGUE COMPLETADO"
echo ""
echo " Comandos útiles:"
echo " - Ver pods:    kubectl get pods -n $NAMESPACE"
echo " - Ver logs:    kubectl logs -f <pod-name> -n $NAMESPACE"
echo " - Ver service: kubectl get svc -n $NAMESPACE"
echo " - Health:      curl http://localhost:8080/actuator/health"
echo " - Swagger:     http://localhost:8080/swagger-ui.html"
echo ""
echo " Para probar la dependencia de secrets:"
echo " bash demo-secret-dependency.sh"
echo "============================================="
