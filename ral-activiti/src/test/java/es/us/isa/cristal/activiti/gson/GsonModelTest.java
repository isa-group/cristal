package es.us.isa.cristal.activiti.gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import es.us.isa.cristal.activiti.model.gson.Document;
import es.us.isa.cristal.activiti.model.gson.Model;
import es.us.isa.cristal.activiti.model.gson.Person;
import es.us.isa.cristal.activiti.model.gson.Position;
import es.us.isa.cristal.activiti.model.gson.Role;
import es.us.isa.cristal.activiti.model.gson.Unit;
import es.us.isa.cristal.activiti.util.IOUtil;

public class GsonModelTest {

	
	@Test
	public void getURLContentTest() throws IOException{
		
		String expectedResult ="{\"shared\":[\"mleonrivas@gmail.com\"],\"modelId\":\"TestModel\",\"revision\":0,\"model\":{\"roles\":[{\"name\":\"Programmer\"},{\"name\":\"Analyst\"},{\"name\":\"Product Owner\"},{\"name\":\"Scrum Master\"},{\"name\":\"Designer\"}],\"persons\":[{\"name\":\"Peter\"},{\"name\":\"John\"},{\"name\":\"Mary\"},{\"name\":\"Cathy\"},{\"name\":\"Albert\"}],\"units\":[{\"positions\":[{\"reportedBy\":[],\"name\":\"Junior Software Developer\",\"roles\":[\"Programmer\"],\"occupiedBy\":[\"Peter\"]},{\"reportedBy\":[],\"name\":\"Senior Software Developer\",\"roles\":[\"Programmer\",\"Analyst\"],\"occupiedBy\":[\"Mary\",\"Albert\"]},{\"reportedBy\":[],\"name\":\"Team Leader\",\"roles\":[\"Scrum Master\",\"Analyst\",\"Programmer\"],\"occupiedBy\":[\"Cathy\"]},{\"reportedBy\":[],\"name\":\"Project Director\",\"roles\":[\"Product Owner\"],\"occupiedBy\":[\"John\"]}],\"name\":\"Development\"}]},\"description\":\"\",\"name\":\"TestModel\",\"type\":\"Organization\",\"extensions\":{}}";
		String result = IOUtil.getURLContent("https://dl.dropboxusercontent.com/s/scvrx6cmuh3bce8/testOrganizationModel.json?dl=1");
		Assert.assertEquals(expectedResult,result);
	}
	
	@Test
	public void importModelTest() throws IOException{
		String organization = IOUtil.getURLContent("https://dl.dropboxusercontent.com/s/scvrx6cmuh3bce8/testOrganizationModel.json?dl=1");
		Gson gson = new Gson();
		Document doc = gson.fromJson(organization, Document.class);
		Document expectedDoc = buildExpectedDocument();
		Assert.assertTrue(expectedDoc.equals(doc));
	}
	
	@Test
	public void cypherCreateScript(){
		Document doc = buildExpectedDocument();
		String script = doc.getCypherCreateQuery().trim();
		String expected = "CREATE peter = { name : 'Peter' } \n" + 
		"CREATE john = { name : 'John' } \n" + 
		"CREATE mary = { name : 'Mary' } \n" +
		"CREATE cathy = { name : 'Cathy' } \n" +
		"CREATE albert = { name : 'Albert' } \n" +
		"CREATE programmer = {role : 'Programmer'} \n" +
		"CREATE analyst = {role : 'Analyst'} \n" +
		"CREATE product_owner = {role : 'Product Owner'} \n" +
		"CREATE scrum_master = {role : 'Scrum Master'} \n" +
		"CREATE designer = {role : 'Designer'} \n" +
		"CREATE development = {unit : 'Development'} \n" +
		"CREATE junior_software_developer = {position : 'Junior Software Developer' } \n" +
		"CREATE junior_software_developer-[:UNIT]->development \n" +
		"CREATE peter-[:POSITION]->junior_software_developer \n" +
		"CREATE junior_software_developer-[:ROLE]->programmer \n" +
		"CREATE senior_software_developer = {position : 'Senior Software Developer' } \n" +
		"CREATE senior_software_developer-[:UNIT]->development \n" +
		"CREATE mary-[:POSITION]->senior_software_developer \n" +
		"CREATE albert-[:POSITION]->senior_software_developer \n" +
		"CREATE senior_software_developer-[:ROLE]->programmer \n" +
		"CREATE senior_software_developer-[:ROLE]->analyst \n" +
		"CREATE team_leader = {position : 'Team Leader' } \n" +
		"CREATE team_leader-[:UNIT]->development \n" +
		"CREATE cathy-[:POSITION]->team_leader \n" +
		"CREATE team_leader-[:ROLE]->scrum_master \n" +
		"CREATE team_leader-[:ROLE]->analyst \n" +
		"CREATE team_leader-[:ROLE]->programmer \n" +
		"CREATE project_director = {position : 'Project Director' } \n" +
		"CREATE project_director-[:UNIT]->development \n" +
		"CREATE john-[:POSITION]->project_director \n" +
		"CREATE project_director-[:ROLE]->product_owner";
		System.out.println(script);
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
