# EcoDrop Backend - Documentación

## Stack Tecnológico
- **Java 17** + **Spring Boot 3.5.14**
- **MySQL** (localhost:3306/ecodrop)
- **JWT** (jjwt 0.11.5) para autenticación
- **Puerto:** 8081
- **CORS:** http://localhost:4200

## Seguridad
- Endpoints públicos: `POST /api/auth/**`, `GET /api/productos/**`, `GET /api/comercios/**`
- El resto requiere header: `Authorization: Bearer <token>`
- Roles: `USUARIO`, `COMERCIO`, `REPARTIDOR`, `ADMIN`
- El registro siempre crea rol `USUARIO`
- Login devuelve: `{token, email, rol}`

## Endpoints

### Auth (`/api/auth`)
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| POST | /registrar | No | Registrar usuario |
| POST | /login | No | Login, devuelve JWT |

### Usuarios (`/api/usuarios`)
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | /me | Sí | Perfil del usuario autenticado |

### Productos (`/api/productos`)
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | / | No | Listar todos |
| GET | /disponibles | No | Solo disponibles |
| GET | /{id} | No | Por ID |
| GET | /comercio/{id} | No | Por comercio |
| POST | / | COMERCIO/ADMIN | Crear producto |
| PUT | /{id} | COMERCIO/ADMIN | Actualizar |
| DELETE | /{id} | COMERCIO/ADMIN | Eliminar |

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
| GET | /todos | ADMIN | Todos los pedidos |
| GET | /usuario/{id} | USUARIO/ADMIN | Por usuario |
| GET | /comercio/{id} | COMERCIO/ADMIN | Por comercio |
| GET | /repartidor/{id} | REPARTIDOR/ADMIN | Por repartidor |
| POST | / | USUARIO | Crear pedido |
| PATCH | /{id}/estado | ADMIN/REPARTIDOR | Cambiar estado |
| PUT | /{id}/repartidor/{idRep} | ADMIN | Asignar repartidor |

### Líneas Pedido (`/api/lineas-pedido`)
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | /pedido/{id} | Sí | Líneas de un pedido |
| POST | / | Sí | Crear línea |
| DELETE | /{id} | Sí | Eliminar línea |

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
- **CategoriaProducto:** `ENTRADA, PRINCIPAL, POSTRE, BEBIDA, MENU, OTRO`
- **UnidadMedida:** `UNIDAD, KILOGRAMO, GRAMO, LITRO, MILILITRO, PIEZA`
- **Vehiculo:** `BICICLETA, PATINETE`
