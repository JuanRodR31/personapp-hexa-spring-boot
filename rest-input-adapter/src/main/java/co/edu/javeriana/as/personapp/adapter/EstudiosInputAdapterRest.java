package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudiosMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import co.edu.javeriana.as.personapp.model.response.EstudiosResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudiosInputAdapterRest {

	@Autowired(required = false)
	@Qualifier("studyOutputAdapterMaria")
	private StudyOutputPort studyOutputPortMaria;

	@Autowired(required = false)
	@Qualifier("studyOutputAdapterMongo")
	private StudyOutputPort studyOutputPortMongo;

	@Autowired(required = false)
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired(required = false)
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired(required = false)
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired(required = false)
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private EstudiosMapperRest estudiosMapperRest;

	@Autowired
	private StudyUseCase studyUseCase;

	@Autowired
	private PersonUseCase personUseCase;

	@Autowired
	private ProfessionUseCase professionUseCase;

	private String configurePersistence(String dbOption) throws InvalidOptionException {
		log.info("[configurePersistence] Maria adapter present? {} | Mongo adapter present? {}",
			studyOutputPortMaria != null, studyOutputPortMongo != null);
		if (dbOption == null) {
			throw new InvalidOptionException("Invalid database option: null");
		}
		String normalized = dbOption.trim().toUpperCase();
		// Accept variants like "MARIA", "MARIADB", "MariaDB"
		if (normalized.contains("MARIA")) {
			if (studyOutputPortMaria == null) {
				throw new InvalidOptionException("MariaDB adapter bean not initialized");
			}
			studyUseCase.setPersistence(studyOutputPortMaria);
			personUseCase.setPersistence(personOutputPortMaria);
			professionUseCase.setPersistence(professionOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (normalized.contains("MONGO")) {
			if (studyOutputPortMongo == null) {
				throw new InvalidOptionException("MongoDB adapter bean not initialized");
			}
			studyUseCase.setPersistence(studyOutputPortMongo);
			personUseCase.setPersistence(personOutputPortMongo);
			professionUseCase.setPersistence(professionOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		}
		throw new InvalidOptionException("Invalid database option: " + dbOption);
	}

	public List<EstudiosResponse> historial(String database) {
		log.info("Into historial EstudiosEntity in Input Adapter");
		try {
			String selected = configurePersistence(database);
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return studyUseCase.findAll().stream()
						.map(estudiosMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return studyUseCase.findAll().stream()
						.map(estudiosMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (Exception e) {
			log.error("Error retrieving studies: {}", e.getMessage(), e);
			throw new RuntimeException("Error retrieving studies from database: " + database, e);
		}
	}

	public EstudiosResponse crearEstudios(EstudiosRequest request) {
		try {
			String selected = configurePersistence(request.getDatabase());
			// Obtener persona y profesión
			Integer personDni = Integer.parseInt(request.getPersonDni());
			Integer professionId = Integer.parseInt(request.getProfessionId());
			
			Person person = personUseCase.findOne(personDni);
			Profession profession = professionUseCase.findOne(professionId);
			
			Study study = estudiosMapperRest.fromAdapterToDomain(request, person, profession);
			study = studyUseCase.create(study);
			
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return estudiosMapperRest.fromDomainToAdapterRestMaria(study);
			} else {
				return estudiosMapperRest.fromDomainToAdapterRestMongo(study);
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		} catch (Exception e) {
			log.warn("Error creating study: " + e.getMessage());
			return null;
		}
	}

	public EstudiosResponse obtenerEstudios(String database, String professionId, String personDni) {
		log.info("Into obtenerEstudios in Input Adapter - database: {}, professionId: {}, personDni: {}", database, professionId, personDni);
		try {
			String selected = configurePersistence(database);
			Integer profId = Integer.parseInt(professionId);
			Integer persId = Integer.parseInt(personDni);
			
			Study study = studyUseCase.findOne(profId, persId);
			if (study == null) {
				return null;
			}
			
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return estudiosMapperRest.fromDomainToAdapterRestMaria(study);
			} else {
				return estudiosMapperRest.fromDomainToAdapterRestMongo(study);
			}
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (NumberFormatException e) {
			log.error("Invalid ID format - professionId: {}, personDni: {}", professionId, personDni);
			throw new RuntimeException("Invalid ID format - professionId: " + professionId + ", personDni: " + personDni, e);
		} catch (Exception e) {
			log.error("Error obtaining study: {}", e.getMessage(), e);
			throw new RuntimeException("Error obtaining study (professionId: " + professionId + ", personDni: " + personDni + ") from database: " + database, e);
		}
	}

	public Integer contarEstudios(String database) {
		log.info("Into contarEstudios in Input Adapter - database: {}", database);
		try {
			configurePersistence(database);
			return studyUseCase.count();
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (Exception e) {
			log.error("Error counting studies: {}", e.getMessage(), e);
			throw new RuntimeException("Error counting studies from database: " + database, e);
		}
	}

	public EstudiosResponse actualizarEstudios(EstudiosRequest request) {
		log.info("Into actualizarEstudios in Input Adapter");
		try {
			String selected = configurePersistence(request.getDatabase());
			// Obtener persona y profesión
			Integer personDni = Integer.parseInt(request.getPersonDni());
			Integer professionId = Integer.parseInt(request.getProfessionId());
			
			Person person = personUseCase.findOne(personDni);
			Profession profession = professionUseCase.findOne(professionId);
			
			Study study = estudiosMapperRest.fromAdapterToDomain(request, person, profession);
			Study updatedStudy = studyUseCase.edit(professionId, personDni, study);
			
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return estudiosMapperRest.fromDomainToAdapterRestMaria(updatedStudy);
			} else {
				return estudiosMapperRest.fromDomainToAdapterRestMongo(updatedStudy);
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		} catch (Exception e) {
			log.warn("Error updating study: " + e.getMessage());
			return null;
		}
	}

	public EstudiosResponse eliminarEstudios(String database, String professionId, String personDni) {
		log.info("Into eliminarEstudios in Input Adapter - database: {}, professionId: {}, personDni: {}", database, professionId, personDni);
		try {
			String selected = configurePersistence(database);
			Integer profId = Integer.parseInt(professionId);
			Integer persId = Integer.parseInt(personDni);
			
			Study study = studyUseCase.findOne(profId, persId);
			Boolean deleted = studyUseCase.drop(profId, persId);
			
			if (deleted) {
				EstudiosResponse response;
				if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
					response = estudiosMapperRest.fromDomainToAdapterRestMaria(study);
				} else {
					response = estudiosMapperRest.fromDomainToAdapterRestMongo(study);
				}
				response.setStatus("Deleted successfully");
				return response;
			}
			return null;
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		} catch (Exception e) {
			log.warn("Error deleting study: " + e.getMessage());
			return null;
		}
	}
}
