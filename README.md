# PersonAPP - Hexagonal Architecture (Spring Boot)

Aplicación de ejemplo que implementa Arquitectura Hexagonal (Clean Architecture) con Spring Boot para la gestión de personas, profesiones, teléfonos y estudios.

## 📋 Tabla de Contenidos

- [Requisitos Previos](#requisitos-previos)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [Configuración Inicial](#configuración-inicial)
- [Ejecución con Docker](#ejecución-con-docker)
- [Ejecución Local (sin Docker)](#ejecución-local-sin-docker)
- [Acceso a las Aplicaciones](#acceso-a-las-aplicaciones)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Troubleshooting](#troubleshooting)

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

### Opción A: Ejecutar con MariaDB

#### 1. Compilar el proyecto
```bash
mvn clean package -DskipTests
```

#### 2. Levantar los contenedores
```bash
docker-compose -f docker-compose-mariadb.yml up -d
```

#### 3. Ejecutar los scripts de base de datos

**Crear las tablas (DDL):**
```bash
docker exec -i personapp-mariadb mariadb -uroot -proot persona_db < scripts/persona_ddl_maria.sql
```

**Insertar datos iniciales (DML):**
```bash
docker exec -i personapp-mariadb mariadb -uroot -proot persona_db < scripts/persona_dml_maria.sql
```

#### 4. Acceder a las aplicaciones
- **REST API**: http://localhost:3000
- **Swagger UI**: http://localhost:3000/swagger-ui.html
- **CLI**: 
  ```bash
  docker attach personapp-cli-mariadb
  ```
  Para salir del CLI sin detener el contenedor: `Ctrl+P` seguido de `Ctrl+Q`

### Opción B: Ejecutar con MongoDB

#### 1. Compilar el proyecto
```bash
mvn clean package -DskipTests
```

#### 2. Levantar los contenedores
```bash
docker-compose -f docker-compose-mongodb.yml up -d
```

#### 3. Ejecutar los scripts de base de datos

**Crear las colecciones (DDL):**
```bash
docker exec -i personapp-mongodb mongosh -u admin -p admin123 --authenticationDatabase admin persona_db < scripts/persona_ddl_mongo.js
```

**Insertar datos iniciales (DML):**
```bash
docker exec -i personapp-mongodb mongosh -u admin -p admin123 --authenticationDatabase admin persona_db < scripts/persona_dml_mongo.js
```

#### 4. Acceder a las aplicaciones
- **REST API**: http://localhost:3001
- **Swagger UI**: http://localhost:3001/swagger-ui.html
- **CLI**: 
  ```bash
  docker attach personapp-cli-mongodb
  ```
  Para salir del CLI sin detener el contenedor: `Ctrl+P` seguido de `Ctrl+Q`

### Detener los contenedores

**Para MariaDB:**
```bash
docker-compose -f docker-compose-mariadb.yml down
```

**Para MongoDB:**
```bash
docker-compose -f docker-compose-mongodb.yml down
```

**Para eliminar también los volúmenes (datos):**
```bash
docker-compose -f docker-compose-mariadb.yml down -v
# o
docker-compose -f docker-compose-mongodb.yml down -v
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

### REST API

**Con MariaDB:**
- URL Base: http://localhost:3000
- Swagger UI: http://localhost:3000/swagger-ui.html
- API Docs: http://localhost:3000/v3/api-docs

**Con MongoDB:**
- URL Base: http://localhost:3001
- Swagger UI: http://localhost:3001/swagger-ui.html
- API Docs: http://localhost:3001/v3/api-docs

### Endpoints principales:
- `GET /api/v1/persona` - Listar todas las personas
- `POST /api/v1/persona` - Crear una persona
- `GET /api/v1/persona/{cc}` - Obtener una persona por CC
- `PUT /api/v1/persona/{cc}` - Actualizar una persona
- `DELETE /api/v1/persona/{cc}` - Eliminar una persona
- `GET /api/v1/profesion` - Listar todas las profesiones
- `GET /api/v1/telefono` - Listar todos los teléfonos
- `GET /api/v1/estudios` - Listar todos los estudios

### CLI (Interfaz de Línea de Comandos)

La aplicación CLI proporciona un menú interactivo para gestionar:
- Personas
- Profesiones
- Teléfonos
- Estudios

## 📁 Estructura del Proyecto

```
personapp-hexa-spring-boot/
├── application/              # Casos de uso (lógica de negocio)
├── cli-input-adapter/        # Adaptador CLI
├── common/                   # Utilidades compartidas
├── domain/                   # Entidades y puertos del dominio
├── maria-output-adapter/     # Adaptador para MariaDB
├── mongo-output-adapter/     # Adaptador para MongoDB
├── rest-input-adapter/       # Adaptador REST API
├── scripts/                  # Scripts de base de datos
│   ├── persona_ddl_maria.sql    # Esquema MariaDB
│   ├── persona_dml_maria.sql    # Datos MariaDB
│   ├── persona_ddl_mongo.js     # Esquema MongoDB
│   ├── persona_dml_mongo.js     # Datos MongoDB
│   └── init-mongo.js            # Inicialización MongoDB
├── docker-compose-mariadb.yml   # Compose para MariaDB
├── docker-compose-mongodb.yml   # Compose para MongoDB
├── Dockerfile                   # Dockerfile multi-stage
└── pom.xml                      # POM principal
```

## 🔍 Troubleshooting

### Error: Puerto ya en uso

**Problema**: `Port 3000/3001/3308/27017 is already in use`

**Solución**:
```bash
# Windows (PowerShell)
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Linux/macOS
lsof -i :3000
kill -9 <PID>
```

O cambia el puerto en el archivo `docker-compose-*.yml`

### Error: Lombok no funciona

**Problema**: Errores de compilación con getters/setters

**Solución**:
1. Verifica que el plugin de Lombok esté instalado en tu IDE
2. Habilita "Annotation Processing" en la configuración del IDE
3. Limpia y reconstruye el proyecto:
   ```bash
   mvn clean install
   ```

### Error: No se puede conectar a la base de datos

**Problema**: `Connection refused` o `Unknown database`

**Solución**:
1. Verifica que el contenedor de la base de datos esté corriendo:
   ```bash
   docker ps
   ```
2. Verifica los logs del contenedor:
   ```bash
   docker logs personapp-mariadb
   # o
   docker logs personapp-mongodb
   ```
3. Ejecuta los scripts de inicialización nuevamente

### Error: Docker build falla

**Problema**: Error durante `docker-compose up`

**Solución**:
1. Asegúrate de haber compilado el proyecto primero:
   ```bash
   mvn clean package -DskipTests
   ```
2. Limpia las imágenes de Docker:
   ```bash
   docker-compose -f docker-compose-mariadb.yml down --rmi all
   docker system prune -a
   ```

### Ver logs de la aplicación

```bash
# Logs en tiempo real
docker logs -f personapp-rest-mariadb
docker logs -f personapp-cli-mariadb

# Logs guardados en el host
cat logs/application.log
```

## 📝 Notas Adicionales

- **Bases de datos**: Puedes ejecutar ambas bases de datos simultáneamente (MariaDB usa puerto 3308, MongoDB usa puerto 27017)
- **REST API**: MariaDB usa puerto 3000, MongoDB usa puerto 3001
- **Profiles**: La aplicación usa Spring Profiles para cambiar entre bases de datos (`mariadb` o `mongodb`)
- **Logs**: Los logs se guardan en el directorio `logs/` del proyecto
- **Fork**: Puedes hacer fork de este repositorio para tus propios experimentos


**¿Necesitas ayuda?** Abre un issue en el repositorio de GitHub.
