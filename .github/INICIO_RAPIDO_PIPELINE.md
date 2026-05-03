# ⚡ INICIO RÁPIDO - GITHUB ACTIONS PIPELINE

**Estudiante:** Keller Rejas (32)

---

## 🎯 EN 5 PASOS

### PASO 1: Obtener Token de DockerHub (2 minutos)

```
1. Ve a: https://hub.docker.com/settings/security
2. Clic en "New Access Token"
3. Nombre: "GitHub Actions Pipeline"
4. Permisos: "Read, Write, Delete" ⚠️ NO solo lectura
5. Clic en "Generate"
6. COPIA EL TOKEN (solo se muestra una vez)
```

---

### PASO 2: Configurar Secrets en GitHub (2 minutos)

```
1. Ve a: https://github.com/Keller-R/AS241S5_AEJ_32-be/settings/secrets/actions
2. Clic en "New repository secret"

Secret 1:
- Name: DOCKERHUB_USERNAME_KELLER
- Secret: kellerr

Secret 2:
- Name: DOCKERHUB_TOKEN_KELLER
- Secret: [Pega el token que copiaste]
```

---

### PASO 3: Verificar Archivos (1 minuto)

```bash
# Verifica que existen estos archivos:
.github/workflows/dockerhub-ci.yml
ConsumoApis/dockerfile-keller-rejas/Dockerfile
```

---

### PASO 4: Hacer Push (1 minuto)

```bash
cd C:\Users\Lenovo\Desktop\Test\AS241S5_AEJ_32-be

git checkout develop
git add .github/
git commit -m "ci: add GitHub Actions pipeline for DockerHub

Student: Keller Rejas (32)"
git push origin develop
```

---

### PASO 5: Verificar Pipeline (2 minutos)

```
1. Ve a: https://github.com/Keller-R/AS241S5_AEJ_32-be/actions
2. Verás el workflow ejecutándose
3. Espera a que termine (✅)
4. Verifica en: https://hub.docker.com/r/kellerr/consumo-apis
```

---

## ✅ CHECKLIST

- [ ] Token de DockerHub obtenido
- [ ] Secret `DOCKERHUB_USERNAME_KELLER` configurado
- [ ] Secret `DOCKERHUB_TOKEN_KELLER` configurado
- [ ] Archivo `dockerhub-ci.yml` creado
- [ ] Push a GitHub realizado
- [ ] Pipeline ejecutándose en Actions
- [ ] Imagen visible en DockerHub

---

## 🎉 RESULTADO

Después de completar:

✅ Pipeline configurado  
✅ Imagen en DockerHub: `kellerr/consumo-apis:latest`  
✅ Automatización completa: cada push construye y sube la imagen

---

## 📚 DOCUMENTACIÓN COMPLETA

- **Configuración detallada:** [CONFIGURACION_SECRETS.md](CONFIGURACION_SECRETS.md)
- **Información del pipeline:** [README.md](README.md)
- **Workflow:** [workflows/dockerhub-ci.yml](workflows/dockerhub-ci.yml)

---

**¡Listo en 5 pasos! 🚀**
