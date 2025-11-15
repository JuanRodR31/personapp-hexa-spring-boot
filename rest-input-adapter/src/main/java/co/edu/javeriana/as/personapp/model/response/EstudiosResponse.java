package co.edu.javeriana.as.personapp.model.response;

import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;

public class EstudiosResponse extends EstudiosRequest {
	
	private String status;
	
	public EstudiosResponse(String professionId, String personDni, String graduationDate, String universityName, String database, String status) {
		super(professionId, personDni, graduationDate, universityName, database);
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
