package es.us.isa.cristal.ram;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import raci.RaciMatrix;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class RAM {

	private String bp;
	private List<RamActivity> activities;

    public static RAM load(Reader matrixReader) {
        RAM matrix;
        ObjectMapper mapper = new ObjectMapper();

        try {
            matrix = mapper.readValue(matrixReader, RAM.class);
        } catch (IOException e) {
            throw new RuntimeException("Error reading RAM matrix", e);
        }

        return matrix;
    }

    public String getBp() {
		return bp;
	}

	public void setBp(String bp) {
		this.bp = bp;
	}

	public List<RamActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<RamActivity> activities) {
		this.activities = activities;
	}

	public RamActivity getActivityByName(String name) {
		RamActivity result = null;
		for (RamActivity a : activities) {
			if (a.getActivityName().equals(name)) {
				result = a;
				break;
			}
		}

		return result;
	}

}
