package es.us.isa.cristal.model.constraints;

public class GroupResourceInDataFieldConstraint extends GroupResourceConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4228647101817558395L;
	private String field;
	
	public GroupResourceInDataFieldConstraint(String id, String field) {

		super(id);
		this.field = field;
	}
	
	public String getField() {
		
		return this.field;
	}
}
