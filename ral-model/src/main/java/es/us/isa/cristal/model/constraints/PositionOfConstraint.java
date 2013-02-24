package es.us.isa.cristal.model.constraints;

public class PositionOfConstraint extends PositionConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8448240464859654404L;
	private PersonConstraint personConstraint;
	
	public PositionOfConstraint(PersonConstraint personConstraint) {

		super("");
		
		this.personConstraint = personConstraint;
	}
	
	public PersonConstraint getPersonConstraint() {
		
		return this.personConstraint;
	}
}
