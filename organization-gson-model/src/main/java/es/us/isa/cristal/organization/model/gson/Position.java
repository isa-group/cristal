package es.us.isa.cristal.organization.model.gson;

import java.util.ArrayList;
import java.util.List;

import es.us.isa.cristal.organization.model.util.CypherUtil;

public class Position implements CypherGenerator{
	
	private List<Position> delegates;
	private List<Position> reportedBy;
	private String name;
	private List<String> roles;
	private List<String> occupiedBy;
	
	
	public Position(){
		delegates = new ArrayList<Position>();
		reportedBy = new ArrayList<Position>();
		roles = new ArrayList<String>();
		occupiedBy = new ArrayList<String>();
		
	}
	
	/**
	 * @return the reportedBy
	 */
	public final List<Position> getReportedBy() {
		return reportedBy;
	}
	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}
	/**
	 * @return the roles
	 */
	public final List<String> getRoles() {
		return roles;
	}
	/**
	 * @return the occupiedBy
	 */
	public final List<String> getOccupiedBy() {
		return occupiedBy;
	}
	/**
	 * @param reportedBy the reportedBy to set
	 */
	public final void setReportedBy(List<Position> reportedBy) {
		this.reportedBy = reportedBy;
	}
	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}
	/**
	 * @param roles the roles to set
	 */
	public final void setRoles(List<String> roles) {
		this.roles = roles;
	}
	/**
	 * @param occupiedBy the occupiedBy to set
	 */
	public final void setOccupiedBy(List<String> occupiedBy) {
		this.occupiedBy = occupiedBy;
	}
	
	public List<Position> getDelegates() {
		return delegates;
	}
	public void setDelegates(List<Position> delegates) {
		this.delegates = delegates;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((occupiedBy == null) ? 0 : occupiedBy.hashCode());
		result = prime * result
				+ ((reportedBy == null) ? 0 : reportedBy.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
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
		Position other = (Position) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (occupiedBy == null) {
			if (other.occupiedBy != null)
				return false;
		} else if (!occupiedBy.equals(other.occupiedBy))
			return false;
		if (reportedBy == null) {
			if (other.reportedBy != null)
				return false;
		} else if (!reportedBy.equals(other.reportedBy))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		return true;
	}
	public String getCypherCreateQuery() {
		return "CREATE "+ CypherUtil.getId(name) +" = {position : '" + name + "' }";
	}
	
	
	
	
}
