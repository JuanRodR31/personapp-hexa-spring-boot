# Script de ayuda para Docker - PersonApp
# Uso: .\docker-helper.ps1 [comando]

param(
    [Parameter(Position=0)]
    [string]$comando = "help"
)

function Show-Help {
    Write-Host "`n=== PersonApp Docker Helper ===" -ForegroundColor Cyan
    Write-Host "`nComandos disponibles:" -ForegroundColor Yellow
    Write-Host "  build          - Compila el proyecto Maven y construye las imágenes Docker"
    Write-Host "  up             - Inicia todos los servicios (MariaDB + MongoDB + App REST)"
    Write-Host "  up-maria       - Inicia solo con MariaDB"
    Write-Host "  up-mongo       - Inicia solo con MongoDB"
    Write-Host "  down           - Detiene y elimina todos los contenedores"
    Write-Host "  restart        - Reinicia la aplicación"
    Write-Host "  logs           - Muestra los logs de la aplicación"
    Write-Host "  logs-all       - Muestra los logs de todos los servicios"
    Write-Host "  ps             - Lista los contenedores en ejecución"
    Write-Host "  clean          - Limpia contenedores, imágenes y volúmenes"
    Write-Host "  help           - Muestra esta ayuda"
    Write-Host "`nEjemplo:" -ForegroundColor Green
    Write-Host "  .\docker-helper.ps1 build" -ForegroundColor Gray
    Write-Host "  .\docker-helper.ps1 up" -ForegroundColor Gray
    Write-Host ""
}

function Build-Project {
    Write-Host "`n🔨 Compilando proyecto Maven..." -ForegroundColor Cyan
    mvn clean package -DskipTests
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Compilación exitosa" -ForegroundColor Green
        Write-Host "`n🐳 Construyendo imágenes Docker..." -ForegroundColor Cyan
        docker-compose build
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Imágenes Docker creadas exitosamente" -ForegroundColor Green
        }
    } else {
        Write-Host "❌ Error en la compilación" -ForegroundColor Red
        exit 1
    }
}

function Start-Services {
    Write-Host "`n🚀 Iniciando servicios..." -ForegroundColor Cyan
    docker-compose up -d
    Write-Host "`n✅ Servicios iniciados" -ForegroundColor Green
    Write-Host "`n📊 Estado de los contenedores:" -ForegroundColor Yellow
    docker-compose ps
    Write-Host "`n🌐 Frontend disponible en: http://localhost:3000" -ForegroundColor Cyan
    Write-Host "📖 API docs en: http://localhost:3000/swagger-ui.html" -ForegroundColor Cyan
}

function Start-MariaDB {
    Write-Host "`n🚀 Iniciando servicios con MariaDB..." -ForegroundColor Cyan
    docker-compose -f docker-compose-mariadb.yml up -d
    Write-Host "`n✅ Servicios iniciados" -ForegroundColor Green
    Write-Host "`n📊 Estado de los contenedores:" -ForegroundColor Yellow
    docker-compose -f docker-compose-mariadb.yml ps
    Write-Host "`n🌐 Frontend disponible en: http://localhost:3000" -ForegroundColor Cyan
}

function Start-MongoDB {
    Write-Host "`n🚀 Iniciando servicios con MongoDB..." -ForegroundColor Cyan
    docker-compose -f docker-compose-mongodb.yml up -d
    Write-Host "`n✅ Servicios iniciados" -ForegroundColor Green
    Write-Host "`n📊 Estado de los contenedores:" -ForegroundColor Yellow
    docker-compose -f docker-compose-mongodb.yml ps
    Write-Host "`n🌐 Frontend disponible en: http://localhost:3001" -ForegroundColor Cyan
}

function Stop-Services {
    Write-Host "`n🛑 Deteniendo servicios..." -ForegroundColor Cyan
    docker-compose down
    Write-Host "✅ Servicios detenidos" -ForegroundColor Green
}

function Restart-App {
    Write-Host "`n🔄 Reiniciando aplicación..." -ForegroundColor Cyan
    docker-compose restart app-rest
    Write-Host "✅ Aplicación reiniciada" -ForegroundColor Green
}

function Show-Logs {
    Write-Host "`n📋 Logs de la aplicación:" -ForegroundColor Cyan
    docker-compose logs -f app-rest
}

function Show-AllLogs {
    Write-Host "`n📋 Logs de todos los servicios:" -ForegroundColor Cyan
    docker-compose logs -f
}

function Show-Status {
    Write-Host "`n📊 Estado de los contenedores:" -ForegroundColor Cyan
    docker-compose ps
}

function Clean-Docker {
    Write-Host "`n🧹 Limpiando contenedores y volúmenes..." -ForegroundColor Cyan
    docker-compose down -v
    docker system prune -f
    Write-Host "✅ Limpieza completada" -ForegroundColor Green
}

# Ejecutar comando
switch ($comando.ToLower()) {
    "build" { Build-Project }
    "up" { Start-Services }
    "up-maria" { Start-MariaDB }
    "up-mongo" { Start-MongoDB }
    "down" { Stop-Services }
    "restart" { Restart-App }
    "logs" { Show-Logs }
    "logs-all" { Show-AllLogs }
    "ps" { Show-Status }
    "clean" { Clean-Docker }
    "help" { Show-Help }
    default { 
        Write-Host "❌ Comando desconocido: $comando" -ForegroundColor Red
        Show-Help 
    }
}
