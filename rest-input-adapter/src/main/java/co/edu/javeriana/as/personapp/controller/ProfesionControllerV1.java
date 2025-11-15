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

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesion")
@Tag(name = "Profesion", description = "API para gestión de profesiones")
public class ProfesionControllerV1 {
	
	@Autowired
	private ProfesionInputAdapterRest profesionInputAdapterRest;
	
	// GetAll - Obtener todas las profesiones de una base de datos específica
	@Operation(summary = "Obtener todas las profesiones", description = "Retorna una lista de todas las profesiones de la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lista de profesiones obtenida exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesionResponse.class))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProfesionResponse> profesiones(@PathVariable String database) {
		log.info("Into profesiones REST API - GetAll");
		return profesionInputAdapterRest.historial(database.toUpperCase());
	}
	
	// GetById - Obtener una profesión por su ID
	@Operation(summary = "Obtener profesión por ID", description = "Retorna una profesión específica según su ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Profesión encontrada exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesionResponse.class))),
		@ApiResponse(responseCode = "404", description = "Profesión no encontrada"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfesionResponse> getProfesionById(
			@PathVariable String database, 
			@PathVariable String id) {
		log.info("Into getProfesionById REST API - database: {}, id: {}", database, id);
		ProfesionResponse response = profesionInputAdapterRest.obtenerProfesion(database.toUpperCase(), id);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	// Count - Contar el número de profesiones en una base de datos
	@Operation(summary = "Contar profesiones", description = "Retorna el número total de profesiones en la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Conteo realizado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> countProfesiones(@PathVariable String database) {
		log.info("Into countProfesiones REST API - database: {}", database);
		Integer count = profesionInputAdapterRest.contarProfesiones(database.toUpperCase());
		return ResponseEntity.ok(count);
	}
	
	// Post (Create) - Crear una nueva profesión
	@Operation(summary = "Crear profesión", description = "Crea una nueva profesión en la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Profesión creada exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesionResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfesionResponse> crearProfesion(@RequestBody ProfesionRequest request) {
		log.info("Into crearProfesion REST API");
		ProfesionResponse response = profesionInputAdapterRest.crearProfesion(request);
		if (response != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	// Put (Update) - Actualizar una profesión existente
	@Operation(summary = "Actualizar profesión", description = "Actualiza los datos de una profesión existente")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Profesión actualizada exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesionResponse.class))),
		@ApiResponse(responseCode = "404", description = "Profesión no encontrada"),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@PutMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfesionResponse> actualizarProfesion(
			@PathVariable String database,
			@PathVariable String id,
			@RequestBody ProfesionRequest request) {
		log.info("Into actualizarProfesion REST API - database: {}, id: {}", database, id);
		request.setDatabase(database.toUpperCase());
		request.setId(id);
		ProfesionResponse response = profesionInputAdapterRest.actualizarProfesion(request);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	// Delete - Eliminar una profesión
	@Operation(summary = "Eliminar profesión", description = "Elimina una profesión de la base de datos según su ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Profesión eliminada exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesionResponse.class))),
		@ApiResponse(responseCode = "404", description = "Profesión no encontrada"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@DeleteMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfesionResponse> eliminarProfesion(
			@PathVariable String database,
			@PathVariable String id) {
		log.info("Into eliminarProfesion REST API - database: {}, id: {}", database, id);
		ProfesionResponse response = profesionInputAdapterRest.eliminarProfesion(database.toUpperCase(), id);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
