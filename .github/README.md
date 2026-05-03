# 🔄 GitHub Actions - CI/CD Pipeline

**Estudiante:** Keller Rejas  
**Número:** 32  
**Proyecto:** ConsumoApis - Spring Boot WebFlux

---

## 📋 DESCRIPCIÓN

Este directorio contiene los workflows de GitHub Actions para automatizar el proceso de CI/CD (Continuous Integration / Continuous Deployment) del proyecto.

---

## 📁 ESTRUCTURA

```
.github/
├── workflows/
│   └── dockerhub-ci.yml          # Pipeline principal de DockerHub
├── CONFIGURACION_SECRETS.md      # Guía para configurar secrets
└── README.md                     # Este archivo
```

---

## 🚀 WORKFLOWS DISPONIBLES

### 1. DockerHub CI/CD Pipeline (`dockerhub-ci.yml`)

**Propósito:** Construir y subir automáticamente la imagen Docker a DockerHub

**Trigger:**
- Push a rama `develop`
- Push a rama `main`
- Pull request a `develop` o `main`
- Ejecución manual desde GitHub Actions UI

**Pasos:**
1. ✅ Checkout del código
2. ✅ Configurar Docker Buildx
3. ✅ Login en DockerHub
4. ✅ Extraer metadata (tags y labels)
5. ✅ Build de la imagen Docker
6. ✅ Push de la imagen a DockerHub
7. ✅ Verificar imagen subida

**Resultado:**
- Imagen subida a: `kellerr/consumo-apis`
- Tags generados: `latest`, `1.0`, `develop-SHA`, `main-SHA`

---

## 🔐 CONFIGURACIÓN REQUERIDA

### Secrets de GitHub

Para que el pipeline funcione, debes configurar estos secrets:

| Secret | Valor | Descripción |
|--------|-------|-------------|
| `DOCKERHUB_USERNAME_KELLER` | `kellerr` | Usuario de DockerHub |
| `DOCKERHUB_TOKEN_KELLER` | `[token]` | Token de acceso de DockerHub |

**Cómo configurar:** Ver [CONFIGURACION_SECRETS.md](CONFIGURACION_SECRETS.md)

---

## 📊 CÓMO FUNCIONA

### Flujo Automático

```
1. Desarrollador hace push a develop
   ↓
2. GitHub Actions detecta el push
   ↓
3. Se ejecuta el workflow dockerhub-ci.yml
   ↓
4. Se construye la imagen Docker
   ↓
5. Se sube automáticamente a DockerHub
   ↓
6. Imagen disponible en hub.docker.com/r/kellerr/consumo-apis
```

### Ejemplo de Uso

```bash
# 1. Hacer cambios en el código
git add .
git commit -m "feat: add new feature"

# 2. Subir a GitHub
git push origin develop

# 3. El pipeline se ejecuta automáticamente
# 4. Ver progreso en: https://github.com/Keller-R/AS241S5_AEJ_32-be/actions

# 5. Verificar imagen en DockerHub
# https://hub.docker.com/r/kellerr/consumo-apis
```

---

## 🎨 TAGS GENERADOS

El pipeline genera automáticamente múltiples tags:

| Tag | Cuándo se genera | Ejemplo |
|-----|------------------|---------|
| `latest` | Push a main | `kellerr/consumo-apis:latest` |
| `1.0` | Siempre | `kellerr/consumo-apis:1.0` |
| `develop-SHA` | Push a develop | `kellerr/consumo-apis:develop-a1b2c3d` |
| `main-SHA` | Push a main | `kellerr/consumo-apis:main-x9y8z7w` |
| `SHA` | Siempre | `kellerr/consumo-apis:a1b2c3d4e5f6` |

---

## 🔍 MONITOREO

### Ver Ejecuciones del Pipeline

1. Ve a: https://github.com/Keller-R/AS241S5_AEJ_32-be/actions
2. Haz clic en el workflow: "DockerHub CI/CD Pipeline"
3. Verás todas las ejecuciones (exitosas y fallidas)

### Ver Logs Detallados

1. Haz clic en una ejecución específica
2. Haz clic en el job: "Build and Push Docker Image to DockerHub"
3. Expande cada paso para ver los logs

### Verificar Imagen en DockerHub

1. Ve a: https://hub.docker.com/r/kellerr/consumo-apis
2. Haz clic en "Tags"
3. Verás todos los tags generados

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### Pipeline Falla en "Login to Docker Hub"

**Causa:** Secrets no configurados o incorrectos

**Solución:**
1. Verifica secrets en: Settings → Secrets and variables → Actions
2. Asegúrate de que existen:
   - `DOCKERHUB_USERNAME_KELLER`
   - `DOCKERHUB_TOKEN_KELLER`
3. Verifica que el token tiene permisos de escritura

### Pipeline Falla en "Build and Push"

**Causa:** Dockerfile no encontrado o error en el build

**Solución:**
1. Verifica que el Dockerfile existe en:
   ```
   ConsumoApis/dockerfile-keller-rejas/Dockerfile
   ```
2. Verifica que el Dockerfile es válido
3. Revisa los logs del pipeline para ver el error específico

### Imagen No Aparece en DockerHub

**Causa:** Push falló o permisos incorrectos

**Solución:**
1. Verifica que el pipeline terminó exitosamente (✅)
2. Verifica que el token tiene permisos de escritura
3. Espera unos minutos (puede tardar en aparecer)

---

## 📚 DOCUMENTACIÓN

- **Configuración de Secrets:** [CONFIGURACION_SECRETS.md](CONFIGURACION_SECRETS.md)
- **Dockerfile:** [../ConsumoApis/dockerfile-keller-rejas/Dockerfile](../ConsumoApis/dockerfile-keller-rejas/Dockerfile)
- **GitHub Actions Docs:** https://docs.github.com/en/actions

---

## ✅ CHECKLIST DE CONFIGURACIÓN

Antes de usar el pipeline:

- [ ] Token de DockerHub creado con permisos de escritura
- [ ] Secret `DOCKERHUB_USERNAME_KELLER` configurado
- [ ] Secret `DOCKERHUB_TOKEN_KELLER` configurado
- [ ] Dockerfile existe en la ruta correcta
- [ ] Archivo `dockerhub-ci.yml` en `.github/workflows/`
- [ ] Push a rama `develop` o `main`

---

## 🎯 RESULTADO ESPERADO

Después de configurar correctamente:

1. ✅ Cada push a `develop` o `main` ejecuta el pipeline
2. ✅ La imagen se construye automáticamente
3. ✅ La imagen se sube a DockerHub
4. ✅ Múltiples tags se generan automáticamente
5. ✅ Puedes descargar la imagen con: `docker pull kellerr/consumo-apis:latest`

---

## 📞 SOPORTE

Si tienes problemas:

1. Revisa [CONFIGURACION_SECRETS.md](CONFIGURACION_SECRETS.md)
2. Revisa los logs del pipeline en GitHub Actions
3. Verifica que los secrets están configurados correctamente
4. Verifica que el Dockerfile existe y es válido

---

**¡Pipeline configurado y listo! 🚀**
