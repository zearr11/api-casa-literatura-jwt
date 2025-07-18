# 📚 Sistema de Gestión de Biblioteca - Casa de la Literatura

## 🔍 Descripción General

Sistema integral de gestión bibliotecaria desarrollado en **Spring Boot** que permite administrar el catálogo de libros, gestionar préstamos y organizar recursos físicos por salas y colecciones. Diseñado específicamente para la Casa de la Literatura del Perú.

## 🚀 Tecnologías Utilizadas

- **Backend**: Spring Boot 3.5.3
- **Base de Datos**: MySQL 8.0
- **ORM**: Hibernate/JPA 6.6.18
- **Pool de Conexiones**: HikariCP
- **Documentación**: Spring Boot DevTools
- **Arquitectura**: API REST

## 📋 Funcionalidades Principales

### 1. 👤 Gestión de Autores
**Endpoint Base**: `/api/v1/autores`

| Método | Endpoint | Descripción | Request Body |
|--------|----------|-------------|--------------|
| `GET` | `/autores` | Listar todos los autores | - |
| `GET` | `/autores/{codigo}` | Obtener autor por código | - |
| `POST` | `/autores` | Registrar nuevo autor | `AutorDataSimpleDto` |
| `PUT` | `/autores/{codigo}` | Actualizar autor existente | `AutorDataSimpleDto` |

**Estructura de Datos**:
```json
{
  "nombre": "Gabriel García Márquez",
  "nacionalidad": "Colombiana"
}
```

### 2. 🏢 Gestión de Editoriales
**Endpoint Base**: `/api/v1/editoriales`

| Método | Endpoint | Descripción | Request Body |
|--------|----------|-------------|--------------|
| `GET` | `/editoriales` | Listar todas las editoriales | - |
| `GET` | `/editoriales/{codigo}` | Obtener editorial por código | - |
| `POST` | `/editoriales` | Registrar nueva editorial | `EditorialDataSimpleDto` |
| `PUT` | `/editoriales/{codigo}` | Actualizar editorial | `EditorialDataSimpleDto` |

### 3. 📂 Gestión de Colecciones
**Endpoint Base**: `/api/v1/colecciones`

| Método | Endpoint | Descripción | Request Body |
|--------|----------|-------------|--------------|
| `GET` | `/colecciones` | Listar todas las colecciones | - |
| `GET` | `/colecciones/{codigo}` | Obtener colección por código | - |
| `POST` | `/colecciones` | Registrar nueva colección | `ColeccionDataSimpleDto` |
| `PUT` | `/colecciones/{codigo}` | Actualizar colección | `ColeccionDataSimpleDto` |

### 4. 🏛️ Gestión de Salas
**Endpoint Base**: `/api/v1/salas`

| Método | Endpoint | Descripción | Request Body |
|--------|----------|-------------|--------------|
| `GET` | `/salas` | Listar todas las salas | - |
| `GET` | `/salas/{codigo}` | Obtener sala por código | - |
| `GET` | `/salas/colecciones` | Listar salas con sus colecciones | - |
| `GET` | `/salas/colecciones/{codigo}` | Obtener sala con colecciones | - |
| `POST` | `/salas` | Registrar nueva sala | `SalaDataSimpleDto` |
| `PUT` | `/salas/{codigo}` | Actualizar sala | `SalaDataSimpleDto` |

### 5. 👥 Gestión de Clientes
**Endpoint Base**: `/api/v1/clientes`

| Método | Endpoint | Descripción | Request Body |
| :--- | :--- | :--- | :--- |
| `GET` | `/clientes` | Listar todos los clientes | - |
| `GET` | `/clientes/codigo/{codigo}` | Obtener cliente por código | - |
| `GET` | `/clientes/numero-doc/{numDoc}`| Obtener cliente por documento | - |
| `POST` | `/clientes` | Registrar nuevo cliente | **`Multipart Form Data` con:** <br> • `datosCliente` (JSON) <br> • `imgDocIdentidad` (Archivo) <br> • `imgRecServicio` (Archivo) |
| `PUT` | `/clientes/{codigo}` | Actualizar cliente | **`Multipart Form Data` con:** <br> • `datosCliente` (JSON) <br> • `imgDocIdentidad` (**Opcional**) <br> • `imgRecServicio` (**Opcional**) |

**Nota**: Los endpoints de clientes requieren archivos multimedia:
- `datosCliente`: JSON con información del cliente
- `imgDocIdentidad`: Imagen del documento de identidad
- `imgRecServicio`: Imagen del recibo de servicio

### 6. 📖 Gestión de Libros (Funcionalidad Principal)
**Endpoint Base**: `/api/v1/libros`

#### 📊 Consultas de Resumen
| Método | Endpoint | Descripción | Parámetros de Consulta |
|--------|----------|-------------|------------------------|
| `GET` | `/libros` | Resumen de libros con cantidades | `titulo`, `isbn`, `year`, `autor`, `editorial`, `coleccion`, `sala`, `cantidadCopias`, `cantidadDisponibles`, `cantidadPrestados`, `cantidadSoloLectura` |
| `GET` | `/libros/{codigo}` | Resumen de libro específico | - |

