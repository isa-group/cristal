package raci;

public class BoundedRole {

	private String role;
	private String bindingExpression;
	
	public BoundedRole() {}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getBindingExpression() {
		return bindingExpression;
	}

	public void setBindingExpression(String bindingExpression) {
		this.bindingExpression = bindingExpression;
	}
	
	public String getAssignmentExpression() {
		String result = "HAS ROLE "+role;
		
		if (bindingExpression != null && ! bindingExpression.isEmpty())
			result += " AND " + bindingExpression;
		
		return  result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((bindingExpression == null) ? 0 : bindingExpression
						.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoundedRole other = (BoundedRole) obj;
		if (bindingExpression == null) {
			if (other.bindingExpression != null)
				return false;
		} else if (!bindingExpression.equals(other.bindingExpression))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}
	
	
}
