package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;

import co.edu.javeriana.as.personapp.domain.Profession;

public interface ProfessionOutputPort {
	
	Profession save(Profession profession);
	
	Boolean delete(Integer identification);
	
	List<Profession> find();
	
	Profession findById(Integer identification);
}
