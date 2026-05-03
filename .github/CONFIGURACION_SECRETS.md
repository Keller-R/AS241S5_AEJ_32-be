# 🔐 CONFIGURACIÓN DE SECRETS PARA GITHUB ACTIONS

**Estudiante:** Keller Rejas  
**Número:** 32  
**Pipeline:** DockerHub CI/CD

---

## 📋 RESUMEN

Para que el pipeline funcione, necesitas configurar 2 secrets en GitHub:

1. ✅ `DOCKERHUB_USERNAME_KELLER` → Tu usuario de DockerHub
2. ✅ `DOCKERHUB_TOKEN_KELLER` → Token de acceso de DockerHub

---

## 🎯 PASO 1: OBTENER TOKEN DE DOCKERHUB

### 1.1. Ir a DockerHub

Abre tu navegador y ve a:
```
https://hub.docker.com/settings/security
```

### 1.2. Crear Access Token

1. Haz clic en **"New Access Token"**
2. Completa el formulario:
   - **Description:** `GitHub Actions Pipeline - Keller Rejas`
   - **Access permissions:** Selecciona **"Read, Write, Delete"**
   
   ⚠️ **IMPORTANTE:** NO selecciones "Read-only" (solo lectura)
   
3. Haz clic en **"Generate"**

### 1.3. Copiar el Token

1. Se mostrará un token como: `dckr_pat_abc123xyz...`
2. **COPIA EL TOKEN INMEDIATAMENTE** (solo se muestra una vez)
3. Guárdalo temporalmente en un lugar seguro

⚠️ **ADVERTENCIA:** Si pierdes el token, tendrás que crear uno nuevo

---

## 🔧 PASO 2: CONFIGURAR SECRETS EN GITHUB

### 2.1. Ir a Settings del Repositorio

1. Ve a tu repositorio en GitHub:
   ```
   https://github.com/Keller-R/AS241S5_AEJ_32-be
   ```

2. Haz clic en **"Settings"** (en la parte superior)

3. En el menú lateral izquierdo, busca **"Secrets and variables"**

4. Haz clic en **"Actions"**

### 2.2. Crear Secret: DOCKERHUB_USERNAME_KELLER

1. Haz clic en **"New repository secret"**

2. Completa el formulario:
   - **Name:** `DOCKERHUB_USERNAME_KELLER`
   - **Secret:** `kellerr`

3. Haz clic en **"Add secret"**

### 2.3. Crear Secret: DOCKERHUB_TOKEN_KELLER

1. Haz clic en **"New repository secret"** nuevamente

2. Completa el formulario:
   - **Name:** `DOCKERHUB_TOKEN_KELLER`
   - **Secret:** `[Pega el token que copiaste de DockerHub]`

3. Haz clic en **"Add secret"**

---

## ✅ PASO 3: VERIFICAR CONFIGURACIÓN

### 3.1. Verificar Secrets

1. Ve a: **Settings → Secrets and variables → Actions**

2. Deberías ver:
   ```
   ✅ DOCKERHUB_USERNAME_KELLER
   ✅ DOCKERHUB_TOKEN_KELLER
   ```

3. Si ves ambos secrets, ¡estás listo!

### 3.2. Verificar Permisos del Token

1. Ve a: https://hub.docker.com/settings/security

2. Busca el token que creaste: "GitHub Actions Pipeline - Keller Rejas"

3. Verifica que tenga permisos: **"Read, Write, Delete"**

---

## 🚀 PASO 4: PROBAR EL PIPELINE

### 4.1. Hacer un Commit

```bash
# Ir al directorio del proyecto
cd C:\Users\Lenovo\Desktop\Test\AS241S5_AEJ_32-be

# Verificar rama
git branch

# Cambiar a develop si no estás en ella
git checkout develop

# Agregar archivos
git add .github/

# Hacer commit
git commit -m "ci: add GitHub Actions pipeline for DockerHub CI/CD

- Add dockerhub-ci.yml workflow
- Configure automatic build and push to DockerHub
- Add documentation for secrets configuration

Student: Keller Rejas (32)"

# Subir a GitHub
git push origin develop
```

### 4.2. Ver el Pipeline en Acción

1. Ve a tu repositorio en GitHub

2. Haz clic en la pestaña **"Actions"**

3. Deberías ver un workflow ejecutándose: **"DockerHub CI/CD Pipeline"**

4. Haz clic en el workflow para ver los detalles

5. Espera a que termine (tarda ~5-10 minutos)

