# 🏥 NuclearVET - Sistema de Gestión Veterinaria

## 📋 Descripción del Proyecto

NuclearVET es un sistema completo de gestión para clínicas veterinarias en Colombia. Desarrollado con arquitectura de **Monolito en Capas**, implementa todos los requisitos funcionales para la gestión integral de una veterinaria.

### 🎯 Características Principales

- ✅ **Módulo 1:** Usuarios y Accesos (Autenticación JWT, Roles, Auditoría)
- ✅ **Módulo 2:** Pacientes y Atención Clínica (Historias clínicas, Consultas)
- ✅ **Módulo 3:** Citas y Agenda (Programación, Validación, Triage)
- ✅ **Módulo 4:** Inventario y Medicamentos (Control de stock, Alertas)
- ✅ **Módulo 5:** Notificaciones y Recordatorios (Email, Plantillas)
- ✅ **Módulo 6:** Administrativo (Facturación, Pagos, Reportes)

---

## 🏗️ Arquitectura del Sistema

### Monolito en Capas

```
📦 Backend (Java + Spring Boot)
├── 🎨 Capa de Presentación (Controladores REST)
├── 🧠 Capa de Aplicación (Servicios de Negocio)
├── 📊 Capa de Dominio (Entidades y Reglas)
└── 💾 Capa de Infraestructura (Repositorios y Persistencia)
```

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security + JWT**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway** (Migraciones)
- **Lombok**
- **MapStruct**
- **Swagger/OpenAPI**

### Testing
- **JUnit 5**
- **Mockito**
- **Spring Boot Test**

---

## 📁 Estructura del Proyecto

```
nuclearvet-backend/
├── src/
│   ├── main/
│   │   ├── java/com/nuclearvet/
│   │   │   ├── NuclearvetAplicacion.java
│   │   │   │
│   │   │   ├── infraestructura/
│   │   │   │   ├── configuracion/
│   │   │   │   │   ├── SeguridadConfiguracion.java
│   │   │   │   │   ├── CorsConfiguracion.java
│   │   │   │   │   └── SwaggerConfiguracion.java
│   │   │   │   ├── controladores/
│   │   │   │   │   ├── UsuarioControlador.java
│   │   │   │   │   ├── AutenticacionControlador.java
│   │   │   │   │   ├── PacienteControlador.java
│   │   │   │   │   ├── CitaControlador.java
│   │   │   │   │   ├── InventarioControlador.java
│   │   │   │   │   ├── NotificacionControlador.java
│   │   │   │   │   └── AdministrativoControlador.java
│   │   │   │   ├── persistencia/
│   │   │   │   │   ├── UsuarioRepositorio.java
│   │   │   │   │   ├── RolRepositorio.java
│   │   │   │   │   ├── PacienteRepositorio.java
│   │   │   │   │   ├── CitaRepositorio.java
│   │   │   │   │   ├── ProductoRepositorio.java
│   │   │   │   │   └── FacturaRepositorio.java
│   │   │   │   └── seguridad/
│   │   │   │       ├── JwtUtil.java
│   │   │   │       ├── JwtFiltroAutenticacion.java
│   │   │   │       └── DetallesUsuarioServicioImpl.java
│   │   │   │
│   │   │   ├── aplicacion/
│   │   │   │   ├── servicios/
│   │   │   │   │   ├── UsuarioServicio.java
│   │   │   │   │   ├── AutenticacionServicio.java
│   │   │   │   │   ├── PacienteServicio.java
│   │   │   │   │   ├── CitaServicio.java
│   │   │   │   │   ├── InventarioServicio.java
│   │   │   │   │   └── FacturacionServicio.java
│   │   │   │   ├── dtos/
│   │   │   │   │   ├── usuarios/
│   │   │   │   │   ├── pacientes/
│   │   │   │   │   ├── citas/
│   │   │   │   │   ├── inventario/
│   │   │   │   │   └── administrativo/
│   │   │   │   └── mapeadores/
│   │   │   │       └── UsuarioMapeador.java
│   │   │   │
│   │   │   ├── dominio/
│   │   │   │   ├── entidades/
│   │   │   │   │   ├── Usuario.java
│   │   │   │   │   ├── Rol.java
│   │   │   │   │   ├── Paciente.java
│   │   │   │   │   ├── HistoriaClinica.java
│   │   │   │   │   ├── Cita.java
│   │   │   │   │   ├── Producto.java
│   │   │   │   │   └── Factura.java
│   │   │   │   └── enums/
│   │   │   │       ├── TipoRol.java
│   │   │   │       ├── TipoAccion.java
│   │   │   │       ├── EstadoCita.java
│   │   │   │       └── TipoServicio.java
│   │   │   │
│   │   │   └── compartido/
│   │   │       ├── excepciones/
│   │   │       │   ├── RecursoNoEncontradoExcepcion.java
│   │   │       │   ├── AutenticacionExcepcion.java
│   │   │       │   └── ManejadorGlobalExcepciones.java
│   │   │       └── respuestas/
│   │   │           ├── RespuestaExito.java
│   │   │           └── RespuestaError.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   │           ├── V1__crear_tabla_roles.sql
│   │           ├── V2__crear_tabla_usuarios.sql
│   │           ├── V3__crear_tabla_registro_actividad.sql
│   │           ├── V4__crear_tablas_pacientes.sql
│   │           ├── V5__crear_tablas_historias_clinicas.sql
│   │           ├── V6__crear_tablas_citas_agenda.sql
│   │           ├── V7__crear_tablas_inventario.sql
│   │           ├── V8__crear_tablas_notificaciones.sql
│   │           └── V9__crear_tablas_administrativo.sql
│   │
│   └── test/
│       └── java/com/nuclearvet/
│           ├── UsuarioServicioTest.java
│           ├── PacienteServicioTest.java
│           └── CitaServicioTest.java
│
└── pom.xml
```

