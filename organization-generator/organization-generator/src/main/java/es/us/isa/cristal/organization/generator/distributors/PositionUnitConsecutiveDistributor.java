package es.us.isa.cristal.organization.generator.distributors;


import es.us.isa.cristal.organization.model.gson.Position;
import es.us.isa.cristal.organization.model.gson.Unit;

public class PositionUnitConsecutiveDistributor extends AbstractConsecutiveDistributor<Position,Unit> implements Distributor<Position,Unit>{

	
	@Override
	public void setRelation(Position entity, Unit entity2) {
		entity2.getPositions().add(entity);
		
	}
}


