package raci2bpmn;


public class EntryPoint {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ModelHandler handler = new ModelHandler();
		handler.loadModel("ejemplo.bpmn");
		handler.handleProcess();
		handler.saveModel("result.bpmn");

	}


}