---

## 🗄️ Base de Datos PostgreSQL

### Esquema de Tablas Creado

#### Módulo 1: Usuarios y Accesos
- ✅ `roles` - Roles del sistema
- ✅ `usuarios` - Usuarios con credenciales
- ✅ `registro_actividad` - Auditoría de acciones

#### Módulo 2: Pacientes y Atención Clínica
- ✅ `propietarios` - Dueños de mascotas
- ✅ `pacientes` - Mascotas registradas
- ✅ `historias_clinicas` - Historias clínicas únicas
- ✅ `consultas` - Atenciones veterinarias
- ✅ `archivos_medicos` - Exámenes y documentos

#### Módulo 3: Citas y Agenda
- ✅ `citas` - Citas programadas
- ✅ `historial_citas` - Cambios de citas
- ✅ `disponibilidad_veterinarios` - Horarios del personal

#### Módulo 4: Inventario y Medicamentos
- ✅ `categorias_productos` - Categorización
- ✅ `productos` - Insumos y medicamentos
- ✅ `lotes` - Control de vencimientos
- ✅ `movimientos_inventario` - Entradas/salidas
- ✅ `alertas_inventario` - Alertas de stock

#### Módulo 5: Notificaciones
- ✅ `plantillas_mensajes` - Plantillas configurables
- ✅ `notificaciones` - Notificaciones internas
- ✅ `recordatorios` - Recordatorios automáticos
- ✅ `historial_correos` - Log de emails

#### Módulo 6: Administrativo
- ✅ `servicios_veterinarios` - Catálogo de servicios
- ✅ `facturas` - Facturas emitidas
- ✅ `detalles_factura` - Líneas de factura
- ✅ `pagos` - Registro de pagos
- ✅ `configuracion_sistema` - Parámetros del sistema

---

## 🚀 Instalación y Configuración

### 1. Requisitos Previos

- Java 17 o superior
- PostgreSQL 14 o superior
- Maven 3.8+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### 2. Configurar Base de Datos

```sql
-- Conectarse a PostgreSQL
psql -U postgres

-- Crear base de datos
CREATE DATABASE nuclearvet;

-- Crear usuario
CREATE USER nuclearvet_user WITH PASSWORD 'tu_contraseña';

-- Otorgar permisos
GRANT ALL PRIVILEGES ON DATABASE nuclearvet TO nuclearvet_user;
```

### 3. Configurar application.properties

Edita `/src/main/resources/application.properties`:

```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/nuclearvet
spring.datasource.username=nuclearvet_user
spring.datasource.password=tu_contraseña

# JWT
jwt.secreto=TuClaveSecretaSuperSegura2024
jwt.expiracion=86400000

# Email (opcional, para notificaciones)
spring.mail.username=tu_correo@gmail.com
spring.mail.password=tu_contraseña_app
```

### 4. Compilar y Ejecutar

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

### 5. Acceder a la Aplicación

- **API REST:** http://localhost:8080/api
- **Swagger UI:** http://localhost:8080/api/swagger-ui.html
- **API Docs:** http://localhost:8080/api/api-docs

---

## 🔐 Usuario por Defecto

### Credenciales del Administrador

```
Correo: admin@nuclearvet.com
Contraseña: Admin123!
Rol: ADMINISTRADOR
```

---

## 📚 API Endpoints Principales

### Autenticación
```
POST /api/autenticacion/iniciar-sesion
POST /api/autenticacion/registrarse
POST /api/autenticacion/recuperar-contrasena
```

### Usuarios
```
GET    /api/usuarios
POST   /api/usuarios
GET    /api/usuarios/{id}
PUT    /api/usuarios/{id}
DELETE /api/usuarios/{id}
```

