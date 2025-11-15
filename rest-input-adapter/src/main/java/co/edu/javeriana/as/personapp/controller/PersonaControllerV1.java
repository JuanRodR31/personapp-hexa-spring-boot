package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
@Tag(name = "Persona", description = "API para gestión de personas")
public class PersonaControllerV1 {
	
	@Autowired
	private PersonaInputAdapterRest personaInputAdapterRest;
	
	// GetAll - Obtener todas las personas de una base de datos específica
	@Operation(summary = "Obtener todas las personas", description = "Retorna una lista de todas las personas de la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lista de personas obtenida exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonaResponse.class))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PersonaResponse> personas(@PathVariable String database) {
		log.info("Into personas REST API - GetAll");
		return personaInputAdapterRest.historial(database.toUpperCase());
	}
	
	// GetById - Obtener una persona por su ID (dni/cc)
	@Operation(summary = "Obtener persona por DNI", description = "Retorna una persona específica según su DNI")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Persona encontrada exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonaResponse.class))),
		@ApiResponse(responseCode = "404", description = "Persona no encontrada"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}/{dni}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonaResponse> getPersonaById(
			@PathVariable String database, 
			@PathVariable String dni) {
		log.info("Into getPersonaById REST API - database: {}, dni: {}", database, dni);
		PersonaResponse response = personaInputAdapterRest.obtenerPersona(database.toUpperCase(), dni);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	// Count - Contar el número de personas en una base de datos
	@Operation(summary = "Contar personas", description = "Retorna el número total de personas en la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Conteo realizado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> countPersonas(@PathVariable String database) {
		log.info("Into countPersonas REST API - database: {}", database);
		Integer count = personaInputAdapterRest.contarPersonas(database.toUpperCase());
		return ResponseEntity.ok(count);
	}
	
	// Post (Create) - Crear una nueva persona
	@Operation(summary = "Crear persona", description = "Crea una nueva persona en la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Persona creada exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonaResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonaResponse> crearPersona(@RequestBody PersonaRequest request) {
		log.info("Into crearPersona REST API");
		PersonaResponse response = personaInputAdapterRest.crearPersona(request);
		if (response != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	// Put (Update) - Actualizar una persona existente
	@Operation(summary = "Actualizar persona", description = "Actualiza los datos de una persona existente")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Persona actualizada exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonaResponse.class))),
		@ApiResponse(responseCode = "404", description = "Persona no encontrada"),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@PutMapping(path = "/{database}/{dni}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonaResponse> actualizarPersona(
			@PathVariable String database,
			@PathVariable String dni,
			@RequestBody PersonaRequest request) {
		log.info("Into actualizarPersona REST API - database: {}, dni: {}", database, dni);
		request.setDatabase(database.toUpperCase());
		request.setDni(dni);
		PersonaResponse response = personaInputAdapterRest.actualizarPersona(request);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	// Delete - Eliminar una persona
	@Operation(summary = "Eliminar persona", description = "Elimina una persona de la base de datos según su DNI")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Persona eliminada exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonaResponse.class))),
		@ApiResponse(responseCode = "404", description = "Persona no encontrada"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@DeleteMapping(path = "/{database}/{dni}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonaResponse> eliminarPersona(
			@PathVariable String database,
			@PathVariable String dni) {
		log.info("Into eliminarPersona REST API - database: {}, dni: {}", database, dni);
		PersonaResponse response = personaInputAdapterRest.eliminarPersona(database.toUpperCase(), dni);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
