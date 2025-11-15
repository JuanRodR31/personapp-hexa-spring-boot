# PersonAPP - Hexagonal Architecture (Spring Boot)

Aplicación de ejemplo que implementa Arquitectura Hexagonal (Clean Architecture) con Spring Boot para la gestión de personas, profesiones, teléfonos y estudios.

## 🚀 Quick Start con Docker

```bash
# 1. Compilar el proyecto
mvn clean package -DskipTests

# 2. Iniciar todos los servicios (MariaDB + MongoDB + REST API)
docker-compose up -d

# 3. Abrir el frontend
# Windows: Start-Process "http://localhost:3000"
# Linux/Mac: open http://localhost:3000
```

✅ **Listo!** La aplicación está corriendo en http://localhost:3000 con datos de ejemplo en ambas bases de datos.

## ✨ Características Principales

- 🏗️ **Arquitectura Hexagonal** - Separación clara de capas y responsabilidades
- 🎨 **Frontend Web Integrado** - Interfaz gráfica moderna y responsive con tablas hermosas
- 🔌 **Múltiples Adaptadores** - REST API y CLI
- 💾 **Soporte Multi-Base de Datos** - MariaDB y MongoDB simultáneamente
- 📚 **Documentación API** - Swagger/OpenAPI integrado
- 🌐 **CORS Configurado** - Preparado para desarrollo cross-origin
- 🐳 **Docker Ready** - Configuración completa con Docker Compose y datos iniciales automáticos

## 📋 Tabla de Contenidos

