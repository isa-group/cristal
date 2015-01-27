package raci2bpmn;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import raci.MatrixHandler;
import raci.RaciMatrix;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class EntryPoint {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
        Bpmn20ModelHandler bpmnHandler = new Bpmn20ModelHandlerImpl();
        MatrixHandler matrixHandler = new MatrixHandler();

        RaciMatrix matrix = matrixHandler.loadMatrixFile("completeRaciMatrix.json");
        bpmnHandler.load(new FileInputStream("initialBP.bpmn"));

        Raci2Bpmn transformer = new Raci2Bpmn();
        transformer.transformProcess(bpmnHandler, matrix);

        bpmnHandler.save(new FileOutputStream("resultBP.bpmn"));

    }

}
