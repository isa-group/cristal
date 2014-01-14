package es.us.isa.cristal.organization.generator.distributors;

import java.util.ArrayList;
import java.util.List;

import es.us.isa.cristal.organization.generator.selectors.Selector;
import es.us.isa.cristal.organization.model.gson.Position;
import es.us.isa.cristal.organization.model.gson.Role;

public class RolePositionMultipleRandomDistributor extends AbstractMultipleRandomDistributor<Role,Position> implements Distributor<Role,Position>{

	public RolePositionMultipleRandomDistributor(Integer min, Integer max, Selector<Role> selector) {
		super(min, max, selector);
		
	}

	@Override
	public void setRelation(Role entity, Position entity2) {
		entity2.getRoles().add(entity.getName());
		
	}

	@Override
	public List<Role> getExclusions(Position entity2) {
		List<Role> result = new ArrayList<Role>();
		for(String name: entity2.getRoles()){
			Role r = new Role();
			r.setName(name);
			result.add(r);
		}
		return result;
	}

}
