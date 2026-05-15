# Manual de Despliegue - EcoDrop

## Estructura del proyecto dockerizado

| Componente       | Puerto expuesto | Base image                                                  |
| ---------------- | --------------- | ----------------------------------------------------------- |
| **mysql**        | `3307:3306`     | `mysql:8.0`                                                 |
| **backend**      | `8081:8081`     | `eclipse-temurin:17-jdk-alpine` (build) / `17-jre-alpine` (runtime) |
| **frontend**     | `4200:80`       | `node:22-alpine` (build) / `nginx:alpine` (servir)          |

## Requisitos previos

### 1. Docker Desktop (obligatorio)
- Descargar e instalar desde [docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop/)
- Requiere virtualización habilitada en BIOS y WSL 2 (recomendado) o Hyper-V

### 2. WSL 2 (recomendado en Windows)
```powershell
wsl --install -d Ubuntu
```
- Configurar Docker Desktop → Settings → Resources → WSL Integration → activar la distro

### 3. Verificaciones rápidas
```powershell
docker --version
docker compose version
```

## Comandos para levantar el proyecto

Ejecutar desde la raíz del proyecto (donde está `docker-compose.yml`):

```powershell
# Construir las imágenes 
docker compose build 

# Levantar todos los servicios en segundo plano
docker compose up -d

# Ver logs en tiempo real
docker compose logs -f

# Detener y eliminar contenedores
docker compose down

# Detener, eliminar contenedores y volúmenes (borra la base de datos)
docker compose down -v

## La ruta para poner en el Navegador una vez ya se han levantado los contenedores es esta: 

http://localhost:4200/
```

## Nota sobre puertos

- MySQL en host `3307` → contenedor `3306`. Si ya tienes MySQL local en `3307`, cambia el mapeo en `docker-compose.yml`.

## Resumen de software requerido

| Software            | Versión mínima | Propósito                        |
| ------------------- | -------------- | -------------------------------- |
| Docker Desktop      | 4.x            | Motor Docker + Compose integrado |
| WSL 2 kernel        | cualquier      | Rendimiento en Windows           |
| Virtualización      | habilitada     | Requisito de WSL2 / Docker       |
