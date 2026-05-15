# Informe Completo de Bugs — EcoDrop Frontend + Backend

---

## 🔴 BUG RAÍZ — El JWT se regenera en cada reinicio del servidor → todos los tokens anteriores son inválidos

**Archivo backend:** `src/main/java/com/ecodrop/backend/Security/JwtUtils.java`

### Por qué esto rompe TODO

Esta es la causa de que los dashboards no carguen datos. Mira esta línea:

```java
// LÍNEA ACTUAL (línea 10 de JwtUtils.java):
private final Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
```

`Keys.secretKeyFor()` genera una clave **aleatoria nueva cada vez que arranca el servidor**. Esto significa:

- El usuario inicia sesión → el backend genera un token con la clave A
- El servidor se reinicia (o se redespliega) → ahora usa la clave B
- El usuario vuelve a la página con el token antiguo → el backend lo valida con la clave B → **falla silenciosamente**
- El `JwtAuthenticationFilter` no autentica al usuario → Spring Security lo trata como anónimo → las rutas con `@PreAuthorize` devuelven **403 Forbidden**
- El frontend recibe el 403, el `error: () => {}` del subscribe silencia el error, `cargando` nunca se pone a `false`, y la pantalla se queda bloqueada en "Cargando..."

Además, en desarrollo es peor: si el servidor está en hot-reload (Spring DevTools), **cada vez que guardas un archivo Java, el token queda inválido**.

### Solución paso a paso

**Paso 1** — Genera una clave segura fija. Ejecuta esto en cualquier terminal:

```bash
openssl rand -base64 64
```

Copia el resultado. Será algo como: `kJ3mN8pQ2rT5vX0yA1bD4fH7jL9nR2sU5wZ8cE1gI4kM7oP0qS3uV6xY9zA2bC5`

**Paso 2** — Abre `src/main/resources/application.properties` y añade al final:

```properties
jwt.secret=kJ3mN8pQ2rT5vX0yA1bD4fH7jL9nR2sU5wZ8cE1gI4kM7oP0qS3uV6xY9zA2bC5
jwt.expiration=86400000
```

(Sustituye el valor por el que generaste en el Paso 1)

**Paso 3** — Abre `JwtUtils.java` y **reemplaza todo el contenido** con:

```java
package com.ecodrop.backend.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    private final Key key;
    private final int jwtExpirationMs;

    public JwtUtils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") int expiration) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.jwtExpirationMs = expiration;
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.JwtException e) {
            return false;
        }
    }
}
```

> Con esto el token siempre se valida con la misma clave, independientemente de cuántas veces se reinicie el servidor.

---

## 🔴 BUG 2 — El frontend silencia todos los errores HTTP → "Cargando..." permanente

**Archivos frontend:** `comercio-dashboard.ts`, `repartidor-dashboard.ts`, `detalle-pedido.ts`

### Diagnóstico

Cuando el backend devuelve un 403 (token inválido) o cualquier otro error, los bloques `error:` de los subscribes o bien están vacíos o no ponen `cargando = false`. El resultado es que la pantalla se queda bloqueada mostrando el spinner para siempre sin decirle nada al usuario.

Además hay un segundo problema: cuando `confirmarCreacion()` llama a `cargarPedidos()` tras crear un pedido, esa segunda llamada a `cargarPedidos()` no pone `cargando = true` al inicio, pero sí pone `cargando = false` al terminar. Esto es correcto. Sin embargo, si esa segunda llamada falla, `cargando` queda `false` y la tabla simplemente no se actualiza sin ningún aviso.

### Solución — `comercio-dashboard.ts`

**Reemplaza el método `cargarPedidos()` entero** con esta versión que maneja correctamente todos los estados:

```typescript
cargarPedidos(): void {
  this.pedidoService.getPedidosComercio().subscribe({
    next: (data) => {
      this.pedidos = data;
      this.cargando = false;
    },
    error: (err) => {
      this.cargando = false;
      // Si es 403, el token es inválido — redirigir al login
      if (err.status === 403 || err.status === 401) {
        this.error = 'Sesión expirada. Vuelve a iniciar sesión.';
      } else {
        this.error = 'Error al cargar pedidos: ' + (err.error?.message || err.message);
      }
    }
  });
}
```

