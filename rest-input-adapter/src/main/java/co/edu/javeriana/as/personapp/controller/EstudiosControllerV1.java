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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.EstudiosInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import co.edu.javeriana.as.personapp.model.response.EstudiosResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudios")
@Tag(name = "Estudios", description = "API para gestión de estudios")
public class EstudiosControllerV1 {
	
	@Autowired
	private EstudiosInputAdapterRest estudiosInputAdapterRest;
	
	// GetAll - Obtener todos los estudios de una base de datos específica
	@Operation(summary = "Obtener todos los estudios", description = "Retorna una lista de todos los estudios de la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lista de estudios obtenida exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudiosResponse.class))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EstudiosResponse> estudios(@PathVariable String database) {
		log.info("Into estudios REST API - GetAll");
		return estudiosInputAdapterRest.historial(database.toUpperCase());
	}
	
	// GetById - Obtener un estudio por su ID compuesto (professionId y personDni)
	@Operation(summary = "Obtener estudio por ID compuesto", description = "Retorna un estudio específico según su ID de profesión y DNI de persona")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Estudio encontrado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudiosResponse.class))),
		@ApiResponse(responseCode = "404", description = "Estudio no encontrado"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}/{professionId}/{personDni}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EstudiosResponse> getEstudiosById(
			@PathVariable String database, 
			@PathVariable String professionId,
			@PathVariable String personDni) {
		log.info("Into getEstudiosById REST API - database: {}, professionId: {}, personDni: {}", database, professionId, personDni);
		EstudiosResponse response = estudiosInputAdapterRest.obtenerEstudios(database.toUpperCase(), professionId, personDni);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	// Count - Contar el número de estudios en una base de datos
	@Operation(summary = "Contar estudios", description = "Retorna el número total de estudios en la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Conteo realizado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> countEstudios(@PathVariable String database) {
		log.info("Into countEstudios REST API - database: {}", database);
		Integer count = estudiosInputAdapterRest.contarEstudios(database.toUpperCase());
		return ResponseEntity.ok(count);
	}
	
	// Post (Create) - Crear un nuevo estudio
	@Operation(summary = "Crear estudio", description = "Crea un nuevo estudio en la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Estudio creado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudiosResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EstudiosResponse> crearEstudios(@RequestBody EstudiosRequest request) {
		log.info("Into crearEstudios REST API");
		EstudiosResponse response = estudiosInputAdapterRest.crearEstudios(request);
		if (response != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	// Put (Update) - Actualizar un estudio existente
	@Operation(summary = "Actualizar estudio", description = "Actualiza los datos de un estudio existente")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Estudio actualizado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudiosResponse.class))),
		@ApiResponse(responseCode = "404", description = "Estudio no encontrado"),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@PutMapping(path = "/{database}/{professionId}/{personDni}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EstudiosResponse> actualizarEstudios(
			@PathVariable String database,
			@PathVariable String professionId,
			@PathVariable String personDni,
			@RequestBody EstudiosRequest request) {
		log.info("Into actualizarEstudios REST API - database: {}, professionId: {}, personDni: {}", database, professionId, personDni);
		request.setDatabase(database.toUpperCase());
		request.setProfessionId(professionId);
		request.setPersonDni(personDni);
		EstudiosResponse response = estudiosInputAdapterRest.actualizarEstudios(request);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	// Delete - Eliminar un estudio
	@Operation(summary = "Eliminar estudio", description = "Elimina un estudio de la base de datos según su ID compuesto")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Estudio eliminado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudiosResponse.class))),
		@ApiResponse(responseCode = "404", description = "Estudio no encontrado"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@DeleteMapping(path = "/{database}/{professionId}/{personDni}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EstudiosResponse> eliminarEstudios(
			@PathVariable String database,
			@PathVariable String professionId,
			@PathVariable String personDni) {
		log.info("Into eliminarEstudios REST API - database: {}, professionId: {}, personDni: {}", database, professionId, personDni);
		EstudiosResponse response = estudiosInputAdapterRest.eliminarEstudios(database.toUpperCase(), professionId, personDni);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
