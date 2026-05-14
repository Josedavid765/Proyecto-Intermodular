# EcoDrop Backend - Documentación

## Stack Tecnológico
- **Java 17** + **Spring Boot 3.5.14**
- **MySQL** (localhost:3306/ecodrop)
- **JWT** (jjwt 0.11.5) para autenticación
- **Puerto:** 8081
- **CORS:** http://localhost:4200

## Seguridad
- Endpoints públicos: `POST /api/auth/**`, `GET /api/comercios/**`, `GET /api/repartidores/**`
- El resto requiere header: `Authorization: Bearer <token>`
- Roles: `COMERCIO`, `REPARTIDOR`
- Login devuelve: `{token, email, rol}`

## Endpoints

### Auth (`/api/auth`)
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| POST | /registrar/comercio | No | Registrar comercio |
| POST | /registrar/repartidor | No | Registrar repartidor |
| POST | /login | No | Login, devuelve JWT |

### Comercios (`/api/comercios`)
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | / | No | Listar todos |
| GET | /{id} | No | Por ID |
| GET | /me | COMERCIO | Mi perfil |
| POST | / | COMERCIO/ADMIN | Crear comercio |

### Pedidos (`/api/pedidos`)
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | /comercio/{id} | COMERCIO/ADMIN | Por comercio |
| GET | /comercio/me | COMERCIO | Mis pedidos |
| GET | /disponibles | REPARTIDOR | Pedidos sin repartidor |
| GET | /repartidor/{id} | REPARTIDOR/ADMIN | Por repartidor |
| POST | / | COMERCIO | Crear pedido |
| PATCH | /{id}/estado | COMERCIO/REPARTIDOR | Cambiar estado |
| PUT | /{id}/repartidor/{idRep} | REPARTIDOR | Asignar repartidor |
| PUT | /{id}/valorar | COMERCIO/REPARTIDOR | Valorar (tipo, puntuacion) |

### Repartidores (`/api/repartidores`)
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | / | No | Listar todos |
| GET | /disponibles | No | Solo disponibles |
| GET | /me | REPARTIDOR | Mi perfil |
| PUT | /estado | REPARTIDOR | Actualizar estado |

## Enums
- **EstadoPedido:** `PENDIENTE, EN_TRANSITO, ENTREGADO`
- **EstadoRepartidor:** `DISPONIBLE, OCUPADO`
- **Vehiculo:** `BICICLETA, PATINETE`
