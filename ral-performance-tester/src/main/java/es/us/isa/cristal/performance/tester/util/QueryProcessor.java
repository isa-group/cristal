package es.us.isa.cristal.performance.tester.util;

import es.us.isa.cristal.organization.generator.selectors.RandomSelector;
import es.us.isa.cristal.organization.generator.selectors.Selector;
import es.us.isa.cristal.organization.model.gson.*;
import es.us.isa.cristal.organization.model.gson.OrganizationalModel;

/**
 * 
 * @author Manuel Leon
 *
 */
public class QueryProcessor {

	Selector selector;
	
	public QueryProcessor(){
		selector = new RandomSelector();
		
	}
	
	public String processQuery(String query, OrganizationalModel model){
		//process persons
		query = query.replace("#FirstPerson#", model.getPersons().get(0).getName());
		query = query.replace("#LastPerson#", model.getPersons().get(model.getPersons().size()-1).getName());
		Person p = (Person) selector.getIndividual(model.getPersons());
		query = query.replace("#RandomPerson#", p.getName());
		
		//process roles
		query = query.replace("#FirstRole#", model.getRoles().get(0).getName());
		query = query.replace("#LastRole#", model.getRoles().get(model.getRoles().size()-1).getName());
		Role r = (Role) selector.getIndividual(model.getRoles());
		query = query.replace("#RandomRole#", r.getName());
		
		//process units
		query = query.replace("#FirstUnit#", model.getUnits().get(0).getName());
		query = query.replace("#LastUnit#", model.getUnits().get(model.getUnits().size()-1).getName());
		Unit u = (Unit) selector.getIndividual(model.getUnits());
		query = query.replace("#RandomUnit#", u.getName());
		
		//process positions
		query = query.replace("#FirstPosition#", model.getUnits().get(0).getPositions().get(0).getName());
		query = query.replace("#LastPosition#", model.getUnits().get(model.getUnits().size()-1).getName());
		Unit u2 = (Unit) selector.getIndividual(model.getUnits());
		Position pos = (Position) selector.getIndividual(u2.getPositions());
		query = query.replace("#RandomPosition#", pos.getName());
		
		return query;
	}
	
	
	
	
}
