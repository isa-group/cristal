package es.us.isa.cristal.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import raci.MatrixHandler;
import raci.RaciMatrix;
import raci2bpmn.ModelHandler;
import raci2bpmn.Raci2Bpmn;

@Path("/api/raci2bpmn")
public class Raci2BpmnResource {

	private static final Logger log = Logger.getLogger(Raci2BpmnResource.class
			.getName());

	@POST 
	public String raci2Bpmn(@FormParam("raci") String raci, @FormParam("bpmn") String bpmn){
		ModelHandler handler = new ModelHandler();
		MatrixHandler matrixHandler = new MatrixHandler();

		log.info("Raci model: "+raci);
		log.info("bpmn model: "+bpmn);
		RaciMatrix matrix = matrixHandler.loadMatrix(raci);
		
		InputStream is = new ByteArrayInputStream(bpmn.getBytes());
		handler.loadModel(is);
		Raci2Bpmn transformer = new Raci2Bpmn();
		transformer.transformProcess(handler, matrix);
		
		return handler.getModel();
	}
	
}
