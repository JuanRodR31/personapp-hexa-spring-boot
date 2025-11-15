FLUSH PRIVILEGES;
--
-- Eliminar usuario y esquema si existen
DROP USER IF EXISTS 'persona_db'@'%';
DROP SCHEMA IF EXISTS `persona_db`;
--
-- Crear usuario y esquema
CREATE USER IF NOT EXISTS 'persona_db'@'%' IDENTIFIED BY 'persona_db';
CREATE SCHEMA IF NOT EXISTS `persona_db`; 
--
-- Otorgar permisos al usuario
GRANT EXECUTE, TRIGGER, INSERT, UPDATE, DELETE, SELECT ON `persona_db`.* TO 'persona_db'@'%'; 
FLUSH PRIVILEGES; 
--
USE `persona_db`;
--
-- =====================================================
-- Tabla: profesion
-- Descripción: Almacena información de las profesiones
-- =====================================================
CREATE TABLE IF NOT EXISTS `persona_db`.`profesion` (
 `id` INT(6) NOT NULL AUTO_INCREMENT COMMENT 'Identificador único de la profesión',
 `nom` VARCHAR(90) NOT NULL COMMENT 'Nombre de la profesión',
 `des` TEXT NULL DEFAULT NULL COMMENT 'Descripción de la profesión',
 CONSTRAINT `profesion_pk` PRIMARY KEY (`id`),
 UNIQUE KEY `profesion_nom_uk` (`nom`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Catálogo de profesiones';
--
-- =====================================================
-- Tabla: persona
-- Descripción: Almacena información de las personas
-- =====================================================
CREATE TABLE IF NOT EXISTS `persona_db`.`persona` (
 `cc` INT(15) NOT NULL COMMENT 'Cédula de ciudadanía (Primary Key)',
 `nombre` VARCHAR(45) NOT NULL COMMENT 'Nombre de la persona',
 `apellido` VARCHAR(45) NOT NULL COMMENT 'Apellido de la persona',
 `genero` ENUM('M', 'F') NOT NULL COMMENT 'Género: M=Masculino, F=Femenino',
 `edad` INT(3) NULL DEFAULT NULL COMMENT 'Edad de la persona',
 CONSTRAINT `persona_pk` PRIMARY KEY (`cc`),
 INDEX `persona_nombre_idx` (`nombre`, `apellido`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Información de personas';
--
-- =====================================================
-- Tabla: telefono
-- Descripción: Almacena teléfonos asociados a personas
-- =====================================================
CREATE TABLE IF NOT EXISTS `persona_db`.`telefono` (
 `num` VARCHAR(15) NOT NULL COMMENT 'Número de teléfono (Primary Key)',
 `oper` VARCHAR(45) NOT NULL COMMENT 'Operador del teléfono',
 `duenio` INT(15) NOT NULL COMMENT 'CC del dueño del teléfono (FK a persona)',
 CONSTRAINT `telefono_pk` PRIMARY KEY (`num`), 
 CONSTRAINT `telefono_persona_fk` FOREIGN KEY (`duenio`) 
   REFERENCES `persona_db`.`persona` (`cc`)
   ON DELETE CASCADE
   ON UPDATE CASCADE,
 INDEX `telefono_duenio_idx` (`duenio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Teléfonos de personas';
--
-- =====================================================
-- Tabla: estudios
-- Descripción: Relación entre personas y profesiones (estudios realizados)
-- =====================================================
CREATE TABLE IF NOT EXISTS `persona_db`.`estudios` (
 `id_prof` INT(6) NOT NULL COMMENT 'ID de la profesión (FK a profesion)',
 `cc_per` INT(15) NOT NULL COMMENT 'CC de la persona (FK a persona)',
 `fecha` DATE NULL DEFAULT NULL COMMENT 'Fecha de graduación',
 `univer` VARCHAR(50) NULL DEFAULT NULL COMMENT 'Universidad donde estudió',
 CONSTRAINT `estudios_pk` PRIMARY KEY (`id_prof`, `cc_per`),
 CONSTRAINT `estudio_persona_fk` FOREIGN KEY (`cc_per`) 
   REFERENCES `persona_db`.`persona` (`cc`)
   ON DELETE CASCADE
   ON UPDATE CASCADE,
 CONSTRAINT `estudio_profesion_fk` FOREIGN KEY (`id_prof`) 
   REFERENCES `persona_db`.`profesion` (`id`)
   ON DELETE CASCADE
   ON UPDATE CASCADE,
 INDEX `estudios_persona_idx` (`cc_per`),
 INDEX `estudios_profesion_idx` (`id_prof`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Estudios realizados por las personas';
--
COMMIT;
FLUSH PRIVILEGES;