package raci;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MatrixHandler {
	
	public void loadMatrix (String matrixFile)
	{
		ObjectMapper mapper = new ObjectMapper();
		try {
			RaciMatrix matrix = mapper.readValue(new File(matrixFile), RaciMatrix.class);
			
			System.out.println("BP: "+matrix.getBp());
			for (ActivityEntry a : matrix.getActivities()) {
				System.out.println("Responsible: "+a.getResponsible());
				System.out.println("Accountable: "+a.getAccountable());
				for (Participant s : a.getSupport()) {
					System.out.println("Support: "+ s.getParticipant());
				}
				for (Participant c : a.getConsulted()) {
					System.out.println("Consulted: "+ c.getParticipant());
				}
				for (Participant i : a.getInformed()) {
					System.out.println("Informed: "+ i.getParticipant());
				}
			}			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
