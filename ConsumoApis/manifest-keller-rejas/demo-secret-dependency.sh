#!/bin/bash
# =============================================================
# Script de demostración: dependencia del secret
# Para el video de entrega - muestra que sin el secret el
# deployment falla
# =============================================================

NAMESPACE="keller-rejas-32"

echo "============================================="
echo " DEMO: Dependencia del Secret"
echo "============================================="

echo ""
echo "[PASO 1] Estado actual - todo funcionando:"
kubectl get pods -n $NAMESPACE
echo ""

echo "[PASO 2] Eliminando el secret..."
kubectl delete secret keller-rejas-32-secret -n $NAMESPACE
echo "✅ Secret eliminado"
echo ""

echo "[PASO 3] Eliminando el deployment..."
kubectl delete deployment keller-rejas-32-deployment -n $NAMESPACE
echo "✅ Deployment eliminado"
echo ""

echo "[PASO 4] Volviendo a crear el deployment SIN el secret..."
kubectl apply -f keller-rejas-32-deployment.yml
echo ""

echo "[PASO 5] Verificando pods (deben fallar - CreateContainerConfigError)..."
sleep 10
kubectl get pods -n $NAMESPACE
echo ""
kubectl describe pods -n $NAMESPACE | grep -A5 "Warning\|Error\|secret"

echo ""
echo "============================================="
echo " ❌ RESULTADO: Deployment FALLIDO"
echo "    Los pods no pueden iniciar sin el secret"
echo "============================================="
echo ""
echo "Para restaurar el funcionamiento:"
echo "  kubectl apply -f keller-rejas-32-secret.yml"
echo "  kubectl rollout restart deployment/keller-rejas-32-deployment -n $NAMESPACE"
