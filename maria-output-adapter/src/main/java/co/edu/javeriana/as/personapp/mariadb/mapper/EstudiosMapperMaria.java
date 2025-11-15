package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.repository.PersonaRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Mapper
public class EstudiosMapperMaria {

	@Autowired
	private PersonaMapperMaria personaMapperMaria;

	@Autowired
	private ProfesionMapperMaria profesionMapperMaria;

	@Autowired
	private PersonaRepositoryMaria personaRepositoryMaria;

	@Autowired
	private ProfesionRepositoryMaria profesionRepositoryMaria;

	public EstudiosEntity fromDomainToAdapter(Study study) {
		EstudiosEntityPK estudioPK = new EstudiosEntityPK();
		estudioPK.setCcPer(study.getPerson().getIdentification());
		estudioPK.setIdProf(study.getProfession().getIdentification());
		EstudiosEntity estudio = new EstudiosEntity();
		estudio.setEstudiosPK(estudioPK);
		estudio.setFecha(validateFecha(study.getGraduationDate()));
		estudio.setUniver(validateUniver(study.getUniversityName()));
		
		// Fetch and set PersonaEntity and ProfesionEntity
		// These are needed for proper mapping back to domain
		try {
			PersonaEntity personaEntity = personaRepositoryMaria.findById(study.getPerson().getIdentification()).orElse(null);
			ProfesionEntity profesionEntity = profesionRepositoryMaria.findById(study.getProfession().getIdentification()).orElse(null);
			
			if (personaEntity != null) {
				estudio.setPersona(personaEntity);
			} else {
				log.warn("PersonaEntity not found for ID: {}", study.getPerson().getIdentification());
			}
			
			if (profesionEntity != null) {
				estudio.setProfesion(profesionEntity);
			} else {
				log.warn("ProfesionEntity not found for ID: {}", study.getProfession().getIdentification());
			}
		} catch (Exception e) {
			log.error("Error fetching PersonaEntity or ProfesionEntity: {}", e.getMessage());
		}
		
		return estudio;
	}

	private Date validateFecha(LocalDate graduationDate) {
		return graduationDate != null
				? Date.from(graduationDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
				: null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosEntity estudiosEntity) {
		if (estudiosEntity == null) {
			return null;
		}
		
		Study study = new Study();
		// Evitar recursión infinita: crear Person y Profession sin sus relaciones bidireccionales
		Person person = personaMapperMaria.fromAdapterToDomainWithoutRelations(estudiosEntity.getPersona());
		if (person == null) {
			return null; // Si no hay persona válida, no podemos crear el estudio
		}
		study.setPerson(person);
		// Usar fromAdapterToDomainWithoutRelations para romper el ciclo Profession <-> Study
		study.setProfession(profesionMapperMaria.fromAdapterToDomainWithoutRelations(estudiosEntity.getProfesion()));
		study.setGraduationDate(validateGraduationDate(estudiosEntity.getFecha()));
		study.setUniversityName(validateUniversityName(estudiosEntity.getUniver()));
		return study;
	}

	private LocalDate validateGraduationDate(Date fecha) {
		if (fecha == null) {
			return null;
		}
		try {
			return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		} catch (UnsupportedOperationException e) {
			// java.sql.Date doesn't support toInstant() in some cases
			return null;
		}
	}

	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}
}