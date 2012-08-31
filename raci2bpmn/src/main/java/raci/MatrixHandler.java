package raci;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MatrixHandler {
	
	public RaciMatrix loadMatrix (String matrixFile)
	{
		RaciMatrix matrix = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			matrix = mapper.readValue(new File(matrixFile), RaciMatrix.class);
			
			System.out.println("BP: "+matrix.getBp());
			for (RaciActivity a : matrix.getActivities()) {
				System.out.println("Responsible: "+a.getResponsible());
				System.out.println("Accountable: "+a.getAccountable());
				for (BoundedRole s : a.getSupport()) {
					System.out.println("Support: "+ s.getRole());
				}
				for (BoundedRole c : a.getConsulted()) {
					System.out.println("Consulted: "+ c.getRole());
				}
				for (BoundedRole i : a.getInformed()) {
					System.out.println("Informed: "+ i.getRole());
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
		
		return matrix;
		
	}

}
