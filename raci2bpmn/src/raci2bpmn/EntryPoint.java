package raci2bpmn;

import raci.MatrixHandler;


public class EntryPoint {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ModelHandler handler = new ModelHandler();
		MatrixHandler matrixHandler = new MatrixHandler();
		matrixHandler.loadMatrix("raciMatrix.json");
		handler.loadModel("ejemplo.bpmn");
		handler.handleProcess();
		handler.saveModel("result.bpmn");

	}

}