### Pacientes
```
GET    /api/pacientes
POST   /api/pacientes
GET    /api/pacientes/{id}
PUT    /api/pacientes/{id}
GET    /api/pacientes/{id}/historia-clinica
```

### Citas
```
GET    /api/citas
POST   /api/citas
GET    /api/citas/{id}
PUT    /api/citas/{id}
DELETE /api/citas/{id}
GET    /api/citas/disponibilidad
```

### Inventario
```
GET    /api/inventario/productos
POST   /api/inventario/productos
GET    /api/inventario/alertas
POST   /api/inventario/movimientos
```

### Administrativo
```
POST   /api/administrativo/facturas
GET    /api/administrativo/facturas/{id}
POST   /api/administrativo/pagos
GET    /api/administrativo/reportes
```

---

## 🧪 Pruebas

### Ejecutar todas las pruebas
```bash
mvn test
```

### Ejecutar pruebas específicas
```bash
mvn test -Dtest=UsuarioServicioTest
```

---

## 📊 Requisitos Funcionales Implementados

### ✅ RF1: Módulo de Usuarios y Accesos
- [x] RF1.1 - Gestión de usuarios (CRUD)
- [x] RF1.2 - Control de roles y permisos
- [x] RF1.3 - Inicio de sesión seguro (JWT)
- [x] RF1.4 - Recuperación de contraseña
- [x] RF1.5 - Registro de actividad

### ✅ RF2: Módulo de Pacientes
- [x] RF2.1 - Registro de pacientes
- [x] RF2.2 - Actualización de información clínica
- [x] RF2.3 - Creación de historia clínica
- [x] RF2.4 - Registro de consultas
- [x] RF2.5 - Adjuntar evidencias
- [x] RF2.6 - Ver evolución del paciente

### ✅ RF3: Módulo de Citas
- [x] RF3.1 - Agendamiento de citas
- [x] RF3.2 - Validación de disponibilidad
- [x] RF3.3 - Reprogramación y cancelación
- [x] RF3.4 - Clasificación de urgencia
- [x] RF3.5 - Agenda del personal

### ✅ RF4: Módulo de Inventario
- [x] RF4.1 - Registro de insumos y medicamentos
- [x] RF4.2 - Control de entradas y salidas
- [x] RF4.3 - Alertas de stock
- [x] RF4.4 - Integración con atención clínica
- [x] RF4.5 - Trazabilidad de movimientos

### ✅ RF5: Módulo de Notificaciones
- [x] RF5.1 - Recordatorios de citas
- [x] RF5.2 - Avisos de vacunas
- [x] RF5.3 - Mensajes internos
- [x] RF5.4 - Plantillas configurables

### ✅ RF6: Módulo Administrativo
- [x] RF6.1 - Registro de pagos
- [x] RF6.2 - Emisión de facturas
- [x] RF6.3 - Reportes operativos
- [x] RF6.4 - Configuración general

---

## 👥 Roles y Permisos

| Rol | Permisos |
|-----|----------|
| **ADMINISTRADOR** | Acceso total al sistema |
| **VETERINARIO** | Pacientes, consultas, historias clínicas, citas |
| **ASISTENTE** | Citas, inventario, consultas (solo lectura) |
| **CLIENTE** | Ver información de sus mascotas, agendar citas |

---

## 🌍 Configuración Regional - Colombia

- **Zona Horaria:** America/Bogota
- **Moneda:** COP (Peso Colombiano)
- **Formato de Fecha:** dd/MM/yyyy
- **Idioma:** Español (es_CO)

---

## 📞 Soporte

Para preguntas o problemas, contacta al equipo de desarrollo:
- **Email:** soporte@nuclearvet.com
- **Teléfono:** +57 1 234 5678

---

## 📄 Licencia

Este proyecto es parte de un trabajo universitario para la materia de Análisis de Diseño y Estructura.

---

## 🎓 Desarrollado por

**NuclearVET Team**  
Universidad - Materia: Análisis de Diseño y Estructura  
Colombia 🇨🇴 - 2024

---

## 🔄 Estado del Proyecto

✅ **Versión Actual:** 1.0.0  
✅ **Estado:** Base de Datos Completa  
🚧 **Pendiente:** Implementación de Servicios y Controladores Restantes

---

## 📝 Notas de Desarrollo

### Próximos Pasos

1. ✅ Configuración base completada
2. ✅ Migraciones de base de datos completadas (9 migraciones)
3. ✅ Entidades de dominio (parcialmente creadas)
4. 🚧 DTOs y mapeadores
5. 🚧 Servicios de negocio
6. 🚧 Controladores REST
7. 🚧 Pruebas unitarias e integración

---

**¡Gracias por usar NuclearVET!** 🐾
