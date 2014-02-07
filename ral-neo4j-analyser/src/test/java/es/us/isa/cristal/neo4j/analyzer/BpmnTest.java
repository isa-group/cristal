package es.us.isa.cristal.neo4j.analyzer;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.neo4j.Neo4JRalResolver;
import es.us.isa.cristal.neo4j.analyzer.operations.MandatoryActivitiesOP;
import es.us.isa.cristal.test.utils.executionengine.ExecutionEngineUtil;
import es.us.isa.cristal.test.utils.graph.GraphUtil;

public class BpmnTest{

	BPEngine engine;
	
	Neo4JRalResolver resolver;
	
	@Before
	public void loadBpEngine(){
		engine = new BPEngineMock(); 
		resolver = new Neo4JRalResolver(engine, ExecutionEngineUtil.getExecutionEngine(GraphUtil.DEVELOPMENT_UNIT_GRAPH));
		
	}
	
	@Test
	public void testMandatory(){
		Set<String> expected = new HashSet<String>();
		expected.add("Approval task");
		expected.add("Check outcome");
		MandatoryActivitiesOP ope = new MandatoryActivitiesOP(engine, resolver,"test.bpmn20.xml", null, TaskDuty.RESPONSIBLE);
		Set<String> result = ope.execute();
		System.out.println("RESULT: " + result);
		
		Assert.assertTrue(expected.equals(result));
	}
	
	@Test
	public void testMandatory2(){
		Set<String> expected = new HashSet<String>();
		expected.add("C");
		expected.add("K");
		MandatoryActivitiesOP ope = new MandatoryActivitiesOP(engine, resolver,"test2.bpmn20.xml", null, TaskDuty.RESPONSIBLE);
		Set<String> result = ope.execute();
		System.out.println("RESULT: " + result);
		
		Assert.assertTrue(expected.equals(result));
	}
	
	
	@Test
	public void testMandatory3(){
		Set<String> expected = new HashSet<String>();
		expected.add("C");
		expected.add("K");
		MandatoryActivitiesOP ope = new MandatoryActivitiesOP(engine, resolver,"test3.bpmn20.xml", null, TaskDuty.RESPONSIBLE);
		Set<String> result = ope.execute();
		System.out.println("RESULT: " + result);
		
		Assert.assertTrue(expected.equals(result));
	}
	
	@Test
	public void testMandatory4(){
		Set<String> expected = new HashSet<String>();
		expected.add("C");
		expected.add("F");
		expected.add("G");
		expected.add("H");
		expected.add("I");
		expected.add("J");
		expected.add("K");
		MandatoryActivitiesOP ope = new MandatoryActivitiesOP(engine, resolver,"test4.bpmn20.xml", null, TaskDuty.RESPONSIBLE);
		Set<String> result = ope.execute();
		System.out.println("RESULT: " + result);
		
		Assert.assertTrue(expected.equals(result));
	}
	
	@Test
	public void testMandatory5(){
		Set<String> expected = new HashSet<String>();
		expected.add("C");
		expected.add("F");
		expected.add("G");
		expected.add("H");
		expected.add("I");
		expected.add("J");
		expected.add("K");
		MandatoryActivitiesOP ope = new MandatoryActivitiesOP(engine, resolver,"test5.bpmn20.xml", null, TaskDuty.RESPONSIBLE);
		Set<String> result = ope.execute();
		System.out.println("RESULT: " + result);
		
		Assert.assertTrue(expected.equals(result));
	}
	
}
