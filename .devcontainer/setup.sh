#!/bin/bash
# =============================================================
# Setup automático del Codespace
# Instala k3s (Kubernetes ligero) y configura kubectl
# =============================================================

set -e

echo "============================================="
echo " Setup Codespace - Keller Rejas 32"
echo "============================================="

# ---- Instalar k3s (Kubernetes ligero) ----
echo "[1/3] Instalando k3s..."
curl -sfL https://get.k3s.io | INSTALL_K3S_EXEC="--disable=traefik" sh -

# ---- Configurar kubectl ----
echo "[2/3] Configurando kubectl..."
mkdir -p /home/vscode/.kube
sudo cp /etc/rancher/k3s/k3s.yaml /home/vscode/.kube/config
sudo chown vscode:vscode /home/vscode/.kube/config
chmod 600 /home/vscode/.kube/config

# Agregar al bashrc para que persista
echo 'export KUBECONFIG=/home/vscode/.kube/config' >> /home/vscode/.bashrc

# ---- Verificar ----
echo "[3/3] Verificando instalación..."
export KUBECONFIG=/home/vscode/.kube/config
sleep 5
kubectl get nodes || echo "k3s iniciando, estará listo en unos segundos..."

echo ""
echo "✅ Setup completo. Kubernetes listo."
echo "   Ejecuta: kubectl get nodes"