**Reemplaza también `cargarComercio()`:**

```typescript
private cargarComercio(): void {
  this.cargando = true;
  this.comercioService.getMiComercio().subscribe({
    next: (c) => {
      this.comercio = c;
      this.cargarPedidos();
    },
    error: (err) => {
      this.cargando = false;
      if (err.status === 403 || err.status === 401) {
        this.error = 'Sesión expirada. Vuelve a iniciar sesión.';
      } else {
        this.error = 'Error al cargar comercio: ' + (err.error?.message || err.message);
      }
    }
  });
}
```

### Solución — `repartidor-dashboard.ts`

**Reemplaza `cargarPedidos()` entero:**

```typescript
cargarPedidos(): void {
  if (!this.repartidor?.idRepartidor) {
    this.error = 'No se pudo obtener el ID del repartidor';
    this.cargando = false;
    return;
  }
  this.cargando = true;
  const id = this.repartidor.idRepartidor;

  this.pedidoService.getPedidosDisponibles().subscribe({
    next: (data) => { this.disponibles = data; },
    error: (err) => {
      if (err.status === 403 || err.status === 401) {
        this.error = 'Sesión expirada. Vuelve a iniciar sesión.';
      } else {
        this.error = 'Error al cargar pedidos disponibles: ' + (err.error?.message || err.message);
      }
    }
  });

  this.pedidoService.getPedidosRepartidor(id).subscribe({
    next: (data) => {
      this.misPedidos = data;
      this.cargando = false;
    },
    error: (err) => {
      this.cargando = false;
      if (err.status === 403 || err.status === 401) {
        this.error = 'Sesión expirada. Vuelve a iniciar sesión.';
      } else {
        this.error = 'Error al cargar mis pedidos: ' + (err.error?.message || err.message);
      }
    }
  });
}
```

**Reemplaza también `cargarRepartidor()` para que ponga `cargando = false` en el error:**

```typescript
private cargarRepartidor(): void {
  this.cargando = true;
  this.repartidorService.getMiPerfil().subscribe({
    next: (r) => {
      this.repartidor = r;
      this.cargarPedidos(); // cargarPedidos vuelve a poner cargando = true, está bien
    },
    error: (err) => {
      this.cargando = false;
      if (err.status === 403 || err.status === 401) {
        this.error = 'Sesión expirada. Vuelve a iniciar sesión.';
      } else {
        this.error = 'Error al cargar perfil: ' + (err.error?.message || err.message);
      }
    }
  });
}
```

### Solución — `detalle-pedido.ts`

El detalle-pedido ya maneja correctamente `cargando = false` en ambos casos. Solo añade el manejo del 403:

**Reemplaza el bloque `error:` dentro de `cargarPedido()`:**

```typescript
error: (err) => {
  if (err.status === 403 || err.status === 401) {
    this.error = 'Sesión expirada. Vuelve a iniciar sesión.';
  } else {
    this.error = 'Error al cargar el pedido. Es posible que no exista o no tengas permisos.';
  }
  this.cargando = false;
}
```

---

## 🔴 BUG 3 — `detalle-pedido.html` tiene el contenido duplicado completo

**Archivo:** `fe/frontend revisar/src/app/pedidos/components/detalle-pedido/detalle-pedido.html`

### Diagnóstico

El archivo HTML del detalle-pedido tiene **todo el bloque `<div class="detalle-container">` dos veces**. La segunda copia empieza en la línea 80 aproximadamente, justo después del `</div>` que cierra la primera. Esto hace que Angular renderice el componente doble, con posibles errores de binding y comportamiento impredecible.

### Solución

**Abre `detalle-pedido.html`** y elimina todo desde la segunda aparición de:
```html
<div *ngIf="!cargando && pedido" class="detalle-container">
```
hasta el final del archivo. El archivo debe terminar con el primer `</div>` que cierra ese contenedor. El contenido correcto y definitivo del archivo es:

