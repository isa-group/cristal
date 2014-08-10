package es.us.isa.cristal.organization.generator.distributors;

import es.us.isa.cristal.organization.model.gson.Person;
import es.us.isa.cristal.organization.model.gson.Position;

/**
 * 
 * @author Manuel Leon
 *
 */
public class PersonPositionConsecutiveDistributor extends AbstractConsecutiveDistributor<Person,Position> implements Distributor<Person,Position>{

	@Override
	public void setRelation(Person entity, Position entity2) {
		entity2.getOccupiedBy().add(entity.getName());
	}

}
