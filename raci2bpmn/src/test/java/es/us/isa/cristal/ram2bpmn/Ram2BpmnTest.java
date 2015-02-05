package es.us.isa.cristal.ram2bpmn;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import es.us.isa.cristal.ram.RAM;
import es.us.isa.cristal.ram2bpmn.templates.DefaultProcessTemplates;
import es.us.isa.cristal.ram2bpmn.templates.rasci.RasciTemplate;
import org.junit.Test;


import java.io.*;

/**
 * Ram2BpmnTest
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class Ram2BpmnTest {

    @Test
    public void ram2Bpmn() throws Exception {
        Bpmn20ModelHandler bpmnHandler = new Bpmn20ModelHandlerImpl();

        RAM matrix = RAM.load(new InputStreamReader(this.getClass().getResourceAsStream("/es/us/isa/cristal/ram/completeRamMatrix.json")));

        bpmnHandler.load(this.getClass().getResourceAsStream("initialBP.bpmn"));

        Ram2Bpmn transformer = new Ram2Bpmn();
        transformer.transformProcess(bpmnHandler, matrix, new DefaultProcessTemplates(new RasciTemplate()));

        bpmnHandler.save(System.out);
    }
}
