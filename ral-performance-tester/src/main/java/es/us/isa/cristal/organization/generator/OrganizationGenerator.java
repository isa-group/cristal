package es.us.isa.cristal.organization.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.us.isa.cristal.organization.generator.distributors.PositionDelegatesMultipleRandomDistributor;
import es.us.isa.cristal.organization.generator.distributors.PositionReportsMultipleRandomDistributor;
import es.us.isa.cristal.organization.model.gson.*;
import es.us.isa.cristal.organization.model.gson.OrganizationalModel;

/**
 * 
 * @author Manuel Leon
 *
 */
public class OrganizationGenerator {
	
	GeneratorConfiguration config;
	
	
	
	public OrganizationGenerator(GeneratorConfiguration config) {
		super();
		this.config = config;
	}



	public OrganizationalModel generate(GenerationMode... generationModes){
		OrganizationalModel model = new OrganizationalModel();
        List<GenerationMode> modes = Arrays.asList(generationModes);

        //generate people
		Integer numberOfPersons = config.getNumberOfPersonsFunction().getResult();
		List<Person> persons = new ArrayList<Person>();
		for(int i=0; i<numberOfPersons;i++){
			persons.add(generatePerson(i));
		}
		model.setPersons(persons);
		
		//generateRoles
		Integer numberOfRoles = config.getNumberOfRolesFunction().getResult();
		List<Role> roles = new ArrayList<Role>();
		for(int i=0; i<numberOfRoles;i++){
			roles.add(generateRole(i));
		}
		model.setRoles(roles);
		
		//generatePositions
		Integer numberOfPositions = config.getNumberOfPositionsFunction().getResult();
		List<Position> positions = new ArrayList<Position>();
		for(int i=0; i<numberOfPositions;i++){
			positions.add(generatePosition(i));
		}		
		
		//generateUnits
		Integer numberOfUnits = config.getNumberOfUnitsFunction().getResult();
		List<Unit> units = new ArrayList<Unit>();
		for(int i=0; i<numberOfUnits;i++){
			units.add(generateUnit(i));
		}
		model.setUnits(units);
		
		config.getPersonPositionDistributor().distribute(persons, positions);
		
		config.getRolePositionDistributor().distribute(roles, positions);
		
		config.getPositionUnitDistributor().distribute(positions, units);

        if (! modes.contains(GenerationMode.DISABLE_REPORTSTO)) {
            ((PositionReportsMultipleRandomDistributor) config.getPositionReportsDistributor()).setPositions(positions);
		    config.getPositionReportsDistributor().distribute(positions, positions);
        }

        if (! modes.contains(GenerationMode.DISABLE_DELEGATESTO)) {
            ((PositionDelegatesMultipleRandomDistributor) config.getPositionDelegatesDistributor()).setPositions(positions);
		    config.getPositionDelegatesDistributor().distribute(positions, positions);
        }
				
		return model;
		
	}



	public GeneratorConfiguration getConfig() {
		return config;
	}



	public void setConfig(GeneratorConfiguration config) {
		this.config = config;
	}
	
	
	private Person generatePerson(int i){
		Person p = new Person();
		p.setName("person" + i);
		return p;
	}
	
	private Role generateRole(int i){
		Role r = new Role();
		r.setName("role" + i);
		return r;
	}
		
	private Position generatePosition(int i){
		Position p = new Position();
		p.setName("position"+i);
		return p;
	}
	
	private Unit generateUnit (int i){
		Unit u = new Unit();
		u.setName("unit"+i);
		return u;
	}
	
}
