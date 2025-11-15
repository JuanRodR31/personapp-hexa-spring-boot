package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterRest {

	@Autowired(required = false)
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired(required = false)
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private TelefonoMapperRest telefonoMapperRest;

	@Autowired
	private PhoneUseCase phoneUseCase;

	@Autowired
	private PersonUseCase personUseCase;

    private String configurePersistence(String dbOption) throws InvalidOptionException {
    	log.info("[configurePersistence] Maria adapter present? {} | Mongo adapter present? {}",
    		phoneOutputPortMaria != null, phoneOutputPortMongo != null);
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
        	if (phoneOutputPortMaria == null) {
        		throw new InvalidOptionException("MariaDB adapter bean not initialized");
        	}
            phoneUseCase.setPersistence(phoneOutputPortMaria);
            personUseCase.setPersistence(personOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
        	if (phoneOutputPortMongo == null) {
        		throw new InvalidOptionException("MongoDB adapter bean not initialized");
        	}
            phoneUseCase.setPersistence(phoneOutputPortMongo);
            personUseCase.setPersistence(personOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        }
        throw new InvalidOptionException("Invalid database option: " + dbOption);
    }

	public List<TelefonoResponse> historial(String database) {
		log.info("Into historial TelefonoEntity in Input Adapter");
		try {
			String selected = configurePersistence(database);
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return phoneUseCase.findAll().stream()
						.map(telefonoMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return phoneUseCase.findAll().stream()
						.map(telefonoMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (Exception e) {
			log.error("Error retrieving phones: {}", e.getMessage(), e);
			throw new RuntimeException("Error retrieving phones from database: " + database, e);
		}
	}

	public TelefonoResponse crearTelefono(TelefonoRequest request) {
		try {
			String selected = configurePersistence(request.getDatabase());
			// Obtener el owner (persona)
			Integer ownerDni = Integer.parseInt(request.getOwnerDni());
			Person owner = personUseCase.findOne(ownerDni);
			
			if (owner == null) {
				log.warn("Owner person with DNI {} not found", ownerDni);
				return null;
			}
			
			Phone phone = telefonoMapperRest.fromAdapterToDomain(request, owner);
			phone = phoneUseCase.create(phone);
			
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return telefonoMapperRest.fromDomainToAdapterRestMaria(phone);
			} else {
				return telefonoMapperRest.fromDomainToAdapterRestMongo(phone);
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error("Error creating phone: {}", e.getMessage(), e);
			return null;
		}
	}

	public TelefonoResponse obtenerTelefono(String database, String number) {
		log.info("Into obtenerTelefono in Input Adapter - database: {}, number: {}", database, number);
		try {
			String selected = configurePersistence(database);
			Phone phone = phoneUseCase.findOne(number);
			if (phone == null) {
				return null;
			}
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return telefonoMapperRest.fromDomainToAdapterRestMaria(phone);
			} else {
				return telefonoMapperRest.fromDomainToAdapterRestMongo(phone);
			}
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (Exception e) {
			log.error("Error obtaining phone: {}", e.getMessage(), e);
			throw new RuntimeException("Error obtaining phone " + number + " from database: " + database, e);
		}
	}

	public Integer contarTelefonos(String database) {
		log.info("Into contarTelefonos in Input Adapter - database: {}", database);
		try {
			configurePersistence(database);
			return phoneUseCase.count();
		} catch (InvalidOptionException e) {
			log.error("Invalid database option: {}", e.getMessage());
			throw new RuntimeException("Invalid database option: " + database, e);
		} catch (Exception e) {
			log.error("Error counting phones: {}", e.getMessage(), e);
			throw new RuntimeException("Error counting phones from database: " + database, e);
		}
	}

	public TelefonoResponse actualizarTelefono(TelefonoRequest request) {
		log.info("Into actualizarTelefono in Input Adapter");
		try {
			String selected = configurePersistence(request.getDatabase());
			// Obtener el owner (persona)
			Integer ownerDni = Integer.parseInt(request.getOwnerDni());
			Person owner = personUseCase.findOne(ownerDni);
			
			Phone phone = telefonoMapperRest.fromAdapterToDomain(request, owner);
			Phone updatedPhone = phoneUseCase.edit(request.getNumber(), phone);
			
			if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return telefonoMapperRest.fromDomainToAdapterRestMaria(updatedPhone);
			} else {
				return telefonoMapperRest.fromDomainToAdapterRestMongo(updatedPhone);
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		} catch (Exception e) {
			log.warn("Error updating phone: " + e.getMessage());
			return null;
		}
	}

	public TelefonoResponse eliminarTelefono(String database, String number) {
		log.info("Into eliminarTelefono in Input Adapter - database: {}, number: {}", database, number);
		try {
			String selected = configurePersistence(database);
			Phone phone = phoneUseCase.findOne(number);
			Boolean deleted = phoneUseCase.drop(number);
			if (deleted) {
				TelefonoResponse response;
				if (selected.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
					response = telefonoMapperRest.fromDomainToAdapterRestMaria(phone);
				} else {
					response = telefonoMapperRest.fromDomainToAdapterRestMongo(phone);
				}
				response.setStatus("Deleted successfully");
				return response;
			}
			return null;
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		} catch (Exception e) {
			log.warn("Error deleting phone: " + e.getMessage());
			return null;
		}
	}
}
