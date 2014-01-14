package es.us.isa.cristal.organization.model.gson;

import java.util.List;

import es.us.isa.cristal.organization.model.util.CypherUtil;

public class Model implements CypherGenerator{
	private List<Person> persons;
	
	private List<Role> roles;
	
	private List<Unit> units;

	/**
	 * @return the persons
	 */
	public final List<Person> getPersons() {
		return persons;
	}

	/**
	 * @return the roles
	 */
	public final List<Role> getRoles() {
		return roles;
	}

	/**
	 * @return the units
	 */
	public final List<Unit> getUnits() {
		return units;
	}

	/**
	 * @param persons the persons to set
	 */
	public final void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	/**
	 * @param roles the roles to set
	 */
	public final void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @param units the units to set
	 */
	public final void setUnits(List<Unit> units) {
		this.units = units;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((persons == null) ? 0 : persons.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Model other = (Model) obj;
		if (persons == null) {
			if (other.persons != null)
				return false;
		} else if (!persons.equals(other.persons))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		return true;
	}

	public String getCypherCreateQuery() {
		String result = "";
		
		for(Person p: this.persons){
			result += p.getCypherCreateQuery() + " \n";
		}
		for(Role r: this.roles){
			result += r.getCypherCreateQuery() + " \n";
		}
		for(Unit u: this.units){
			result += u.getCypherCreateQuery() + " \n";
			for(Position p: u.getPositions()){
				result += p.getCypherCreateQuery() + " \n";
				result += CypherUtil.cypherCreateEdgeQuery(p.getName(), u.getName(), "[:UNIT]") + " \n";
				for(String personName: p.getOccupiedBy()){
					result += CypherUtil.cypherCreateEdgeQuery(personName, p.getName(), "[:POSITION]") + " \n";
				}
				for(String roleName: p.getRoles()){
					result += CypherUtil.cypherCreateEdgeQuery(p.getName(), roleName, "[:ROLE]") + " \n";
				}
				for(Position reporter: p.getReportedBy()){
					result += CypherUtil.cypherCreateEdgeQuery(reporter.getName(), p.getName(), "[:REPORTS]") + " \n";
				}
				for(Position del: p.getDelegates()){
					result += CypherUtil.cypherCreateEdgeQuery(p.getName(), del.getName(), "[:DELEGATES]") + " \n";
				}
				
				
			}
		}
		
		
		return result;
	}
	
	
}
