# Sistema de Liquidación de Comisiones - Backend

Sistema backend para el procesamiento de transacciones financieras y liquidación automática de comisiones basado en reglas de negocio dinámicas.

## Información del Proyecto

- **Nombre**: Sofka-Fintech Backend
- **Versión**: 1.0.0
- **Fecha**: Enero 2025
- **Repositorio**: https://github.com/FernandoMartinez018/PruebaSoftkaBackend

## Tabla de Contenidos

1. [Descripción](#descripción)
2. [Tecnologías](#tecnologías)
3. [Requisitos Previos](#requisitos-previos)
4. [Instalación](#instalación)
5. [Configuración](#configuración)
6. [Ejecución](#ejecución)
7. [Arquitectura](#arquitectura)
8. [API Endpoints](#api-endpoints)
9. [Reglas de Negocio](#reglas-de-negocio)
10. [Pruebas Unitarias](#pruebas-unitarias)
11. [Base de Datos](#base-de-datos)
12. [Principios de Diseño](#principios-de-diseño)

## Descripción

Este sistema permite el registro y procesamiento de transacciones financieras con cálculo automático de comisiones. Implementa programación reactiva mediante Spring WebFlux para el manejo eficiente de operaciones asíncronas y persistencia de datos mediante R2DBC.

### Características Principales

- Registro de transacciones financieras
- Cálculo automático de comisiones basado en reglas de negocio
- API REST reactiva con Spring WebFlux
- Persistencia de datos con R2DBC
- Manejo global de errores
- Validaciones de entrada
- Arquitectura limpia siguiendo principios SOLID

## Tecnologías

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring WebFlux**: Programación reactiva
- **Spring Data R2DBC**: Acceso reactivo a base de datos
- **H2 Database**: Base de datos en memoria
- **Lombok**: Reducción de código boilerplate
- **Maven**: Gestión de dependencias
- **JUnit 5**: Framework de pruebas
- **Mockito**: Framework de mocking para pruebas

## Requisitos Previos

- JDK 17 o superior
- Maven 3.6 o superior
- IDE recomendado: IntelliJ IDEA, Eclipse o VS Code

## Instalación

### Clonar el Repositorio

```bash
git clone https://github.com/FernandoMartinez018/PruebaSoftkaBackend.git
cd PruebaSoftkaBackend
```

### Instalar Dependencias

```bash
mvn clean install
```

## Configuración

La configuración de la aplicación se encuentra en `src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: fintech-backend
  
  r2dbc:
    url: r2dbc:h2:mem:///testdb;DB_CLOSE_DELAY=-1
    username: sa
    password: 
  
  h2:
    console:
      enabled: true
      path: /h2-console

server:
  port: 8080

logging:
  level:
    com.sofka: DEBUG
```

### Variables de Entorno

Aunque la aplicación funciona con la configuración por defecto, se pueden sobrescribir mediante variables de entorno:

- `SERVER_PORT`: Puerto del servidor (por defecto: 8080)
- `SPRING_R2DBC_URL`: URL de conexión a la base de datos
- `SPRING_R2DBC_USERNAME`: Usuario de la base de datos
- `SPRING_R2DBC_PASSWORD`: Contraseña de la base de datos

## Ejecución

### Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

### Ejecutar en Modo Desarrollo

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Generar JAR

```bash
mvn clean package
java -jar target/fintech-backend-1.0.0.jar
```

## Arquitectura

### Estructura del Proyecto

```
src/main/java/com/sofka/fintech/
├── config/              # Configuraciones de la aplicación
│   └── DatabaseConfig.java
├── controller/          # Controladores REST
│   └── TransactionController.java
├── domain/             # Entidades de dominio
│   └── Transaction.java
├── dto/                # Data Transfer Objects
│   ├── ErrorResponse.java
│   ├── TransactionRequest.java
│   └── TransactionResponse.java
├── exception/          # Manejo de excepciones
│   └── GlobalExceptionHandler.java
├── repository/         # Repositorios de datos
│   └── TransactionRepository.java
└── service/            # Lógica de negocio
    ├── CommissionService.java
    └── TransactionService.java
```

### Capas de la Aplicación

1. **Capa de Presentación (Controller)**: Maneja las peticiones HTTP
2. **Capa de Servicio (Service)**: Contiene la lógica de negocio
3. **Capa de Persistencia (Repository)**: Acceso a datos
4. **Capa de Dominio (Domain)**: Entidades del negocio

## API Endpoints

### Crear Transacción

**Endpoint**: `POST /api/transactions`

**Request Body**:
```json
{
  "monto": 15000.00
}
```

**Response** (201 Created):
```json
{
  "id": 1,
  "monto": 15000.00,
  "comision": 750.00,
  "fecha": "2025-01-05T10:30:00",
  "mensaje": "Comisión aplicada: 5%"
}
```

### Listar Todas las Transacciones

**Endpoint**: `GET /api/transactions`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "monto": 15000.00,
    "comision": 750.00,
    "fecha": "2025-01-05T10:30:00",
    "mensaje": "Comisión aplicada: 5%"
  }
]
```

### Obtener Transacción por ID

**Endpoint**: `GET /api/transactions/{id}`

**Response** (200 OK):
```json
{
  "id": 1,
  "monto": 15000.00,
  "comision": 750.00,
  "fecha": "2025-01-05T10:30:00",
  "mensaje": "Comisión aplicada: 5%"
}
```

### Errores

Todos los errores devuelven una estructura consistente:

**Response** (400 Bad Request / 500 Internal Server Error):
```json
{
  "error": "Validation Error",
  "mensaje": "El monto es obligatorio",
  "timestamp": "2025-01-05T10:30:00",
  "path": "/api/transactions"
}
```

## Reglas de Negocio

### Cálculo de Comisiones

El sistema aplica las siguientes reglas para el cálculo de comisiones:

| Monto de Transacción | Porcentaje de Comisión |
|---------------------|----------------------|
| Menor o igual a $10,000 | 2% |
| Mayor a $10,000 | 5% |

### Fórmula de Cálculo

```
Comisión = Monto × Tasa de Comisión
```

- Tasa de Comisión = 0.02 (2%) si monto ≤ 10,000
- Tasa de Comisión = 0.05 (5%) si monto > 10,000

### Validaciones

- El monto debe ser mayor a 0
- El monto es obligatorio
- Se permiten hasta 2 decimales
- Los valores se redondean usando HALF_UP

## Pruebas Unitarias

### Ejecutar Todas las Pruebas

```bash
mvn test
```

### Ejecutar una Clase de Prueba Específica

```bash
mvn test -Dtest=CommissionServiceTest
```

### Generar Reporte de Cobertura

```bash
mvn clean test jacoco:report
```

El reporte se genera en `target/site/jacoco/index.html`

### Cobertura de Pruebas

Las pruebas cubren:

- Cálculo de comisiones para diferentes montos
- Validaciones de entrada
- Manejo de errores
- Operaciones CRUD de transacciones
- Casos extremos y límites

## Base de Datos

### Esquema

```sql
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    monto DECIMAL(15, 2) NOT NULL,
    comision DECIMAL(15, 2) NOT NULL,
    fecha TIMESTAMP NOT NULL,
    CONSTRAINT chk_monto CHECK (monto > 0),
    CONSTRAINT chk_comision CHECK (comision >= 0)
);
```

### Acceso a la Consola H2

Durante el desarrollo, puede acceder a la consola H2 en:

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (vacío)
```

### Datos de Prueba

El sistema incluye datos de prueba que se cargan automáticamente al iniciar:

```sql
INSERT INTO transactions (monto, comision, fecha) VALUES 
    (5000.00, 100.00, CURRENT_TIMESTAMP),
    (15000.00, 750.00, CURRENT_TIMESTAMP),
    (8500.50, 170.01, CURRENT_TIMESTAMP);
```

## Principios de Diseño

### Principios SOLID

#### Single Responsibility Principle (SRP)
Cada clase tiene una única responsabilidad:
- `CommissionService`: Solo calcula comisiones
- `TransactionService`: Solo gestiona transacciones
- `TransactionController`: Solo maneja peticiones HTTP

#### Open/Closed Principle (OCP)
Las clases están abiertas a extensión pero cerradas a modificación:
- `CommissionService` permite agregar nuevas reglas sin modificar el código existente

#### Liskov Substitution Principle (LSP)
Las implementaciones pueden sustituirse por sus interfaces sin alterar la funcionalidad

#### Interface Segregation Principle (ISP)
Se utilizan interfaces específicas y cohesivas

#### Dependency Inversion Principle (DIP)
Las clases dependen de abstracciones, no de implementaciones concretas:
- Uso de interfaces de repositorios
- Inyección de dependencias mediante constructores

### Clean Code

El proyecto sigue las prácticas de Clean Code:

- Nombres descriptivos y significativos
- Funciones pequeñas con una sola responsabilidad
- Comentarios útiles que explican el "por qué", no el "qué"
- Código autodocumentado
- Manejo apropiado de errores
- Formato consistente

### Programación Reactiva

El proyecto implementa programación reactiva usando:

- `Mono<T>`: Para operaciones que devuelven un solo elemento
- `Flux<T>`: Para operaciones que devuelven múltiples elementos
- Operaciones no bloqueantes
- Backpressure handling

## Manejo de Errores

El sistema implementa un manejador global de excepciones que captura y procesa:

- Errores de validación (400 Bad Request)
- Argumentos inválidos (400 Bad Request)
- Recursos no encontrados (404 Not Found)
- Errores internos del servidor (500 Internal Server Error)

Todos los errores devuelven una respuesta estructurada con información relevante para el cliente.

## Logs

El sistema genera logs en diferentes niveles:

- **DEBUG**: Información detallada para desarrollo
- **INFO**: Información general de operaciones
- **WARN**: Advertencias que no afectan la operación
- **ERROR**: Errores que requieren atención

Los logs se pueden configurar en `application.yml`.

## Despliegue

### Preparación para Producción

1. Cambiar la base de datos a una solución permanente (PostgreSQL, MySQL)
2. Configurar variables de entorno apropiadas
3. Habilitar HTTPS
4. Configurar CORS según el dominio de producción
5. Ajustar niveles de log
6. Implementar monitoreo y métricas

### Ejemplo de Configuración para Producción

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/fintech
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  
  h2:
    console:
      enabled: false

server:
  port: ${SERVER_PORT:8080}
  ssl:
    enabled: true

logging:
  level:
    com.sofka: INFO
```

## Contribución

Para contribuir al proyecto:

1. Fork el repositorio
2. Cree una rama para su funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Commit sus cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Cree un Pull Request

### Estándares de Código

- Seguir las convenciones de Java
- Mantener cobertura de pruebas superior al 80%
- Documentar métodos públicos
- Usar nombres descriptivos en inglés
- Seguir principios SOLID y Clean Code
