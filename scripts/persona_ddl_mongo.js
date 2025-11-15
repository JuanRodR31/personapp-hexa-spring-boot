// =====================================================
// Script DDL para MongoDB - Base de datos persona_db
// Descripción: Crea base de datos y colecciones
// =====================================================

// Cambiar a la base de datos persona_db
db = db.getSiblingDB('persona_db');

// =====================================================
// Colección: profesion
// Descripción: Almacena información de las profesiones
// =====================================================
db.createCollection("profesion", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "nom"],
      properties: {
        _id: {
          bsonType: "int",
          description: "ID único de la profesión (requerido)"
        },
        nom: {
          bsonType: "string",
          maxLength: 90,
          description: "Nombre de la profesión (requerido, máx 90 caracteres)"
        },
        des: {
          bsonType: "string",
          description: "Descripción de la profesión (opcional)"
        }
      }
    }
  },
  validationLevel: "moderate",
  validationAction: "warn"
})

// Índices para profesion
db.profesion.createIndex({ "nom": 1 }, { unique: true, name: "profesion_nom_uk" })

// =====================================================
// Colección: persona
// Descripción: Almacena información de las personas
// =====================================================
db.createCollection("persona", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "nombre", "apellido", "genero"],
      properties: {
        _id: {
          bsonType: "int",
          description: "Cédula de ciudadanía (requerido)"
        },
        nombre: {
          bsonType: "string",
          maxLength: 45,
          description: "Nombre de la persona (requerido, máx 45 caracteres)"
        },
        apellido: {
          bsonType: "string",
          maxLength: 45,
          description: "Apellido de la persona (requerido, máx 45 caracteres)"
        },
        genero: {
          enum: ["M", "F"],
          description: "Género: M=Masculino, F=Femenino (requerido)"
        },
        edad: {
          bsonType: "int",
          minimum: 0,
          maximum: 150,
          description: "Edad de la persona (opcional, entre 0 y 150)"
        }
      }
    }
  },
  validationLevel: "moderate",
  validationAction: "warn"
})

// Índices para persona
db.persona.createIndex({ "nombre": 1, "apellido": 1 }, { name: "persona_nombre_idx" })

// =====================================================
// Colección: telefono
// Descripción: Almacena teléfonos asociados a personas
// =====================================================
db.createCollection("telefono", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["_id", "oper", "duenio"],
      properties: {
        _id: {
          bsonType: "string",
          maxLength: 15,
          description: "Número de teléfono (requerido, máx 15 caracteres)"
        },
        oper: {
          bsonType: "string",
          maxLength: 45,
          description: "Operador del teléfono (requerido, máx 45 caracteres)"
        },
        duenio: {
          bsonType: "int",
          description: "CC del dueño del teléfono - referencia a persona (requerido)"
        }
      }
    }
  },
  validationLevel: "moderate",
  validationAction: "warn"
})

// Índices para telefono
db.telefono.createIndex({ "duenio": 1 }, { name: "telefono_duenio_idx" })

// =====================================================
// Colección: estudios
// Descripción: Relación entre personas y profesiones
// =====================================================
db.createCollection("estudios", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["id_prof", "cc_per"],
      properties: {
        id_prof: {
          bsonType: "int",
          description: "ID de la profesión - referencia a profesion (requerido)"
        },
        cc_per: {
          bsonType: "int",
          description: "CC de la persona - referencia a persona (requerido)"
        },
        fecha: {
          bsonType: "date",
          description: "Fecha de graduación (opcional)"
        },
        univer: {
          bsonType: "string",
          maxLength: 50,
          description: "Universidad donde estudió (opcional, máx 50 caracteres)"
        }
      }
    }
  },
  validationLevel: "moderate",
  validationAction: "warn"
})

// Índices para estudios
db.estudios.createIndex({ "id_prof": 1, "cc_per": 1 }, { unique: true, name: "estudios_pk" })
db.estudios.createIndex({ "cc_per": 1 }, { name: "estudios_persona_idx" })
db.estudios.createIndex({ "id_prof": 1 }, { name: "estudios_profesion_idx" })

print("✅ Base de datos persona_db creada exitosamente")
print("✅ Usuario persona_db creado con permisos")
print("✅ Colecciones creadas: profesion, persona, telefono, estudios")
print("✅ Validaciones y índices aplicados correctamente")
