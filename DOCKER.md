# 🐳 Guía Rápida de Docker - PersonApp

## 📋 Prerrequisitos

- Docker Desktop instalado y en ejecución
- Maven instalado (para compilar el proyecto)
- PowerShell (Windows)

## 🚀 Inicio Rápido

### Opción 1: Usando el Script de Ayuda (Recomendado)

```powershell
# 1. Compilar y construir imágenes Docker
.\docker-helper.ps1 build

# 2. Iniciar todos los servicios (MariaDB + MongoDB + App)
.\docker-helper.ps1 up

# 3. Ver logs
.\docker-helper.ps1 logs

# 4. Detener servicios
.\docker-helper.ps1 down
```

### Opción 2: Usando Docker Compose Directamente

```powershell
# 1. Compilar el proyecto
mvn clean package -DskipTests

# 2. Iniciar con ambas bases de datos
docker-compose up -d

# 3. Ver logs
docker-compose logs -f app-rest

# 4. Detener
docker-compose down
```

## 📦 Opciones de Despliegue

### 🔵 Todas las Bases de Datos (Recomendado para desarrollo)
```powershell
docker-compose up -d
```
- **Puerto App**: 3000
- **Puerto MariaDB**: 3308
- **Puerto MongoDB**: 27017
- Puedes cambiar entre bases de datos desde el frontend

### 🟢 Solo MariaDB
```powershell
docker-compose -f docker-compose-mariadb.yml up -d
```
- **Puerto App**: 3000
- **Puerto MariaDB**: 3308

### 🟡 Solo MongoDB
```powershell
docker-compose -f docker-compose-mongodb.yml up -d
```
- **Puerto App**: 3001
- **Puerto MongoDB**: 27017

## 🌐 URLs de Acceso

### Frontend Web
- **Con docker-compose.yml**: http://localhost:3000
- **Con docker-compose-mariadb.yml**: http://localhost:3000
- **Con docker-compose-mongodb.yml**: http://localhost:3001

### API REST
- **Swagger UI**: http://localhost:3000/swagger-ui.html
- **OpenAPI JSON**: http://localhost:3000/v3/api-docs

### Base de Datos
- **MariaDB**: localhost:3308
  - Usuario: `persona_db`
  - Contraseña: `persona_db`
  - Base de datos: `persona_db`

- **MongoDB**: localhost:27017
  - Usuario admin: `admin` / `admin123`
  - Usuario app: `persona_db` / `persona_db`
  - Base de datos: `persona_db`

## 🔧 Comandos Útiles

### Ver estado de contenedores
```powershell
docker-compose ps
```

### Ver logs en tiempo real
```powershell
# Solo app
docker-compose logs -f app-rest

# Todos los servicios
docker-compose logs -f
```

### Reiniciar solo la aplicación
```powershell
docker-compose restart app-rest
```

### Acceder a la base de datos MariaDB
```powershell
docker exec -it personapp-mariadb mysql -u persona_db -ppersona_db persona_db
```

### Acceder a MongoDB
```powershell
docker exec -it personapp-mongodb mongosh -u persona_db -p persona_db --authenticationDatabase persona_db persona_db
```

### Reconstruir imagen después de cambios
```powershell
# Recompilar proyecto
mvn clean package -DskipTests

# Reconstruir y reiniciar
docker-compose up -d --build app-rest
```

### Limpiar todo (contenedores, imágenes, volúmenes)
```powershell
docker-compose down -v
docker system prune -f
```

## 🐛 Troubleshooting

### La aplicación no inicia
1. Verifica que las bases de datos estén saludables:
   ```powershell
   docker-compose ps
   ```
2. Revisa los logs:
   ```powershell
   docker-compose logs app-rest
   ```

### Puerto ocupado
Si el puerto 3000 está ocupado, puedes cambiarlo en `docker-compose.yml`:
```yaml
ports:
  - "8080:3000"  # Cambia 8080 por el puerto que prefieras
```

### Error de conexión a base de datos
1. Espera a que las bases de datos estén completamente iniciadas (health check)
2. Reinicia la aplicación:
   ```powershell
   docker-compose restart app-rest
   ```

### Reconstruir desde cero
```powershell
# Detener y limpiar todo
docker-compose down -v
docker system prune -f

# Recompilar
mvn clean package -DskipTests

# Construir y iniciar
docker-compose build
docker-compose up -d
```

## 📊 Arquitectura Docker

```
┌─────────────────────────────────────────────┐
│           Docker Network                     │
│                                              │
│  ┌──────────────┐                           │
│  │   MariaDB    │                           │
│  │  Port: 3308  │                           │
│  └──────┬───────┘                           │
│         │                                    │
│         │        ┌──────────────┐           │
│         └────────┤   App REST   │           │
│                  │  Port: 3000  │           │
│         ┌────────┤  (Spring)    │           │
│         │        └──────────────┘           │
│  ┌──────┴───────┐                           │
│  │   MongoDB    │                           │
│  │  Port: 27017 │                           │
│  └──────────────┘                           │
│                                              │
└─────────────────────────────────────────────┘
```

## 🎯 Comandos del Script Helper

```powershell
.\docker-helper.ps1 build        # Compilar y construir imágenes
.\docker-helper.ps1 up           # Iniciar todos los servicios
.\docker-helper.ps1 up-maria     # Iniciar solo con MariaDB
.\docker-helper.ps1 up-mongo     # Iniciar solo con MongoDB
.\docker-helper.ps1 down         # Detener servicios
.\docker-helper.ps1 restart      # Reiniciar aplicación
.\docker-helper.ps1 logs         # Ver logs de la app
.\docker-helper.ps1 logs-all     # Ver logs de todo
.\docker-helper.ps1 ps           # Estado de contenedores
.\docker-helper.ps1 clean        # Limpiar todo
.\docker-helper.ps1 help         # Mostrar ayuda
```

## 💡 Tips

1. **Primera vez**: Usa `.\docker-helper.ps1 build` y luego `.\docker-helper.ps1 up`
2. **Desarrollo activo**: Usa `docker-compose.yml` para tener ambas bases de datos disponibles
3. **Producción**: Considera usar solo una base de datos específica
4. **Logs persistentes**: Se guardan en `./logs/` del host
5. **Datos persistentes**: Los datos de las bases de datos persisten en volúmenes Docker

## 🔒 Seguridad

⚠️ **IMPORTANTE**: Las credenciales en estos archivos son para DESARROLLO únicamente. 
En producción, usa:
- Variables de entorno seguras
- Secrets de Docker
- Gestores de secretos (Vault, AWS Secrets Manager, etc.)
