package es.us.isa.cristal.organization.model.gson;

import java.util.ArrayList;
import java.util.List;

import es.us.isa.cristal.organization.model.util.CypherUtil;

public class Unit implements CypherGenerator{
	private String name;
	private List<Position> positions;
	
	
	public Unit(){
		positions = new ArrayList<Position>();
	}
	
	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}
	/**
	 * @return the positions
	 */
	public final List<Position> getPositions() {
		return positions;
	}
	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}
	/**
	 * @param positions the positions to set
	 */
	public final void setPositions(List<Position> positions) {
		this.positions = positions;
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
				+ ((positions == null) ? 0 : positions.hashCode());
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
		Unit other = (Unit) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (positions == null) {
			if (other.positions != null)
				return false;
		} else if (!positions.equals(other.positions))
			return false;
		return true;
	}
	public String getCypherCreateQuery() {
		
		String query =  "CREATE "+ CypherUtil.getId(name) +" = {unit : '" + name + "'} ";
		for(Position p: this.positions){
			query+="\n" + p.getCypherCreateQuery();
		}
		
		return query;
	}
	
	
	
	
}
