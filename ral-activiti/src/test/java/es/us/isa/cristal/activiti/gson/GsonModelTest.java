package es.us.isa.cristal.activiti.gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;




import es.us.isa.cristal.organization.model.gson.Document;
import es.us.isa.cristal.organization.model.gson.Model;
import es.us.isa.cristal.organization.model.gson.Person;
import es.us.isa.cristal.organization.model.gson.Position;
import es.us.isa.cristal.organization.model.gson.Role;
import es.us.isa.cristal.organization.model.gson.Unit;
import es.us.isa.cristal.neo4j.test.utils.graph.GraphUtil;

public class GsonModelTest {

	
	
	
	
	
	@Test
	public void cypherCreateScript(){
		Document doc = buildExpectedDocument();
		String script = doc.getCypherCreateQuery().trim();
		String expected = GraphUtil.DEVELOPMENT_UNIT_GRAPH;
		
		Assert.assertEquals(expected,script);
	}
	
	private Document buildExpectedDocument(){
		Document res = new Document();
		res.setDescription("");
		res.setModelId("TestModel");
		res.setName("TestModel");
		res.setRevision(0);
		res.setShared(Arrays.asList("mleonrivas@gmail.com"));
		res.setType("Organization");
		
		Person p1 = new Person();
		p1.setName("Peter");
		Person p2 = new Person();
		p2.setName("John");
		Person p3 = new Person();
		p3.setName("Mary");
		Person p4 = new Person();
		p4.setName("Cathy");
		Person p5 = new Person();
		p5.setName("Albert");
		
		Role r1 = new Role();
		r1.setName("Programmer");
		Role r2 = new Role();
		r2.setName("Analyst");
		Role r3 = new Role();
		r3.setName("Product Owner");
		Role r4 = new Role();
		r4.setName("Scrum Master");
		Role r5 = new Role();
		r5.setName("Designer");
		
		Unit u1 = new Unit();
		u1.setName("Development");
		
		Position pos1 = createPosition("Junior Software Developer","Programmer","Peter");
		Position pos2 = createPosition("Senior Software Developer","Programmer,Analyst","Mary,Albert");
		Position pos3 = createPosition("Team Leader","Scrum Master,Analyst,Programmer","Cathy");
		Position pos4 = createPosition("Project Director","Product Owner","John");
		List<Position> positions = new ArrayList<Position>();
		positions.add(pos1);
		positions.add(pos2);
		positions.add(pos3);
		positions.add(pos4);
		
		u1.setPositions(positions);
		
		Model model = new Model();
		model.setPersons(Arrays.asList(p1,p2,p3,p4,p5));
		model.setRoles(Arrays.asList(r1,r2,r3,r4,r5));
		model.setUnits(Arrays.asList(u1));
		
		res.setModel(model);
		
		return res;
	}
	
	
	private Position createPosition(String name, String roles, String occupiedBy){
		Position result = new Position();
		result.setName(name);
		result.setRoles(Arrays.asList(roles.split(",")));
		result.setOccupiedBy(Arrays.asList(occupiedBy.split(",")));
		result.setReportedBy(new ArrayList<Position>());
		return result;
	}
	
}
