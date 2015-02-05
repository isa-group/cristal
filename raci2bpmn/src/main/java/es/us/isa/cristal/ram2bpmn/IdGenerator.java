package es.us.isa.cristal.ram2bpmn;

public class IdGenerator {

	private static int idCounter = 0;
	
	public static String createId() {
		return "id-" + idCounter++;
	}
}