### 4.3. Verificar en DockerHub

1. Ve a: https://hub.docker.com/r/kellerr/consumo-apis

2. Deberías ver nuevos tags:
   - `latest`
   - `1.0`
   - `develop-abc123` (con el SHA del commit)

---

## 📊 ESTRUCTURA DEL PIPELINE

```
┌─────────────────────────────────────────┐
│  TRIGGER: Push a develop o main         │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│  PASO 1: Checkout del código            │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│  PASO 2: Configurar Docker Buildx       │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│  PASO 3: Login en DockerHub             │
│  (Usa secrets configurados)             │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│  PASO 4: Extraer metadata (tags)        │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│  PASO 5: Build y Push de la imagen      │
│  - Construye con Dockerfile             │
│  - Sube a DockerHub automáticamente     │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│  PASO 6: Verificar imagen subida        │
│  ✅ Imagen en DockerHub                 │
└─────────────────────────────────────────┘
```

---

## 🎨 TAGS GENERADOS AUTOMÁTICAMENTE

El pipeline genera múltiples tags para cada build:

| Tag | Descripción | Ejemplo |
|-----|-------------|---------|
| `latest` | Última versión de main | `kellerr/consumo-apis:latest` |
| `1.0` | Versión específica | `kellerr/consumo-apis:1.0` |
| `develop-SHA` | Rama develop + commit | `kellerr/consumo-apis:develop-a1b2c3d` |
| `main-SHA` | Rama main + commit | `kellerr/consumo-apis:main-x9y8z7w` |
| `SHA` | Solo commit SHA | `kellerr/consumo-apis:a1b2c3d4e5f6` |

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### Problema 1: "Error: Cannot connect to Docker daemon"

**Causa:** Docker Buildx no está configurado correctamente

**Solución:** El pipeline ya incluye `docker/setup-buildx-action@v3`

---

### Problema 2: "Error: denied: requested access to the resource is denied"

**Causa:** Token de DockerHub no tiene permisos de escritura

**Solución:**
1. Ve a https://hub.docker.com/settings/security
2. Elimina el token anterior
3. Crea un nuevo token con permisos **"Read, Write, Delete"**
4. Actualiza el secret en GitHub

---

### Problema 3: "Error: Username and password required"

**Causa:** Secrets no están configurados correctamente

**Solución:**
1. Verifica que los secrets existen: Settings → Secrets and variables → Actions
2. Verifica los nombres exactos:
   - `DOCKERHUB_USERNAME_KELLER`
   - `DOCKERHUB_TOKEN_KELLER`
3. Si están mal, elimínalos y créalos de nuevo

---

### Problema 4: "Error: Dockerfile not found"

**Causa:** La ruta del Dockerfile es incorrecta

**Solución:**
El pipeline usa:
```yaml
context: ./ConsumoApis
file: ./ConsumoApis/dockerfile-keller-rejas/Dockerfile
```

Verifica que el Dockerfile existe en esa ruta

---

### Problema 5: Pipeline no se ejecuta

**Causa:** El archivo no está en la ruta correcta

**Solución:**
Verifica que el archivo está en:
```
.github/workflows/dockerhub-ci.yml
```

---

## 📝 CHECKLIST FINAL

Antes de hacer push, verifica:

- [ ] Token de DockerHub creado con permisos de escritura
- [ ] Secret `DOCKERHUB_USERNAME_KELLER` configurado en GitHub
- [ ] Secret `DOCKERHUB_TOKEN_KELLER` configurado en GitHub
- [ ] Archivo `.github/workflows/dockerhub-ci.yml` creado
- [ ] Dockerfile existe en `ConsumoApis/dockerfile-keller-rejas/Dockerfile`
- [ ] Estás en la rama `develop`

---

## 🎯 RESULTADO ESPERADO

Después de hacer push:

1. ✅ El pipeline se ejecuta automáticamente
2. ✅ Construye la imagen Docker
3. ✅ Sube la imagen a DockerHub
4. ✅ Genera múltiples tags (latest, 1.0, develop-SHA)
5. ✅ Puedes ver la imagen en https://hub.docker.com/r/kellerr/consumo-apis

---

## 📚 REFERENCIAS

- **GitHub Actions:** https://docs.github.com/en/actions
- **Docker Build Push Action:** https://github.com/docker/build-push-action
- **DockerHub Tokens:** https://docs.docker.com/docker-hub/access-tokens/

---

**¡Listo para configurar! 🚀**
