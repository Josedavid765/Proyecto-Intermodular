# EcoDrop - Ejecución del Backend con Docker

## Requisitos
- **Docker** y **Docker Compose** instalados
- **Git** (opcional, para clonar el repositorio)
- Puerto `8081` libre (backend)
- Puerto `3306` libre (MySQL)

## Estructura de archivos para Docker

```
EcoDrop/
├── backend/
│   ├── Dockerfile
│   ├── src/
│   ├── pom.xml
│   ├── mvnw
│   └── ...
├── docker-compose.yml
└── documentacion/
    └── README-EJECUCION.md
```

## Pasos para ejecutar

### 1. Abrir terminal en la raíz del proyecto

```bash
cd "ruta/del/proyecto/EcoDrop"
```

### 2. Construir y levantar los contenedores

```bash
docker-compose up --build
```

Este comando:
- Construye la imagen del backend (compila el JAR con Maven dentro del contenedor)
- Descarga la imagen de MySQL 8.0
- Crea una red interna entre ambos contenedores
- Expone el backend en `localhost:8081`
- Expone MySQL en `localhost:3306`

### 3. Verificar que funciona

Abre en el navegador o Postman:

```
http://localhost:8081/api/productos
```

Debería devolver `[]` (lista vacía de productos).

### 4. Detener los contenedores

```bash
docker-compose down
```

### 5. Detener y borrar los datos de la base de datos

```bash
docker-compose down -v
```

El flag `-v` elimina también el volumen de MySQL (datos persistentes).

## Comandos útiles

| Comando | Descripción |
|---------|-------------|
| `docker-compose up --build` | Construye y levanta |
| `docker-compose up -d --build` | Construye y levanta en segundo plano |
| `docker-compose down` | Detiene los contenedores |
| `docker-compose down -v` | Detiene y borra volúmenes (datos BD) |
| `docker-compose logs -f` | Ver logs en tiempo real |
| `docker-compose logs backend` | Ver logs solo del backend |
| `docker-compose ps` | Ver estado de los contenedores |

## Notas importantes

- La primera vez que ejecutes `docker-compose up --build` tardará unos minutos porque tiene que descargar imágenes y compilar el proyecto.
- El backend espera a que MySQL esté listo gracias al `healthcheck` configurado.
- Si cambias código del backend, vuelve a ejecutar `docker-compose up --build` para recompilar.
- El `application.properties` usa variables de entorno. Cuando ejecutas con Docker, se usan los valores definidos en el `docker-compose.yml`. Cuando ejecutas localmente sin Docker, se usan los valores por defecto (localhost:3306).

## Solución de problemas

**Error: "Port 3306 already in use"**
Tienes MySQL instalado localmente y está usando el puerto. Detenlo antes o cambia el puerto en el `docker-compose.yml`.

**Error: "Connection refused" al conectar a MySQL**
Espera unos segundos y reintenta. Puede que MySQL no haya terminado de iniciarse.

**Error: "mvnw: line 1: syntax error" en Windows**
Asegúrate de que los archivos se hayan clonado con saltos de línea LF. Si no, el Dockerfile ya ejecuta `sed -i 's/\r$//' mvnw` para corregirlo automáticamente.
