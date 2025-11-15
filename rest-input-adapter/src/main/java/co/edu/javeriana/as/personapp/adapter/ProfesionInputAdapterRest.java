package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {

	@Autowired(required = false)
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired(required = false)
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperRest profesionMapperRest;

	@Autowired
	private ProfessionUseCase professionUseCase;

    private String configurePersistence(String dbOption) throws InvalidOptionException {
    	log.info("[configurePersistence] Maria adapter present? {} | Mongo adapter present? {}",
    		professionOutputPortMaria != null, professionOutputPortMongo != null);
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
        	if (professionOutputPortMaria == null) {
        		throw new InvalidOptionException("MariaDB adapter bean not initialized");
        	}
            professionUseCase.setPersistence(professionOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
        	if (professionOutputPortMongo == null) {
        		throw new InvalidOptionException("MongoDB adapter bean not initialized");
        	}
            professionUseCase.setPersistence(professionOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        }
        throw new InvalidOptionException("Invalid database option: " + dbOption);
    }

	public List<ProfesionResponse> historial(String database) {
		log.info("Into historial ProfesionEntity in Input Adapter");
		try {
			String selected = configurePersistence(database);
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return professionUseCase.findAll().stream()
						.map(profesionMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return professionUseCase.findAll().stream()
						.map(profesionMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (Exception e) {
			log.error("Error retrieving professions: {}", e.getMessage(), e);
			throw new RuntimeException("Error retrieving professions from database: " + database, e);
		}
	}

	public ProfesionResponse crearProfesion(ProfesionRequest request) {
		try {
			String selected = configurePersistence(request.getDatabase());
			Profession profession = professionUseCase.create(profesionMapperRest.fromAdapterToDomain(request));
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return profesionMapperRest.fromDomainToAdapterRestMaria(profession);
			} else {
				return profesionMapperRest.fromDomainToAdapterRestMongo(profession);
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public ProfesionResponse obtenerProfesion(String database, String id) {
		log.info("Into obtenerProfesion in Input Adapter - database: {}, id: {}", database, id);
		try {
			String selected = configurePersistence(database);
			Integer identification = Integer.parseInt(id);
			Profession profession = professionUseCase.findOne(identification);
			if (profession == null) {
				return null;
			}
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return profesionMapperRest.fromDomainToAdapterRestMaria(profession);
			} else {
				return profesionMapperRest.fromDomainToAdapterRestMongo(profession);
			}
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (NumberFormatException e) {
			log.error("Invalid profession ID format: {}", id);
			throw new RuntimeException("Invalid profession ID format: " + id, e);
		} catch (Exception e) {
			log.error("Error obtaining profession: {}", e.getMessage(), e);
			throw new RuntimeException("Error obtaining profession " + id + " from database: " + database, e);
		}
	}

	public Integer contarProfesiones(String database) {
		log.info("Into contarProfesiones in Input Adapter - database: {}", database);
		try {
			configurePersistence(database);
			return professionUseCase.count();
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (Exception e) {
			log.error("Error counting professions: {}", e.getMessage(), e);
			throw new RuntimeException("Error counting professions from database: " + database, e);
		}
	}

	public ProfesionResponse actualizarProfesion(ProfesionRequest request) {
		log.info("Into actualizarProfesion in Input Adapter");
		try {
			String selected = configurePersistence(request.getDatabase());
			Integer identification = Integer.parseInt(request.getId());
			Profession profession = profesionMapperRest.fromAdapterToDomain(request);
			Profession updatedProfession = professionUseCase.edit(identification, profession);
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return profesionMapperRest.fromDomainToAdapterRestMaria(updatedProfession);
			} else {
				return profesionMapperRest.fromDomainToAdapterRestMongo(updatedProfession);
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		} catch (Exception e) {
			log.warn("Error updating profession: " + e.getMessage());
			return null;
		}
	}

	public ProfesionResponse eliminarProfesion(String database, String id) {
		log.info("Into eliminarProfesion in Input Adapter - database: {}, id: {}", database, id);
		try {
			String selected = configurePersistence(database);
			Integer identification = Integer.parseInt(id);
			Profession profession = professionUseCase.findOne(identification);
			Boolean deleted = professionUseCase.drop(identification);
			if (deleted) {
				ProfesionResponse response;
				if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
					response = profesionMapperRest.fromDomainToAdapterRestMaria(profession);
				} else {
					response = profesionMapperRest.fromDomainToAdapterRestMongo(profession);
				}
				response.setStatus("Deleted successfully");
				return response;
			}
			return null;
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		} catch (Exception e) {
			log.warn("Error deleting profession: " + e.getMessage());
			return null;
		}
	}
}
