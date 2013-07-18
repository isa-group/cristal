package es.us.isa.cristal.model.constraints;

import es.us.isa.cristal.model.GroupResourceType;

public class ThisGroupResourceConstraint extends IdConstraint implements GroupResourceConstraint {

    private GroupResourceType type;

	public ThisGroupResourceConstraint(String id) {
		super(id);
	}

    @Override
    public void setGroupResourceType(GroupResourceType type) {
        this.type = type;
    }

    @Override
    public GroupResourceType getGroupResourceType() {
        return type;
    }
}
