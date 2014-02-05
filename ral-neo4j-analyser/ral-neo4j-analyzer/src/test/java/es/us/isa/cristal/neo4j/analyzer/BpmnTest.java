package es.us.isa.cristal.neo4j.analyzer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.analyzer.operations.MandatoryActivitiesOP;

public class BpmnTest {

	BPEngine engine;
	
	@Before
	public void loadBpEngine(){
		engine = new BPEngineMock(); 
	}
	
	@Test
	public void testMandatory(){
		MandatoryActivitiesOP ope = new MandatoryActivitiesOP(engine, null,null, null, TaskDuty.RESPONSIBLE);
		ope.execute();
		Assert.assertTrue(false);
	}
	
}
