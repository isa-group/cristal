package es.us.isa.cristal.organization.generator;

import es.us.isa.cristal.organization.generator.distributors.PersonPositionConsecutiveDistributor;
import es.us.isa.cristal.organization.generator.distributors.PositionDelegatesMultipleRandomDistributor;
import es.us.isa.cristal.organization.generator.distributors.PositionReportsMultipleRandomDistributor;
import es.us.isa.cristal.organization.generator.distributors.PositionUnitConsecutiveDistributor;
import es.us.isa.cristal.organization.generator.distributors.RolePositionMultipleRandomDistributor;
import es.us.isa.cristal.organization.generator.functions.ConstantFunction;
import es.us.isa.cristal.organization.generator.functions.RandomFunction;
import es.us.isa.cristal.organization.generator.selectors.ConsecutiveSelector;
import es.us.isa.cristal.organization.generator.selectors.RandomSelector;
import es.us.isa.cristal.organization.model.gson.Position;
import es.us.isa.cristal.organization.model.gson.Role;

/**
 * 
 * @author Manuel Leon
 *
 */
public class ConfigurationFactory {
	
	public GeneratorConfiguration getDefaultConfiguration(int weight){
		
		GeneratorConfiguration result= new GeneratorConfiguration();
		
		result.setNumberOfPersonsFunction(new RandomFunction(weight, weight*2));
		result.setNumberOfPositionsFunction(new ConstantFunction(weight));
		result.setNumberOfUnitsFunction(new ConstantFunction(2));
		result.setNumberOfRolesFunction(new RandomFunction(weight*2, weight*3));
		
		result.setPersonPositionDistributor(new PersonPositionConsecutiveDistributor());
		result.setRolePositionDistributor(new RolePositionMultipleRandomDistributor(1,4,new ConsecutiveSelector<Role>()));
		result.setPositionUnitDistributor(new PositionUnitConsecutiveDistributor());
		result.setPositionDelegatesDistributor(new PositionDelegatesMultipleRandomDistributor(1,3,new RandomSelector<Position>()));
		result.setPositionReportsDistributor(new PositionReportsMultipleRandomDistributor(1,1,new RandomSelector<Position>()));
		
		return result;
	}
	
	
}
