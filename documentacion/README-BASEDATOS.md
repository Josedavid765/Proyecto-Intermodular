# EcoDrop - Base de Datos

## Configuración
- **Motor:** MySQL
- **Base de datos:** `ecodrop`
- **Puerto:** 3306
- **Usuario:** `root`
- **Contraseña:** (vacía)
- **DDL:** `spring.jpa.hibernate.ddl-auto=create` (las tablas se crean automáticamente al iniciar)

## Diagrama de Entidades

### usuarios
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id_usuario | BIGINT | PK, AUTO_INCREMENT |
| nombre | VARCHAR(50) | NOT NULL |
| apellido | VARCHAR(50) | NOT NULL |
| email | VARCHAR(255) | UNIQUE, NOT NULL |
| telefono | VARCHAR(15) | NOT NULL |
| direccion_entrega | VARCHAR(255) | NOT NULL |
| password | VARCHAR(255) | NOT NULL |
| rol | ENUM('ROLE_USUARIO','ROLE_COMERCIO','ROLE_REPARTIDOR','ROLE_ADMIN') | NOT NULL |

### comercio_local
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| idcomercio | BIGINT | PK, AUTO_INCREMENT |
| nombre_comercio | VARCHAR(100) | NOT NULL |
| categoria | VARCHAR(255) | NOT NULL |
| direccion_comercio | VARCHAR(255) | NOT NULL |
| logo | VARCHAR(255) | NULL |
| telefono | VARCHAR(9) | NOT NULL |
| horario_apertura | VARCHAR(255) | NOT NULL |
| id_usuario | BIGINT | FK → usuarios.id_usuario, NOT NULL |

### productos
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id_producto | BIGINT | PK, AUTO_INCREMENT |
| nombre | VARCHAR(255) | NOT NULL |
| descripcion | VARCHAR(255) | NULL |
| precio_unitario | DOUBLE | NOT NULL |
| stock | INT | NOT NULL |
| categoria_producto | ENUM('ENTRADA','PRINCIPAL','POSTRE','BEBIDA','MENU','OTRO') | NOT NULL |
| unidad_medida | ENUM('UNIDAD','KILOGRAMO','GRAMO','LITRO','MILILITRO','PIEZA') | NOT NULL |
| disponibilidad | BIT | NULL |
| imagen | VARCHAR(255) | NULL |
| id_comercio | BIGINT | FK → comercio_local.idcomercio, NOT NULL |

### pedido
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id_pedido | BIGINT | PK, AUTO_INCREMENT |
| fecha_pedido | DATE | NOT NULL |
| total | DOUBLE | NOT NULL |
| gastos_envio | DOUBLE | NOT NULL |
| estado | ENUM('PENDIENTE','EN_TRANSITO','ENTREGADO') | NOT NULL |
| direccion_entrega | VARCHAR(255) | NOT NULL |
| id_usuario | BIGINT | FK → usuarios.id_usuario, NOT NULL |
| id_comercio | BIGINT | FK → comercio_local.idcomercio, NOT NULL |
| id_repartidor | BIGINT | FK → repartidor.id_repartidor, NULL |

### lineas_pedido
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id_linea_pedido | BIGINT | PK, AUTO_INCREMENT |
| cantidad | INT | NOT NULL |
| precio_venta | DOUBLE | NOT NULL |
| id_pedido | BIGINT | FK → pedido.id_pedido, NOT NULL |
| id_producto | BIGINT | FK → productos.id_producto, NOT NULL |

### repartidor
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id_repartidor | BIGINT | PK, AUTO_INCREMENT |
| nombre | VARCHAR(255) | NOT NULL |
| apellidos | VARCHAR(255) | NOT NULL |
| telefono | VARCHAR(9) | NOT NULL |
| vehiculo | ENUM('BICICLETA','PATINETE') | NOT NULL |
| disponibilidad | BIT | NULL |
| estado | ENUM('DISPONIBLE','OCUPADO') | NOT NULL |
| id_usuario | BIGINT | FK → usuarios.id_usuario |

## Relaciones
- **Usuario 1→N Pedido** (un usuario puede tener varios pedidos)
- **ComercioLocal 1→N Pedido** (un comercio puede tener varios pedidos)
- **ComercioLocal 1→N Producto** (un comercio tiene varios productos)
- **Pedido 1→N LineaPedido** (un pedido tiene varias líneas)
- **Producto 1→N LineaPedido** (un producto puede estar en varias líneas)
- **Repartidor 1→N Pedido** (un repartidor puede tener varios pedidos)
- **Usuario 1→1 ComercioLocal** (un usuario puede gestionar un comercio)
- **Usuario 1→1 Repartidor** (un usuario puede ser un repartidor)