#### 📋 Consultas de Copias
| Método | Endpoint | Descripción | Parámetros de Consulta |
|--------|----------|-------------|------------------------|
| `GET` | `/libros/copias` | Listar copias con estados | `isbn`, `titulo`, `year`, `autor`, `editorial`, `coleccion`, `sala`, `numeroCopia`, `estado` |
| `GET` | `/libros/copias/{codigo}` | Copias de un libro específico | - |
| `GET` | `/libros/copias/{codigo}/{numeroCopia}` | Obtener copia específica | - |

#### ✏️ Operaciones de Escritura
| Método | Endpoint | Descripción | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/libros` | Registrar nuevo libro | `LibroDetalleNuevoDto` |
| `PUT` | `/libros/{codigo}` | Actualizar libro | `LibroDetalleNuevoDto` |
| `POST` | `/libros/copias` | Registrar nueva copia | `LibroCopiaNuevoDto` |

**Estructura para Nuevo Libro**:
```json
{
  "isbn": "978-3-16-148410-0",
  "titulo": "Cien años de soledad",
  "year": 1967,
  "codigoAutor": "AT00001",
  "codigoColeccion": "CC00001",
  "codigoEditorial": "ED00001"
}
```

### 7. 📅 Gestión de Préstamos
**Endpoint Base**: `/api/v1/prestamos`

| Método | Endpoint | Descripción | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/prestamos` | Registrar nuevo préstamo | `PrestamoDataDto` |

### 8. 💭 Sistema de Citas Célebres
**Endpoint Base**: `/api/v1/citas`

| Método | Endpoint | Descripción | Request Body |
|--------|----------|-------------|--------------|
| `GET` | `/citas` | Obtener cita aleatoria | - |

## 🔧 Estados de Libros

El sistema maneja tres estados principales para las copias de libros:

- **DISPONIBLE**: Copia disponible para préstamo
- **PRESTADO**: Copia actualmente prestada a un usuario
- **SOLO_PARA_LECTURA_EN_SALA**: Copia disponible solo para lectura en sala

## 🗂️ Estructura de Códigos

El sistema utiliza códigos alfanuméricos únicos para cada entidad:

- **Autores**: `AT00001`, `AT00002`, etc.
- **Editoriales**: `ED00001`, `ED00002`, etc.
- **Colecciones**: `CC00001`, `CC00002`, etc.
- **Salas**: `SL00001`, `SL00002`, etc.
- **Clientes**: `CL00001`, `CL00002`, etc.
- **Libros**: `LB00001`, `LB00002`, etc.

## 🚀 Cómo Ejecutar el Proyecto

### Prerrequisitos
- Java 17 o superior
- MySQL 8.0
- Maven 3.6+

### Configuración de Base de Datos
1. Crear base de datos MySQL llamada `biblioteca`
2. Configurar credenciales en `application.properties`
3. El sistema creará automáticamente las tablas al iniciar

### Ejecución
```bash
# Clonar el repositorio
git clone [URL_DEL_REPOSITORIO]

# Navegar al directorio
cd biblioteca

# Ejecutar con Maven
mvn spring-boot:run
```

El servidor estará disponible en: `http://localhost:8080`

## 📡 Testing de APIs

### Ejemplos de Uso

#### Registrar un Autor
```bash
curl -X POST http://localhost:8080/api/v1/autores \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Mario Vargas Llosa",
    "nacionalidad": "Peruana"
  }'
```

#### Buscar Libros por Filtros
```bash
curl "http://localhost:8080/api/v1/libros?titulo=Cien&autor=García&year=1967"
```

#### Consultar Estado de Copias
```bash
http://localhost:8080/api/v1/libros/copias?estado=DISPONIBLE&sala=Biblioteca Mario Vargas Llosa
```

## 🏗️ Arquitectura del Sistema

### Capas de la Aplicación
- **Controllers**: Manejo de requests HTTP y responses
- **Services**: Lógica de negocio
- **Repositories**: Acceso a datos con JPA
- **DTOs**: Transferencia de datos entre capas
- **Models**: Entidades de base de datos

### Características Técnicas
- **Pool de Conexiones**: HikariCP para optimización de BD
- **Manejo de Archivos**: Soporte para carga de imágenes
- **Validaciones**: Integridad de datos y reglas de negocio
- **Filtros Avanzados**: Búsquedas complejas con múltiples criterios

## 🐛 Solución de Problemas Comunes

### Error de Conexión a MySQL
- Verificar que MySQL esté corriendo
- Comprobar credenciales en `application.properties`
- Asegurar que la base de datos `biblioteca` exista

### Error de Generación de IDs
- Los códigos de entidades se generan automáticamente
- No incluir el campo `id` o `codigo` en las peticiones POST

### Error de Carga de Archivos
- Verificar que los archivos no excedan el tamaño máximo
- Usar `multipart/form-data` para endpoints de clientes

## 🤝 Contribución

Para contribuir al proyecto:
1. Fork del repositorio
2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit de cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## 📝 Licencia

Este proyecto está desarrollado para la Casa de la Literatura del Perú.

---
