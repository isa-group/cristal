package es.us.isa.cristal.organization.generator.distributors;

import java.util.ArrayList;
import java.util.List;

import es.us.isa.cristal.organization.generator.selectors.Selector;
import es.us.isa.cristal.organization.model.gson.Position;

public class PositionDelegatesMultipleRandomDistributor extends AbstractMultipleRandomDistributor<Position,Position> implements Distributor<Position,Position> {

	public PositionDelegatesMultipleRandomDistributor(Integer min, Integer max,	Selector<Position> selector) {
		super(min, max, selector);
	}

	@Override
	public void setRelation(Position entity, Position entity2) {
		entity2.getDelegates().add(entity);
	}

	
	@Override
	public List<Position> getExclusions(Position entity2) {
		List<Position> result = new ArrayList<Position>();
		result.addAll(entity2.getDelegates());
		result.add(entity2);
		return result;
	}
}
