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

import co.edu.javeriana.as.personapp.adapter.TelefonoInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/telefono")
@Tag(name = "Telefono", description = "API para gestión de teléfonos")
public class TelefonoControllerV1 {
	
	@Autowired
	private TelefonoInputAdapterRest telefonoInputAdapterRest;
	
	// GetAll - Obtener todos los teléfonos de una base de datos específica
	@Operation(summary = "Obtener todos los teléfonos", description = "Retorna una lista de todos los teléfonos de la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lista de teléfonos obtenida exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = TelefonoResponse.class))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TelefonoResponse> telefonos(@PathVariable String database) {
		log.info("Into telefonos REST API - GetAll");
		return telefonoInputAdapterRest.historial(database.toUpperCase());
	}
	
	// GetById - Obtener un teléfono por su número
	@Operation(summary = "Obtener teléfono por número", description = "Retorna un teléfono específico según su número")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Teléfono encontrado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = TelefonoResponse.class))),
		@ApiResponse(responseCode = "404", description = "Teléfono no encontrado"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TelefonoResponse> getTelefonoById(
			@PathVariable String database, 
			@PathVariable String number) {
		log.info("Into getTelefonoById REST API - database: {}, number: {}", database, number);
		TelefonoResponse response = telefonoInputAdapterRest.obtenerTelefono(database.toUpperCase(), number);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	// Count - Contar el número de teléfonos en una base de datos
	@Operation(summary = "Contar teléfonos", description = "Retorna el número total de teléfonos en la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Conteo realizado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> countTelefonos(@PathVariable String database) {
		log.info("Into countTelefonos REST API - database: {}", database);
		Integer count = telefonoInputAdapterRest.contarTelefonos(database.toUpperCase());
		return ResponseEntity.ok(count);
	}
	
	// Post (Create) - Crear un nuevo teléfono
	@Operation(summary = "Crear teléfono", description = "Crea un nuevo teléfono en la base de datos especificada")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Teléfono creado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = TelefonoResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TelefonoResponse> crearTelefono(@RequestBody TelefonoRequest request) {
		log.info("Into crearTelefono REST API");
		TelefonoResponse response = telefonoInputAdapterRest.crearTelefono(request);
		if (response != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	// Put (Update) - Actualizar un teléfono existente
	@Operation(summary = "Actualizar teléfono", description = "Actualiza los datos de un teléfono existente")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Teléfono actualizado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = TelefonoResponse.class))),
		@ApiResponse(responseCode = "404", description = "Teléfono no encontrado"),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@PutMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TelefonoResponse> actualizarTelefono(
			@PathVariable String database,
			@PathVariable String number,
			@RequestBody TelefonoRequest request) {
		log.info("Into actualizarTelefono REST API - database: {}, number: {}", database, number);
		request.setDatabase(database.toUpperCase());
		request.setNumber(number);
		TelefonoResponse response = telefonoInputAdapterRest.actualizarTelefono(request);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	// Delete - Eliminar un teléfono
	@Operation(summary = "Eliminar teléfono", description = "Elimina un teléfono de la base de datos según su número")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Teléfono eliminado exitosamente",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = TelefonoResponse.class))),
		@ApiResponse(responseCode = "404", description = "Teléfono no encontrado"),
		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@ResponseBody
	@DeleteMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TelefonoResponse> eliminarTelefono(
			@PathVariable String database,
			@PathVariable String number) {
		log.info("Into eliminarTelefono REST API - database: {}, number: {}", database, number);
		TelefonoResponse response = telefonoInputAdapterRest.eliminarTelefono(database.toUpperCase(), number);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