```html
<div *ngIf="error" class="mensaje error" (click)="error = null">{{ error }}</div>
<div *ngIf="mensajeExito" class="mensaje exito" (click)="mensajeExito = null">{{ mensajeExito }}</div>
<div *ngIf="cargando" class="mensaje">Cargando pedido...</div>
<div *ngIf="!cargando && !pedido && !error" class="mensaje">No se encontró el pedido.</div>

<div *ngIf="!cargando && pedido" class="detalle-container">
  <div class="detalle-header">
    <button class="btn-volver" (click)="volver()">← Volver</button>
    <h1>Pedido #{{ pedido?.idPedido }}</h1>
  </div>

  <div class="detalle-card" *ngIf="!editando">
    <div class="detalle-grid">
      <div class="detalle-item">
        <span class="detalle-label">Nombre</span>
        <span class="detalle-value">{{ pedido?.nombre }}</span>
      </div>
      <div class="detalle-item">
        <span class="detalle-label">Fecha</span>
        <span class="detalle-value">{{ pedido?.fechaPedido | date:'dd/MM/yyyy' }}</span>
      </div>
      <div class="detalle-item">
        <span class="detalle-label">Estado</span>
        <span class="detalle-value">
          <span class="badge-estado"
            [class.pendiente]="pedido?.estado === 'PENDIENTE'"
            [class.transito]="pedido?.estado === 'EN_TRANSITO'"
            [class.entregado]="pedido?.estado === 'ENTREGADO'">
            {{ pedido?.estado === 'EN_TRANSITO' ? 'EN REPARTO' : pedido?.estado }}
          </span>
        </span>
      </div>
      <div class="detalle-item">
        <span class="detalle-label">Peso</span>
        <span class="detalle-value">{{ pedido?.peso }} kg</span>
      </div>
      <div class="detalle-item">
        <span class="detalle-label">Dirección de Recogida</span>
        <span class="detalle-value">{{ pedido?.direccionRecogida }}</span>
      </div>
      <div class="detalle-item">
        <span class="detalle-label">Dirección de Entrega</span>
        <span class="detalle-value">{{ pedido?.direccionEntrega }}</span>
      </div>
      <div class="detalle-item" *ngIf="pedido?.nombreComercio">
        <span class="detalle-label">Comercio</span>
        <span class="detalle-value">{{ pedido?.nombreComercio }}</span>
      </div>
      <div class="detalle-item" *ngIf="pedido?.nombreRepartidor">
        <span class="detalle-label">Repartidor</span>
        <span class="detalle-value">{{ pedido?.nombreRepartidor }}</span>
      </div>
      <div class="detalle-item" *ngIf="pedido?.distancia">
        <span class="detalle-label">Distancia</span>
        <span class="detalle-value">{{ pedido?.distancia }} km</span>
      </div>
    </div>

    <div class="detalle-actions" *ngIf="pedido?.estado !== 'ENTREGADO' && esComercio">
      <button class="btn-edit" (click)="iniciarEdicion()">Modificar Pedido</button>
      <button class="btn-delete" (click)="mostrandoConfirmacionEliminar = true">Eliminar Pedido</button>
    </div>
  </div>

  <div class="detalle-card edit-card" *ngIf="editando">
    <h3>Modificar Pedido</h3>
    <div class="form-grid">
      <label>
        Nombre del Pedido
        <input type="text" [(ngModel)]="editData.nombre" required>
      </label>
      <label>
        Peso (kg)
        <input type="number" step="0.1" min="0" [(ngModel)]="editData.peso" required>
      </label>
      <label class="full-width">
        Dirección de Recogida
        <input type="text" [(ngModel)]="editData.direccionRecogida" required>
      </label>
      <label class="full-width">
        Dirección de Entrega
        <input type="text" [(ngModel)]="editData.direccionEntrega" required>
      </label>
    </div>
    <div class="form-actions">
      <button class="btn-primario" (click)="guardarCambios()" [disabled]="guardando">
        {{ guardando ? 'Guardando...' : 'Guardar Cambios' }}
      </button>
      <button class="btn-secundario" (click)="cancelarEdicion()">Cancelar</button>
    </div>
  </div>

  <div class="modal-overlay" *ngIf="mostrandoConfirmacionEliminar" (click)="mostrandoConfirmacionEliminar = false">
    <div class="modal" (click)="$event.stopPropagation()">
      <h3>¿Eliminar pedido?</h3>
      <p>Esta acción no se puede deshacer.</p>
      <div class="form-actions">
        <button class="btn-delete" (click)="eliminarPedido()">Eliminar</button>
        <button class="btn-secundario" (click)="mostrandoConfirmacionEliminar = false">Cancelar</button>
      </div>
    </div>
  </div>
</div>
```

