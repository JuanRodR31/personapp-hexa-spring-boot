package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import co.edu.javeriana.as.personapp.model.response.EstudiosResponse;

@Mapper
public class EstudiosMapperRest {
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
	
	public EstudiosResponse fromDomainToAdapterRestMaria(Study study) {
		return fromDomainToAdapterRest(study, "MariaDB");
	}
	
	public EstudiosResponse fromDomainToAdapterRestMongo(Study study) {
		return fromDomainToAdapterRest(study, "MongoDB");
	}
	
	public EstudiosResponse fromDomainToAdapterRest(Study study, String database) {
		return new EstudiosResponse(
				study.getProfession() != null ? study.getProfession().getIdentification() + "" : null,
				study.getPerson() != null ? study.getPerson().getIdentification() + "" : null,
				study.getGraduationDate() != null ? study.getGraduationDate().format(DATE_FORMATTER) : null,
				study.getUniversityName(),
				database,
				"OK");
	}

	public Study fromAdapterToDomain(EstudiosRequest request, Person person, Profession profession) {
		Study study = new Study();
		study.setPerson(person);
		study.setProfession(profession);
		study.setUniversityName(request.getUniversityName());
		
		// Parse graduation date if provided
		if (request.getGraduationDate() != null && !request.getGraduationDate().isEmpty()) {
			try {
				study.setGraduationDate(LocalDate.parse(request.getGraduationDate(), DATE_FORMATTER));
			} catch (Exception e) {
				study.setGraduationDate(null);
			}
		}
		
		return study;
	}
}
