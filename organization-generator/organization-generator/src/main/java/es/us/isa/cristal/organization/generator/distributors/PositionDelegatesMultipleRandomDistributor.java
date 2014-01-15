package es.us.isa.cristal.organization.generator.distributors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.us.isa.cristal.organization.generator.selectors.Selector;
import es.us.isa.cristal.organization.model.gson.Position;

public class PositionDelegatesMultipleRandomDistributor extends AbstractMultipleRandomDistributor<Position,Position> implements Distributor<Position,Position> {

	private List<Position> positions;
	
	public PositionDelegatesMultipleRandomDistributor(Integer min, Integer max,	Selector<Position> selector) {
		super(min, max, selector);
	}
	
	
	
	public final List<Position> getPositions() {
		return positions;
	}



	public final void setPositions(List<Position> positions) {
		this.positions = positions;
	}



	@Override
	public void setRelation(Position entity, Position entity2) {
		entity2.getDelegates().add(entity);
	}

	
	@Override
	public List<Position> getExclusions(Position entity2) {
		List<Position> result = new ArrayList<Position>();
		result.addAll(entity2.getDelegates());
		
		Set<Position> excluded = new HashSet<Position>();
		excluded.add(entity2);
		result.addAll(getParents(entity2, excluded));
		
		return result;
		
	}
	
	
	private Set<Position> getParents(Position p, Set<Position> excluded){
		for(Position p2: positions){
			//check if p2 delegates in p, so p2 and their parents must be excluded
			if(p2.getDelegates().contains(p)){
				excluded.add(p2);
				excluded.addAll(getParents(p2,excluded));
			}	
		}
		return excluded;
	}
	
	@Override
	public void distribute(List<Position> entities, List<Position> entities2){
		if(positions==null || positions.isEmpty()){
			throw new RuntimeException("Positions are not setted, please check you are setting the positions in the distributor before executing distribute method");
		}
		super.distribute(entities, entities2);
	}
	
}