---

## 🔴 BUG 4 — `ComercioController.GET /me` no tiene `@PreAuthorize` — cualquiera puede llamarlo

**Archivo backend:** `Controller/ComercioController.java`

### Diagnóstico

```java
@GetMapping("/me")
// ← No tiene @PreAuthorize
public ResponseEntity<ComercioLocalDTO> obtenerPerfil() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(comercioService.obtenerPorEmail(email));
}
```

Si un repartidor (o cualquier usuario autenticado) llama a `/api/comercios/me`, Spring Security deja pasar la petición porque no hay restricción de rol. El `SecurityContextHolder` devolverá el email del repartidor, y `obtenerPorEmail` buscará ese email en la tabla de comercios → lanzará `RecursoNoEncontrado` → 404 al frontend. Esto podría causar comportamientos raros si alguna parte del frontend llama a este endpoint siendo repartidor.

### Solución

**Abre `ComercioController.java`** y añade `@PreAuthorize` al método `obtenerPerfil()`:

```java
// ANTES:
@GetMapping("/me")
public ResponseEntity<ComercioLocalDTO> obtenerPerfil() {

// DESPUÉS:
@GetMapping("/me")
@PreAuthorize("hasRole('COMERCIO')")
public ResponseEntity<ComercioLocalDTO> obtenerPerfil() {
```

---

## 🟡 BUG 5 — El servicio frontend llama a `/repartidor/{id}` pero debería usar `/repartidor/mis-pedidos`

**Archivo frontend:** `src/app/services/pedido.ts`

### Diagnóstico

El backend tiene **dos endpoints** para obtener los pedidos de un repartidor:

- `GET /api/pedidos/repartidor/{idRepartidor}` — busca por ID, accesible para cualquier repartidor autenticado
- `GET /api/pedidos/repartidor/mis-pedidos` — busca por el email del token JWT actual

El frontend usa el primero, pasando el `idRepartidor` que obtiene de `getMiPerfil()`. Esto funciona, pero tiene un problema de seguridad: cualquier repartidor podría teóricamente ver los pedidos de otro repartidor si conoce su ID. El endpoint `mis-pedidos` es más seguro porque usa el JWT para identificar al repartidor.

### Solución

**Abre `src/app/services/pedido.ts`** y añade el método más seguro:

```typescript
// AÑADIR este método (más seguro, no requiere conocer el ID):
getMisPedidosRepartidor(): Observable<Pedido[]> {
  return this.http.get<Pedido[]>(`${this.apiUrl}/repartidor/mis-pedidos`);
}
```

Luego en `repartidor-dashboard.ts`, en `cargarPedidos()`, **reemplaza**:
```typescript
// ANTES:
this.pedidoService.getPedidosRepartidor(id).subscribe({

// DESPUÉS:
this.pedidoService.getMisPedidosRepartidor().subscribe({
```

Y en `aceptarReparto()`, **reemplaza igualmente**:
```typescript
// ANTES:
this.pedidoService.getPedidosRepartidor(idRepartidor).subscribe({

// DESPUÉS:
this.pedidoService.getMisPedidosRepartidor().subscribe({
```

Con esto, tampoco necesitas pasar el `id` como parámetro y eliminas el riesgo de exposición de datos.

---

## 🟡 BUG 6 — `confirmarCreacion()` no pone `cargando = true` → la tabla se actualiza sin spinner

**Archivo:** `comercio-dashboard.ts`

### Diagnóstico

Cuando el comercio crea un pedido y confirma, `confirmarCreacion()` llama a `cargarPedidos()` al terminar. Pero en ese momento `cargando` es `false` (se puso a `false` al cargar inicialmente), así que la tabla muestra los datos viejos hasta que llega la respuesta, sin ningún indicador visual.

### Solución

**En `confirmarCreacion()`**, añade `this.cargando = true` justo antes de llamar a `cargarPedidos()`:

