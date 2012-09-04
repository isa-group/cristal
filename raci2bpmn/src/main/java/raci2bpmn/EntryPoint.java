package raci2bpmn;

import raci.MatrixHandler;
import raci.RaciMatrix;


public class EntryPoint {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ModelHandler handler = new ModelHandler();
		MatrixHandler matrixHandler = new MatrixHandler();
		RaciMatrix matrix = matrixHandler.loadMatrixFile("completeRaciMatrix.json");
		handler.loadModelFile("initialBP.bpmn");
		Raci2Bpmn transformer = new Raci2Bpmn();
		transformer.transformProcess(handler,matrix);
		handler.saveModel("resultBP.bpmn");

	}

}
