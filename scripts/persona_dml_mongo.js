// =====================================================
// Script DML para MongoDB - Base de datos persona_db
// Descripción: Inserta datos de ejemplo en todas las colecciones
// =====================================================

db = db.getSiblingDB('persona_db');

// =====================================================
// Colección: profesion
// Insertar profesiones de ejemplo
// =====================================================
db.profesion.insertMany([
	{
		"_id": NumberInt(1),
		"nom": "Ingeniería de Sistemas",
		"des": "Profesional en desarrollo de software y sistemas de información",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	},
	{
		"_id": NumberInt(2),
		"nom": "Medicina",
		"des": "Profesional en ciencias de la salud y atención médica",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	},
	{
		"_id": NumberInt(3),
		"nom": "Derecho",
		"des": "Profesional en ciencias jurídicas y leyes",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	},
	{
		"_id": NumberInt(4),
		"nom": "Administración de Empresas",
		"des": "Profesional en gestión y dirección de organizaciones",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	},
	{
		"_id": NumberInt(5),
		"nom": "Psicología",
		"des": "Profesional en salud mental y comportamiento humano",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	},
	{
		"_id": NumberInt(6),
		"nom": "Arquitectura",
		"des": "Profesional en diseño y construcción de edificaciones",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	}
], { ordered: false })

print("✅ Insertadas 6 profesiones")

// =====================================================
// Colección: persona
// Insertar personas de ejemplo
// =====================================================
db.persona.insertMany([
	{
		"_id": NumberInt(123456789),
		"nombre": "Pepe",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(987654321),
		"nombre": "Pepito",
		"apellido": "Perez",
		"genero": "M",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(321654987),
		"nombre": "Pepa",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(147258369),
		"nombre": "Pepita",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(10),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(963852741),
		"nombre": "Fede",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(18),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(456789123),
		"nombre": "Ana",
		"apellido": "García",
		"genero": "F",
		"edad": NumberInt(28),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(789456123),
		"nombre": "Carlos",
		"apellido": "Rodríguez",
		"genero": "M",
		"edad": NumberInt(35),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(159753486),
		"nombre": "María",
		"apellido": "López",
		"genero": "F",
		"edad": NumberInt(42),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	}
], { ordered: false })

print("✅ Insertadas 8 personas")

// =====================================================
// Colección: telefono
// Insertar teléfonos asociados a las personas
// =====================================================
db.telefono.insertMany([
	{
		"_id": "3001234567",
		"oper": "Claro",
		"duenio": NumberInt(123456789),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3109876543",
		"oper": "Movistar",
		"duenio": NumberInt(123456789),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3201112233",
		"oper": "Tigo",
		"duenio": NumberInt(987654321),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3154445566",
		"oper": "Claro",
		"duenio": NumberInt(321654987),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3007778899",
		"oper": "Movistar",
		"duenio": NumberInt(147258369),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3186665544",
		"oper": "Tigo",
		"duenio": NumberInt(963852741),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3123334455",
		"oper": "Claro",
		"duenio": NumberInt(456789123),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3145556677",
		"oper": "Movistar",
		"duenio": NumberInt(789456123),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3167778899",
		"oper": "Tigo",
		"duenio": NumberInt(159753486),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	}
], { ordered: false })

print("✅ Insertados 9 teléfonos")

// =====================================================
// Colección: estudios
// Insertar estudios realizados por las personas
// =====================================================
db.estudios.insertMany([
	{
		"id_prof": NumberInt(1),
		"cc_per": NumberInt(123456789),
		"fecha": new Date("2015-06-15"),
		"univer": "Universidad Javeriana",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"id_prof": NumberInt(2),
		"cc_per": NumberInt(321654987),
		"fecha": new Date("2017-12-20"),
		"univer": "Universidad Nacional",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"id_prof": NumberInt(3),
		"cc_per": NumberInt(963852741),
		"fecha": new Date("2020-08-10"),
		"univer": "Universidad de los Andes",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"id_prof": NumberInt(1),
		"cc_per": NumberInt(456789123),
		"fecha": new Date("2018-06-30"),
		"univer": "Universidad Javeriana",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"id_prof": NumberInt(4),
		"cc_per": NumberInt(789456123),
		"fecha": new Date("2012-11-25"),
		"univer": "Universidad Externado",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"id_prof": NumberInt(5),
		"cc_per": NumberInt(159753486),
		"fecha": new Date("2008-05-18"),
		"univer": "Universidad El Bosque",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"id_prof": NumberInt(1),
		"cc_per": NumberInt(987654321),
		"univer": "Universidad Nacional",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"id_prof": NumberInt(6),
		"cc_per": NumberInt(123456789),
		"fecha": new Date("2019-12-15"),
		"univer": "Universidad de los Andes",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	}
], { ordered: false })

print("✅ Insertados 8 estudios")
print("✅ Todos los datos de ejemplo han sido insertados correctamente")
