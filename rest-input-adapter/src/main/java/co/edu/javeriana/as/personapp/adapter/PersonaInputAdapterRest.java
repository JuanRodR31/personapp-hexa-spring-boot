package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

	// Fail fast if a persistence adapter is missing (should not be null). Removing required=false.
	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperRest personaMapperRest;

	@Autowired
	private PersonUseCase personUseCase; // leverage Spring-managed instance (@UseCase)

    private String configurePersistence(String dbOption) throws InvalidOptionException {
    	log.info("[configurePersistence] Maria adapter present? {} | Mongo adapter present? {}",
    		personOutputPortMaria != null, personOutputPortMongo != null);
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
        	if (personOutputPortMaria == null) {
        		throw new InvalidOptionException("MariaDB adapter bean not initialized");
        	}
            personUseCase.setPersistence(personOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
        	if (personOutputPortMongo == null) {
        		throw new InvalidOptionException("MongoDB adapter bean not initialized");
        	}
            personUseCase.setPersistence(personOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        }
        throw new InvalidOptionException("Invalid database option: " + dbOption);
    }

	public List<PersonaResponse> historial(String database) {
		log.info("Into historial PersonaEntity in Input Adapter");
		try {
			String selected = configurePersistence(database);
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return personUseCase.findAll().stream()
						.map(personaMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return personUseCase.findAll().stream()
						.map(personaMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (Exception e) {
			log.error("Error retrieving persons: {}", e.getMessage(), e);
			throw new RuntimeException("Error retrieving persons from database: " + database, e);
		}
	}

	public PersonaResponse crearPersona(PersonaRequest request) {
		try {
			String selected = configurePersistence(request.getDatabase());
			Person person = personUseCase.create(personaMapperRest.fromAdapterToDomain(request));
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return personaMapperRest.fromDomainToAdapterRestMaria(person);
			} else {
				return personaMapperRest.fromDomainToAdapterRestMongo(person);
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public PersonaResponse obtenerPersona(String database, String dni) {
		log.info("Into obtenerPersona in Input Adapter - database: {}, dni: {}", database, dni);
		try {
			String selected = configurePersistence(database);
			Integer identification = Integer.parseInt(dni);
			Person person = personUseCase.findOne(identification);
			if (person == null) {
				return null;
			}
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return personaMapperRest.fromDomainToAdapterRestMaria(person);
			} else {
				return personaMapperRest.fromDomainToAdapterRestMongo(person);
			}
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (NumberFormatException e) {
			log.error("Invalid DNI format: {}", dni);
			throw new RuntimeException("Invalid DNI format: " + dni, e);
		} catch (Exception e) {
			log.error("Error obtaining person: {}", e.getMessage(), e);
			throw new RuntimeException("Error obtaining person " + dni + " from database: " + database, e);
		}
	}

	public Integer contarPersonas(String database) {
		log.info("Into contarPersonas in Input Adapter - database: {}", database);
		try {
			configurePersistence(database);
			return personUseCase.count();
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (Exception e) {
			log.error("Error counting persons: {}", e.getMessage(), e);
			throw new RuntimeException("Error counting persons from database: " + database, e);
		}
	}

	public PersonaResponse actualizarPersona(PersonaRequest request) {
		log.info("Into actualizarPersona in Input Adapter");
		try {
			String selected = configurePersistence(request.getDatabase());
			Integer identification = Integer.parseInt(request.getDni());
			Person person = personaMapperRest.fromAdapterToDomain(request);
			Person updatedPerson = personUseCase.edit(identification, person);
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return personaMapperRest.fromDomainToAdapterRestMaria(updatedPerson);
			} else {
				return personaMapperRest.fromDomainToAdapterRestMongo(updatedPerson);
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		} catch (Exception e) {
			log.warn("Error updating person: " + e.getMessage());
			return null;
		}
	}

	public PersonaResponse eliminarPersona(String database, String dni) {
		log.info("Into eliminarPersona in Input Adapter - database: {}, dni: {}", database, dni);
		try {
			String selected = configurePersistence(database);
			Integer identification = Integer.parseInt(dni);
			// Obtener la persona antes de eliminarla para devolver su información
			Person person = personUseCase.findOne(identification);
			Boolean deleted = personUseCase.drop(identification);
			if (deleted) {
				PersonaResponse response;
				if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
					response = personaMapperRest.fromDomainToAdapterRestMaria(person);
				} else {
					response = personaMapperRest.fromDomainToAdapterRestMongo(person);
				}
				response.setStatus("Deleted successfully");
				return response;
			}
			return null;
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		} catch (Exception e) {
			log.warn("Error deleting person: " + e.getMessage());
			return null;
		}
	}

}
