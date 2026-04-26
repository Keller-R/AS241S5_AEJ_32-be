#!/bin/bash
# =============================================================
# Setup k3s para GitHub Codespace (sin systemd)
# =============================================================

set -e

echo "============================================="
echo " Setup Codespace - Keller Rejas 32"
echo "============================================="

CURRENT_USER=$(whoami)
HOME_DIR=$(eval echo ~$CURRENT_USER)

# ---- 1. Instalar k3s sin systemd ----
echo ""
echo "[1/3] Instalando k3s (sin systemd)..."
curl -sfL https://get.k3s.io | INSTALL_K3S_EXEC="--disable=traefik" INSTALL_K3S_SKIP_ENABLE=true sh -

# Iniciar k3s manualmente en background
echo "      Iniciando k3s en background..."
sudo k3s server --disable=traefik &
K3S_PID=$!
echo "      k3s PID: $K3S_PID"

# Esperar que k3s genere el kubeconfig
echo "      Esperando que k3s esté listo..."
for i in $(seq 1 30); do
  if sudo test -f /etc/rancher/k3s/k3s.yaml 2>/dev/null; then
    echo "      ✅ k3s.yaml disponible"
    break
  fi
  echo "      Intento $i/30..."
  sleep 3
done

# ---- 2. Configurar kubectl ----
echo ""
echo "[2/3] Configurando kubectl..."
mkdir -p "$HOME_DIR/.kube"
sudo cp /etc/rancher/k3s/k3s.yaml "$HOME_DIR/.kube/config"
sudo chown "$CURRENT_USER:$CURRENT_USER" "$HOME_DIR/.kube/config"
chmod 600 "$HOME_DIR/.kube/config"

# Exportar en sesión actual y en bashrc
export KUBECONFIG="$HOME_DIR/.kube/config"
grep -qxF "export KUBECONFIG=$HOME_DIR/.kube/config" "$HOME_DIR/.bashrc" || \
  echo "export KUBECONFIG=$HOME_DIR/.kube/config" >> "$HOME_DIR/.bashrc"

# ---- 3. Verificar ----
echo ""
echo "[3/3] Verificando kubectl..."
sleep 5
kubectl get nodes || echo "⚠️  k3s aún iniciando, espera 20s y ejecuta: kubectl get nodes"

echo ""
echo "✅ Setup completo."
echo "   Si los nodos no aparecen aún, espera 20 segundos y ejecuta:"
echo "   kubectl get nodes"
