package raci;

import java.util.List;

public class RaciActivity {
	
	private String activityName;
	private BoundedRole responsible;
	private BoundedRole accountable;
	private List<BoundedRole> support;
	private List<BoundedRole> consulted;
	private List<BoundedRole> informed;
	
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public BoundedRole getResponsible() {
		return responsible;
	}
	public void setResponsible(BoundedRole responsible) {
		this.responsible = responsible;
	}
	public BoundedRole getAccountable() {
		return accountable;
	}
	public void setAccountable(BoundedRole accountable) {
		this.accountable = accountable;
	}
	public List<BoundedRole> getSupport() {
		return support;
	}
	public void setSupport(List<BoundedRole> support) {
		this.support = support;
	}
	public List<BoundedRole> getConsulted() {
		return consulted;
	}
	public void setConsulted(List<BoundedRole> consulted) {
		this.consulted = consulted;
	}
	public List<BoundedRole> getInformed() {
		return informed;
	}
	public void setInformed(List<BoundedRole> informed) {
		this.informed = informed;
	}
	
	public boolean shouldIncludeAccountable() {
		boolean result = true;
		if (getResponsible().equals(getAccountable()))
			result = false;
		
		return result;
	}
	
	public boolean shouldIncludeSupport() {
		return getSupport() != null & getSupport().size() > 0;
	}
	
	public boolean shouldIncludeConsult() {
		return getConsulted() != null & getConsulted().size() > 0;
	}
	
	public boolean shouldIncludeInformed() {
		return getInformed() != null & getInformed().size() > 0;
	}
}