```typescript
next: () => {
  this.nuevoPedido = { nombre: '', direccionEntrega: '', peso: null };
  this.creando = false;
  this.mostrarFormulario = false;
  this.mensajeExito = 'Pedido creado correctamente';
  this.cargando = true;   // ← AÑADIR ESTA LÍNEA
  this.cargarPedidos();
},
```

---

## 🟡 BUG 7 — `GeocodingService` puede bloquear `crearPedido()` si el servicio externo falla lentamente

**Archivo backend:** `Service/PedidoService.java`

### Diagnóstico

En `crearPedido()`, la geocodificación está dentro de un `try/catch` correcto, pero si el servicio de geocodificación tarda mucho (timeout de red), la petición del frontend se queda esperando durante ese tiempo antes de recibir respuesta. El pedido ya se guarda antes del try/catch, así que no se pierde, pero el usuario puede ver un spinner durante varios segundos al crear un pedido.

### Solución recomendada

**Abre `PedidoService.java`** y mueve la geocodificación a un proceso asíncrono. Añade la anotación `@Async` sobre el bloque de geocodificación extrayéndolo a un método separado. La solución más sencilla sin refactoring grande es añadir un timeout al cliente HTTP del `GeocodingService`, pero la mejora mínima es ya garantizar que el pedido se devuelve inmediatamente:

```java
// En crearPedido(), el pedido se guarda primero (ya lo hace):
pedido = pedidoRepository.save(pedido);  // guardado antes de geocodificar ✓

// El try/catch de geocodificación ya existe — solo añadir un log más claro:
} catch (Exception e) {
    // No bloquear al usuario — el pedido ya está guardado
    System.err.println("⚠️ Geocodificación omitida para pedido " + pedido.getIdPedido() + ": " + e.getMessage());
}

// Devolver el pedido inmediatamente tras el segundo save (ya lo hace):
pedido = pedidoRepository.save(pedido);
return mapToDTO(pedido);
```

El código actual ya hace esto correctamente. Solo hay que asegurarse de que `GeocodingService` tiene configurado un **timeout** en su cliente HTTP para que no espere indefinidamente.

---

## Resumen de cambios por prioridad

| Prioridad | Archivo | Cambio |
|---|---|---|
| 🔴 1 | `Security/JwtUtils.java` | Clave JWT fija desde `application.properties` |
| 🔴 1 | `src/main/resources/application.properties` | Añadir `jwt.secret` y `jwt.expiration` |
| 🔴 2 | `comercio-dashboard.ts` | Manejar errores 401/403 en `cargarComercio` y `cargarPedidos` |
| 🔴 2 | `repartidor-dashboard.ts` | Manejar errores 401/403 en `cargarRepartidor` y `cargarPedidos` |
| 🔴 2 | `detalle-pedido.ts` | Manejar errores 401/403 en `cargarPedido` |
| 🔴 3 | `detalle-pedido.html` | Eliminar el bloque `detalle-container` duplicado (línea ~80 al final) |
| 🔴 4 | `Controller/ComercioController.java` | Añadir `@PreAuthorize("hasRole('COMERCIO')")` a `GET /me` |
| 🟡 5 | `services/pedido.ts` + `repartidor-dashboard.ts` | Usar `getMisPedidosRepartidor()` en lugar de `getPedidosRepartidor(id)` |
| 🟡 6 | `comercio-dashboard.ts` | Añadir `cargando = true` antes de `cargarPedidos()` en `confirmarCreacion()` |

---

## Orden de aplicación recomendado

1. **Primero el backend**: Corrige `JwtUtils.java` y `application.properties` (Bug 1) y `ComercioController.java` (Bug 4). Reinicia el servidor.
2. **Luego el frontend**: Aplica el manejo de errores en los tres componentes (Bug 2), elimina el HTML duplicado (Bug 3), y aplica las mejoras de Bug 5 y Bug 6.
3. **Prueba**: Abre DevTools → pestaña Network. Al iniciar sesión deberías ver las peticiones a `/api/comercios/me` y `/api/pedidos/comercio/me` devolviendo **200**, no 403. Si sigues viendo 403, borra el token del localStorage y vuelve a iniciar sesión.
