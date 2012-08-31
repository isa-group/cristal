package raci2bpmn;

public class IdGenerator {

	private static int idCounter = 0;
	
	public static String createId() {
		return "id-" + idCounter++;
	}
}
