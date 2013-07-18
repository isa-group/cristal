package es.us.isa.cristal.model.constraints;

import es.us.isa.cristal.model.GroupResourceType;

public class GroupResourceInDataFieldConstraint extends DataConstraint implements GroupResourceConstraint {

    private GroupResourceType type;

    public GroupResourceInDataFieldConstraint(String id, String field) {
        super(id, field);
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