- [Quick Start con Docker](#-quick-start-con-docker)
- [Características Principales](#-características-principales)
- [Requisitos Previos](#-requisitos-previos)
- [Arquitectura del Proyecto](#️-arquitectura-del-proyecto)
- [Configuración Inicial](#-configuración-inicial)
- [Ejecución con Docker](#-ejecución-con-docker)
- [Ejecución Local (sin Docker)](#-ejecución-local-sin-docker)
- [Acceso a las Aplicaciones](#-acceso-a-las-aplicaciones)
- [Frontend Web](#-frontend-web)
- [API REST](#-api-rest)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Troubleshooting](#-troubleshooting)

## 🔧 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

### Opción 1: Ejecución con Docker (Recomendado)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (versión 20.10 o superior)
- [Docker Compose](https://docs.docker.com/compose/install/) (incluido en Docker Desktop)

### Opción 2: Ejecución Local
- [Java JDK 17](https://adoptium.net/) o superior
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [MariaDB 10.6+](https://mariadb.org/download/) (si vas a usar MariaDB)
- [MongoDB 6.0+](https://www.mongodb.com/try/download/community) (si vas a usar MongoDB)

### Para Desarrollo
- IDE con soporte para Lombok (IntelliJ IDEA, Eclipse, VS Code)
  - **IntelliJ IDEA**: Instalar plugin "Lombok" desde Settings → Plugins
  - **Eclipse**: Descargar [lombok.jar](https://projectlombok.org/download) y ejecutar
  - **VS Code**: Instalar extensión "Lombok Annotations Support"

## 🏗️ Arquitectura del Proyecto

Este proyecto implementa una **Arquitectura Hexagonal** con los siguientes módulos:

- **domain**: Entidades del dominio y puertos
- **application**: Casos de uso (lógica de negocio)
- **common**: Utilidades compartidas
- **rest-input-adapter**: API REST (Puerto: 3000/3001)
- **cli-input-adapter**: Interfaz de línea de comandos
- **maria-output-adapter**: Adaptador para MariaDB
- **mongo-output-adapter**: Adaptador para MongoDB

La aplicación tiene **dos adaptadores de entrada** independientes:
1. **REST API**: Interfaz web con Swagger
2. **CLI**: Aplicación de consola interactiva

## 🚀 Configuración Inicial

### 1. Clonar el Repositorio

```bash
git clone https://github.com/JuanRodR31/personapp-hexa-spring-boot.git
cd personapp-hexa-spring-boot
```

### 2. Configurar Lombok en tu IDE

#### IntelliJ IDEA:
1. Ve a `File → Settings → Plugins`
2. Busca "Lombok" e instálalo
3. Ve a `File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors`
4. Marca "Enable annotation processing"

#### Eclipse:
1. Descarga [lombok.jar](https://projectlombok.org/download)
2. Ejecuta: `java -jar lombok.jar`
3. Selecciona tu instalación de Eclipse y haz clic en "Install/Update"

#### VS Code:
1. Instala la extensión "Language Support for Java(TM) by Red Hat"
2. Instala la extensión "Lombok Annotations Support for VS Code"

## 🐳 Ejecución con Docker

### 🚀 Quick Start (Recomendado)

La forma más rápida de ejecutar la aplicación con **ambas bases de datos** (MariaDB y MongoDB):

#### 1. Compilar el proyecto
```bash
mvn clean package -DskipTests
```

#### 2. Iniciar todos los servicios
```bash
docker-compose up -d
```

Este comando levantará automáticamente:
- ✅ **MariaDB** en puerto 3308 con datos iniciales
- ✅ **MongoDB** en puerto 27017 con datos iniciales
- ✅ **REST API** en puerto 3000 conectada a ambas bases de datos

#### 3. Verificar que todo esté corriendo
```bash
docker-compose ps
```

Deberías ver todos los servicios con estado `healthy`:
```
NAME                STATUS
personapp-mariadb   Up (healthy)
personapp-mongodb   Up (healthy)
personapp-rest      Up
```

#### 4. Acceder a la aplicación
- **🎨 Frontend Web**: http://localhost:3000
- **📚 Swagger UI**: http://localhost:3000/swagger-ui/index.html
- **🔌 API MariaDB**: http://localhost:3000/api/v1/persona/maria
- **🔌 API MongoDB**: http://localhost:3000/api/v1/persona/mongo

#### 5. Verificar datos
```powershell
# Contar personas en MariaDB
curl http://localhost:3000/api/v1/persona/maria/count

# Contar personas en MongoDB
curl http://localhost:3000/api/v1/persona/mongo/count
```

Ambos deberían retornar `8`.

### 🎯 Datos Iniciales Incluidos

La configuración de Docker incluye automáticamente datos de ejemplo:
- 👥 **8 personas** (Pepe, Pepito, Pepa, Pepita, Fede, Ana, Carlos, María)
- 💼 **6 profesiones** (Ing. Sistemas, Medicina, Derecho, Administración, Psicología, Arquitectura)
- 📱 **9 teléfonos** distribuidos entre las personas
- 🎓 **8 estudios** (relaciones persona-profesión)

### 🔧 Comandos Útiles

#### Ver logs
```bash
# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f app-rest
docker-compose logs -f mariadb
docker-compose logs -f mongodb
```

#### Detener servicios
```bash
# Detener todos los servicios
docker-compose down

# Detener y eliminar volúmenes (borra los datos)
docker-compose down -v
```

#### Reiniciar servicios
```bash
# Reiniciar todos los servicios
docker-compose restart

# Reiniciar solo la API REST
docker-compose restart app-rest
```

#### Reconstruir después de cambios en el código
```bash
# 1. Recompilar el proyecto
mvn clean package -DskipTests

# 2. Reconstruir la imagen Docker
docker-compose build --no-cache app-rest

# 3. Reiniciar solo el servicio REST
docker-compose up -d app-rest
```

### 📝 Script de Ayuda (PowerShell)

El proyecto incluye un script de ayuda `docker-helper.ps1` con comandos útiles:

```powershell
# Ver todos los comandos disponibles
.\docker-helper.ps1 help

# Construir imágenes
.\docker-helper.ps1 build

# Iniciar todos los servicios
.\docker-helper.ps1 up

# Ver estado de los servicios
.\docker-helper.ps1 ps

# Ver logs en tiempo real
.\docker-helper.ps1 logs

# Detener servicios
.\docker-helper.ps1 down

# Reiniciar servicios
.\docker-helper.ps1 restart

# Limpiar todo (incluyendo volúmenes)
.\docker-helper.ps1 clean
```

### 🔍 Verificación de la Instalación

Ejecuta estos comandos para verificar que todo funciona correctamente:

```powershell
# 1. Estado de contenedores
docker-compose ps

# 2. Datos en MariaDB
docker exec personapp-mariadb mariadb -u persona_db -ppersona_db persona_db -e "SELECT COUNT(*) as total FROM persona"

# 3. Datos en MongoDB
docker exec personapp-mongodb mongosh -u persona_db -p persona_db --authenticationDatabase admin --eval "db.getSiblingDB('persona_db').persona.countDocuments()"

# 4. API funcionando
curl http://localhost:3000/api/v1/persona/maria/count
curl http://localhost:3000/api/v1/persona/mongo/count
```

### 📖 Documentación Adicional

Para más detalles sobre la configuración de Docker, consulta:
- **[DOCKER.md](DOCKER.md)** - Guía completa de Docker con troubleshooting
- **[DOCKER_SETUP_COMPLETE.md](DOCKER_SETUP_COMPLETE.md)** - Resumen de configuración y verificación

### 🎓 Configuraciones Avanzadas

#### Ejecutar solo con MariaDB
```bash
docker-compose -f docker-compose-mariadb.yml up -d
```

#### Ejecutar solo con MongoDB
```bash
docker-compose -f docker-compose-mongodb.yml up -d
```

#### Cambiar puertos
Edita el archivo `docker-compose.yml` y modifica la sección `ports`:
```yaml
services:
  app-rest:
    ports:
      - "8080:3000"  # Cambia el primer número (puerto del host)
```

#### Ver base de datos directamente

**MariaDB:**
```bash
docker exec -it personapp-mariadb mariadb -u persona_db -ppersona_db persona_db
```

**MongoDB:**
```bash
docker exec -it personapp-mongodb mongosh -u persona_db -p persona_db --authenticationDatabase admin persona_db
```

## 💻 Ejecución Local (sin Docker)

### Opción A: Con MariaDB

#### 1. Instalar y configurar MariaDB
```bash
# Iniciar el servicio de MariaDB
# Windows: Iniciar desde Servicios o XAMPP
# Linux: sudo systemctl start mariadb
# macOS: brew services start mariadb
```

#### 2. Crear la base de datos y usuario
```bash
mysql -u root -p < scripts/persona_ddl_maria.sql
mysql -u root -p persona_db < scripts/persona_dml_maria.sql
```

#### 3. Compilar el proyecto
```bash
mvn clean install
```

#### 4. Ejecutar la aplicación REST
```bash
cd rest-input-adapter
mvn spring-boot:run -Dspring-boot.run.profiles=mariadb
```

#### 5. Ejecutar la aplicación CLI (en otra terminal)
```bash
cd cli-input-adapter
mvn spring-boot:run -Dspring-boot.run.profiles=mariadb
```

### Opción B: Con MongoDB

#### 1. Instalar y configurar MongoDB
```bash
# Iniciar el servicio de MongoDB
# Windows: Iniciar desde Servicios
# Linux: sudo systemctl start mongod
# macOS: brew services start mongodb-community
```

#### 2. Crear la base de datos y usuario
```bash
mongosh admin < scripts/init-mongo.js
mongosh -u admin -p admin123 --authenticationDatabase admin persona_db < scripts/persona_ddl_mongo.js
mongosh -u admin -p admin123 --authenticationDatabase admin persona_db < scripts/persona_dml_mongo.js
```

#### 3. Compilar el proyecto
```bash
mvn clean install
```

#### 4. Ejecutar la aplicación REST
```bash
cd rest-input-adapter
mvn spring-boot:run -Dspring-boot.run.profiles=mongodb
```

#### 5. Ejecutar la aplicación CLI (en otra terminal)
```bash
cd cli-input-adapter
mvn spring-boot:run -Dspring-boot.run.profiles=mongodb
```

## 🌐 Acceso a las Aplicaciones

### 🎨 Frontend Web (Interfaz Gráfica)

La aplicación incluye un frontend web moderno y responsive integrado con Spring Boot.

**URL de Acceso:**
- **Docker (Recomendado)**: http://localhost:3000
- **Local (sin Docker)**: http://localhost:3000

#### Características del Frontend:
- ✅ Interfaz moderna y responsive con tablas hermosas
- ✅ Gestión completa de CRUD para todas las entidades
- ✅ Selector de base de datos (MariaDB/MongoDB)
- ✅ Visualización en tablas estilizadas en lugar de JSON
- ✅ Indicadores de carga durante las peticiones
- ✅ Sin necesidad de configuración adicional
- ✅ Incluido automáticamente en el JAR de Spring Boot

#### Módulos Disponibles:
- 👤 **Personas** - Crear, listar, buscar, actualizar y eliminar personas
- 📱 **Teléfonos** - Gestión completa de teléfonos asociados a personas
- 💼 **Profesiones** - Administración de profesiones
- 🎓 **Estudios** - Gestión de estudios (relación entre personas y profesiones)

### 🔌 REST API

**Con Docker (ambas bases de datos disponibles):**
- URL Base: http://localhost:3000
- Swagger UI: http://localhost:3000/swagger-ui/index.html
- API Docs: http://localhost:3000/v3/api-docs
- MariaDB endpoints: `/api/v1/{entity}/maria`
- MongoDB endpoints: `/api/v1/{entity}/mongo`

**Ejemplos de endpoints:**
```bash
# Listar personas de MariaDB
curl http://localhost:3000/api/v1/persona/maria

# Listar personas de MongoDB
curl http://localhost:3000/api/v1/persona/mongo

# Contar personas en MariaDB
curl http://localhost:3000/api/v1/persona/maria/count

# Contar personas en MongoDB
curl http://localhost:3000/api/v1/persona/mongo/count
```

### CLI (Interfaz de Línea de Comandos)

**Nota**: La aplicación CLI está disponible solo en las configuraciones individuales de Docker:

```bash
# Para MariaDB
docker-compose -f docker-compose-mariadb.yml up -d
docker attach personapp-cli-mariadb

# Para MongoDB
docker-compose -f docker-compose-mongodb.yml up -d
docker attach personapp-cli-mongodb
```

Para salir del CLI sin detener el contenedor: `Ctrl+P` seguido de `Ctrl+Q`

La aplicación CLI proporciona un menú interactivo para gestionar:
- Personas
- Profesiones
- Teléfonos
- Estudios

## 🎨 Frontend Web

### Características del Frontend Integrado

El proyecto incluye un frontend web completo desarrollado en HTML, CSS y JavaScript vanilla, integrado directamente en Spring Boot.

#### Tecnologías Utilizadas:
- **HTML5** - Estructura semántica
- **CSS3** - Diseño moderno con gradientes y animaciones
- **JavaScript (ES6+)** - Funcionalidad dinámica con Fetch API
- **Spring Boot Static Resources** - Servido automáticamente desde el classpath

#### Ubicación del Frontend:
```
rest-input-adapter/src/main/resources/static/index.html
```

#### Funcionamiento:
1. El frontend se sirve automáticamente desde Spring Boot
2. Utiliza rutas relativas para las peticiones API (mismo servidor)
3. No requiere configuración CORS adicional (mismo origen)
4. Se empaqueta dentro del JAR ejecutable

#### Modificar el Frontend:
Si deseas personalizar el frontend:
1. Edita el archivo `rest-input-adapter/src/main/resources/static/index.html`
2. Recompila el proyecto: `mvn clean package -DskipTests`
3. Reinicia la aplicación

## � API REST

### Endpoints Principales

#### Personas (`/api/v1/persona`)
- `GET /{database}` - Listar todas las personas
- `GET /{database}/{dni}` - Obtener una persona por DNI
- `GET /{database}/count` - Contar personas
- `POST /` - Crear una persona
- `PUT /{database}/{dni}` - Actualizar una persona
- `DELETE /{database}/{dni}` - Eliminar una persona

#### Teléfonos (`/api/v1/telefono`)
- `GET /{database}` - Listar todos los teléfonos
- `GET /{database}/{number}` - Obtener un teléfono por número
- `GET /{database}/count` - Contar teléfonos
- `POST /` - Crear un teléfono
- `PUT /{database}/{number}` - Actualizar un teléfono
- `DELETE /{database}/{number}` - Eliminar un teléfono

#### Profesiones (`/api/v1/profesion`)
- `GET /{database}` - Listar todas las profesiones
- `GET /{database}/{id}` - Obtener una profesión por ID
- `GET /{database}/count` - Contar profesiones
- `POST /` - Crear una profesión
- `PUT /{database}/{id}` - Actualizar una profesión
- `DELETE /{database}/{id}` - Eliminar una profesión

#### Estudios (`/api/v1/estudios`)
- `GET /{database}` - Listar todos los estudios
- `GET /{database}/{professionId}/{personDni}` - Obtener un estudio específico
- `GET /{database}/count` - Contar estudios
- `POST /` - Crear un estudio
- `PUT /{database}/{professionId}/{personDni}` - Actualizar un estudio
- `DELETE /{database}/{professionId}/{personDni}` - Eliminar un estudio

**Nota**: `{database}` puede ser `maria` (MariaDB) o `mongo` (MongoDB)

### Configuración CORS

El proyecto incluye configuración CORS pre-configurada para desarrollo:

**Ubicación**: `rest-input-adapter/src/main/java/co/edu/javeriana/as/personapp/config/CorsConfig.java`

#### Características CORS:
- ✅ Permite todos los orígenes en desarrollo
- ✅ Soporta todos los métodos HTTP (GET, POST, PUT, DELETE, OPTIONS)
- ✅ Permite todos los headers
- ✅ Soporta credenciales

**Nota de Producción**: Modifica la configuración CORS antes de desplegar a producción para restringir los orígenes permitidos.

## �📁 Estructura del Proyecto

```
personapp-hexa-spring-boot/
├── application/              # Casos de uso (lógica de negocio)
├── cli-input-adapter/        # Adaptador CLI
├── common/                   # Utilidades compartidas
├── domain/                   # Entidades y puertos del dominio
├── maria-output-adapter/     # Adaptador para MariaDB
├── mongo-output-adapter/     # Adaptador para MongoDB
├── rest-input-adapter/       # Adaptador REST API + Frontend
│   └── src/main/
│       ├── java/
│       │   └── co/edu/javeriana/as/personapp/
│       │       ├── config/
│       │       │   └── CorsConfig.java       # Configuración CORS
│       │       ├── controller/               # Controladores REST
│       │       ├── mapper/                   # Mapeadores DTO
│       │       └── model/                    # DTOs
│       └── resources/
│           ├── static/
│           │   └── index.html                # Frontend integrado
│           └── application.properties
├── frontend/                 # Backup del frontend (no se usa en producción)
│   └── index.html           # Versión standalone del frontend
├── scripts/                  # Scripts de base de datos
│   ├── persona_ddl_maria.sql    # Esquema MariaDB
│   ├── persona_dml_maria.sql    # Datos MariaDB
│   ├── persona_ddl_mongo.js     # Esquema MongoDB
│   ├── persona_dml_mongo.js     # Datos MongoDB
│   └── init-mongo.js            # Inicialización MongoDB
├── logs/                     # Directorio de logs (generado en runtime)
├── docker-compose-mariadb.yml   # Compose para MariaDB
├── docker-compose-mongodb.yml   # Compose para MongoDB
├── Dockerfile                   # Dockerfile multi-stage
└── pom.xml                      # POM principal
```

## 🔍 Troubleshooting

### Error: Puerto ya en uso

**Problema**: `Port 3000/3308/27017 is already in use`

**Solución**:
```powershell
# Windows (PowerShell)
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Linux/macOS
lsof -i :3000
kill -9 <PID>
```

O cambia el puerto en el archivo `docker-compose.yml`:
```yaml
services:
  app-rest:
    ports:
      - "8080:3000"  # Usa el puerto 8080 en lugar de 3000
```

### Error: Contenedor falla al iniciar (dependency failed to start)

**Problema**: El contenedor `app-rest` no inicia porque las bases de datos no están listas

**Solución**:
```powershell
# 1. Ver estado de los contenedores
docker-compose ps

# 2. Ver logs de las bases de datos
docker logs personapp-mariadb
docker logs personapp-mongodb

# 3. Si hay problemas, reiniciar con datos limpios
docker-compose down -v
docker-compose up -d

# 4. Esperar a que estén healthy (~15 segundos)
docker-compose ps
```

### Error: MongoDB Authentication failed

**Problema**: `Command failed with error 18 (AuthenticationFailed)`

**Solución**:
Este error ocurre cuando la configuración de autenticación está incorrecta. La aplicación ya está configurada correctamente, pero si persiste:

```powershell
# 1. Detener y limpiar volúmenes
docker-compose down -v

# 2. Reiniciar (esto recreará el usuario correctamente)
docker-compose up -d

# 3. Verificar que MongoDB esté healthy
docker-compose ps

# 4. Probar la conexión
curl http://localhost:3000/api/v1/persona/mongo/count
```

### Error: La base de datos está vacía

**Problema**: Los endpoints retornan listas vacías o `count` retorna `0`

**Solución**:
```powershell
# 1. Verificar datos en MariaDB
docker exec personapp-mariadb mariadb -u persona_db -ppersona_db persona_db -e "SELECT COUNT(*) FROM persona"

# 2. Verificar datos en MongoDB
docker exec personapp-mongodb mongosh -u persona_db -p persona_db --authenticationDatabase admin --eval "db.getSiblingDB('persona_db').persona.countDocuments()"

# 3. Si no hay datos, reiniciar con volúmenes limpios
docker-compose down -v
docker-compose up -d
```

### Error: Lombok no funciona

**Problema**: Errores de compilación con getters/setters

**Solución**:
1. Verifica que el plugin de Lombok esté instalado en tu IDE
2. Habilita "Annotation Processing" en la configuración del IDE
3. Limpia y reconstruye el proyecto:
   ```bash
   mvn clean install
   ```

### Error: Cambios en el código no se reflejan

**Problema**: Modificaste el código pero los cambios no aparecen en Docker

**Solución**:
```powershell
# 1. Recompilar el proyecto
mvn clean package -DskipTests

# 2. Reconstruir la imagen sin cache
docker-compose build --no-cache app-rest

# 3. Reiniciar el servicio
docker-compose up -d app-rest

# 4. Ver logs para confirmar
docker-compose logs -f app-rest
```

### Error: Docker build falla

**Problema**: Error durante `docker-compose up` o `docker-compose build`

**Solución**:
1. Asegúrate de haber compilado el proyecto primero:
   ```bash
   mvn clean package -DskipTests
   ```
2. Limpia las imágenes de Docker:
   ```bash
   docker-compose down --rmi all
   docker system prune -a
   ```
3. Reconstruye desde cero:
   ```bash
   docker-compose build --no-cache
   docker-compose up -d
   ```

### Ver logs de la aplicación

```powershell
# Logs en tiempo real de todos los servicios
docker-compose logs -f

# Logs de un servicio específico
docker-compose logs -f app-rest
docker-compose logs -f mariadb
docker-compose logs -f mongodb

# Últimas 50 líneas de logs
docker-compose logs --tail=50 app-rest

# Logs guardados en el host (si la aplicación está escribiendo archivos)
Get-Content logs/persona.log -Tail 50 -Wait
```

### Reiniciar completamente (solución nuclear)

Si nada funciona, intenta esto:

```powershell
# 1. Detener todo y limpiar
docker-compose down -v
docker system prune -a -f

# 2. Recompilar el proyecto
mvn clean package -DskipTests

# 3. Reconstruir e iniciar
docker-compose build --no-cache
docker-compose up -d

# 4. Esperar y verificar
Start-Sleep -Seconds 15
docker-compose ps

# 5. Probar endpoints
curl http://localhost:3000/api/v1/persona/maria/count
curl http://localhost:3000/api/v1/persona/mongo/count
```

### Obtener ayuda adicional

Para más información detallada sobre problemas de Docker:
- Consulta **[DOCKER.md](DOCKER.md)** para troubleshooting completo
- Revisa los logs: `docker-compose logs -f`
- Verifica el estado: `docker-compose ps`
- Abre un issue en el repositorio de GitHub

## 📝 Notas Adicionales

- **Bases de datos**: Con Docker, ambas bases de datos corren simultáneamente (MariaDB en puerto 3308, MongoDB en puerto 27017)
- **REST API**: Un solo servicio REST en puerto 3000 que se conecta a ambas bases de datos
- **Datos iniciales**: Incluye automáticamente 8 personas, 6 profesiones, 9 teléfonos y 8 estudios en ambas bases de datos
- **Health checks**: Docker espera a que las bases de datos estén completamente inicializadas antes de iniciar la API
- **Volúmenes persistentes**: Los datos persisten entre reinicios de Docker (usa `docker-compose down -v` para limpiar)
- **Logs**: Los logs se guardan en el directorio `logs/` del proyecto
- **Frontend**: Se sirve automáticamente desde Spring Boot, no requiere servidor web separado
- **Profiles**: La aplicación usa Spring Profiles para cambiar entre bases de datos (`mariadb` o `mongodb`)
- **Fork**: Puedes hacer fork de este repositorio para tus propios experimentos

## 🎯 Arquitectura de Despliegue con Docker

```
┌─────────────────────────────────────────────────────────────┐
│                    localhost:3000                            │
│                   (Frontend + REST API)                      │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │         Spring Boot Application (Java 21)              │ │
│  │  ┌──────────────┐  ┌──────────────┐                   │ │
│  │  │   MariaDB    │  │   MongoDB    │                   │ │
│  │  │   Adapter    │  │   Adapter    │                   │ │
│  │  └───────┬──────┘  └──────┬───────┘                   │ │
│  └──────────┼─────────────────┼──────────────────────────┘ │
└─────────────┼─────────────────┼────────────────────────────┘
              │                 │
              ▼                 ▼
    ┌─────────────────┐ ┌─────────────────┐
    │    MariaDB      │ │    MongoDB      │
    │ Port: 3308      │ │ Port: 27017     │
    │ 8 personas      │ │ 8 personas      │
    │ 6 profesiones   │ │ 6 profesiones   │
    │ 9 teléfonos     │ │ 9 teléfonos     │
    │ 8 estudios      │ │ 8 estudios      │
    └─────────────────┘ └─────────────────┘
```

## 🔗 Enlaces Útiles

- 📖 **[DOCKER.md](DOCKER.md)** - Documentación completa de Docker
- ✅ **[DOCKER_SETUP_COMPLETE.md](DOCKER_SETUP_COMPLETE.md)** - Resumen de configuración completada
- 🐳 **[docker-compose.yml](docker-compose.yml)** - Configuración de servicios Docker
- 🛠️ **[docker-helper.ps1](docker-helper.ps1)** - Script de ayuda para comandos Docker
- 📜 **[scripts/](scripts/)** - Scripts de inicialización de bases de datos

## 📊 Estado del Proyecto

- ✅ Backend completamente funcional con ambas bases de datos
- ✅ Frontend web integrado con tablas hermosas
- ✅ API REST documentada con Swagger
- ✅ Dockerización completa con inicialización automática
- ✅ Datos de ejemplo incluidos
- ✅ Health checks implementados
- ✅ CORS configurado
- ✅ Logs persistentes

**¿Necesitas ayuda?** Abre un issue en el repositorio de GitHub.
