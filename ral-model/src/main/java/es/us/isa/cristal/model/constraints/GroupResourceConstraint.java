package es.us.isa.cristal.model.constraints;

import es.us.isa.cristal.model.GroupResourceType;

/**
 * Indica que se hace referencia justamente al recurso nombrado
 * 
 * @author Edelia
 *
 */
public interface GroupResourceConstraint extends Constraint {

    public void setGroupResourceType(GroupResourceType type);
    public GroupResourceType getGroupResourceType();

}
