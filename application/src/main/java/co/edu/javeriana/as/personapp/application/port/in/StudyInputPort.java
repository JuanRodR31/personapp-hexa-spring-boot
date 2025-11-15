package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

public interface StudyInputPort {
	
	void setPersistence(StudyOutputPort studyPersistence);
	
	Study create(Study study);
	
	Study edit(Integer professionId, Integer personId, Study study) throws NoExistException;
	
	Boolean drop(Integer professionId, Integer personId) throws NoExistException;
	
	List<Study> findAll();
	
	Study findOne(Integer professionId, Integer personId) throws NoExistException;
	
	Integer count();
}
