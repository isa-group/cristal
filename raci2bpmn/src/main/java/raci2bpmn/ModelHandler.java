package raci2bpmn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import bpmn.TDefinitions;

public class ModelHandler {

	private JAXBElement<?> bpmnModel;
	private JAXBContext jc;

	public ModelHandler() {
		// Obtenemos un contexto para jaxb seleccionando el paquete donde estan
		// las clases generadas por xjc
		try {
			jc = JAXBContext.newInstance("bpmn");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void loadModel(InputStream input) {
		try {
			// Creamos un unmarshaller
			Unmarshaller u = jc.createUnmarshaller();
			// Leemos el XML obteniendo el elemento raiz como un JAXBElement
			bpmnModel = (JAXBElement<?>) u.unmarshal(input);
		} catch (JAXBException je) {
			je.printStackTrace();
		}
	}

	public void loadModelFile(String bpmnFile) {
		loadModelFile(new File(bpmnFile));
	}
	
	public void loadModelFile(File bpmnFile) {
		try {
			loadModel(new FileInputStream(bpmnFile));
		} catch (IOException io) {
			io.printStackTrace();
		}		
	}
	
	public TDefinitions getDefinitions() {
		return (TDefinitions) bpmnModel.getValue();
	}

	public void saveModel(String filename) {
		try {
			// Creamos un marshaller
			Marshaller m = jc.createMarshaller();

			// Guardamos el modelo en un XML
			m.marshal(bpmnModel, new FileOutputStream(filename));
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getModel() {
		String model = "";

		try {
			// Creamos un marshaller
			Marshaller m = jc.createMarshaller();
			StringWriter sw = new StringWriter();
			// Guardamos el modelo en un XML
			m.marshal(bpmnModel, sw);

			model = sw.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return model;
	}

}
