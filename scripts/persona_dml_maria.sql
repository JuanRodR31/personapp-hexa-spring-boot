-- =====================================================
-- Script DML para MariaDB - Base de datos persona_db
-- Descripción: Inserta datos de ejemplo en todas las tablas
-- =====================================================

USE `persona_db`;

-- =====================================================
-- Tabla: profesion
-- Insertar profesiones de ejemplo
-- =====================================================
INSERT INTO `persona_db`.`profesion`(`id`,`nom`,`des`) 
VALUES
	(1, 'Ingeniería de Sistemas', 'Profesional en desarrollo de software y sistemas de información'),
	(2, 'Medicina', 'Profesional en ciencias de la salud y atención médica'),
	(3, 'Derecho', 'Profesional en ciencias jurídicas y leyes'),
	(4, 'Administración de Empresas', 'Profesional en gestión y dirección de organizaciones'),
	(5, 'Psicología', 'Profesional en salud mental y comportamiento humano'),
	(6, 'Arquitectura', 'Profesional en diseño y construcción de edificaciones');

-- =====================================================
-- Tabla: persona
-- Insertar personas de ejemplo
-- =====================================================
INSERT INTO `persona_db`.`persona`(`cc`,`nombre`,`apellido`,`genero`,`edad`) 
VALUES
	(123456789, 'Pepe', 'Perez', 'M', 30),
	(987654321, 'Pepito', 'Perez', 'M', NULL),
	(321654987, 'Pepa', 'Juarez', 'F', 30),
	(147258369, 'Pepita', 'Juarez', 'F', 10),
	(963852741, 'Fede', 'Perez', 'M', 18),
	(456789123, 'Ana', 'García', 'F', 28),
	(789456123, 'Carlos', 'Rodríguez', 'M', 35),
	(159753486, 'María', 'López', 'F', 42);

-- =====================================================
-- Tabla: telefono
-- Insertar teléfonos asociados a las personas
-- =====================================================
INSERT INTO `persona_db`.`telefono`(`num`,`oper`,`duenio`) 
VALUES
	('3001234567', 'Claro', 123456789),
	('3109876543', 'Movistar', 123456789),
	('3201112233', 'Tigo', 987654321),
	('3154445566', 'Claro', 321654987),
	('3007778899', 'Movistar', 147258369),
	('3186665544', 'Tigo', 963852741),
	('3123334455', 'Claro', 456789123),
	('3145556677', 'Movistar', 789456123),
	('3167778899', 'Tigo', 159753486);

-- =====================================================
-- Tabla: estudios
-- Insertar estudios realizados por las personas
-- =====================================================
INSERT INTO `persona_db`.`estudios`(`id_prof`,`cc_per`,`fecha`,`univer`) 
VALUES
	(1, 123456789, '2015-06-15', 'Universidad Javeriana'),
	(2, 321654987, '2017-12-20', 'Universidad Nacional'),
	(3, 963852741, '2020-08-10', 'Universidad de los Andes'),
	(1, 456789123, '2018-06-30', 'Universidad Javeriana'),
	(4, 789456123, '2012-11-25', 'Universidad Externado'),
	(5, 159753486, '2008-05-18', 'Universidad El Bosque'),
	(1, 987654321, NULL, 'Universidad Nacional'),
	(6, 123456789, '2019-12-15', 'Universidad de los Andes');

COMMIT;