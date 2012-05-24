package raci;

import java.util.List;

public class ActivityEntry {
	
	String activityName;
	String responsible;
	String accountable;
	List<Participant> support;
	List<Participant> consulted;
	List<Participant> informed;
	
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	public String getAccountable() {
		return accountable;
	}
	public void setAccountable(String accountable) {
		this.accountable = accountable;
	}
	public List<Participant> getSupport() {
		return support;
	}
	public void setSupport(List<Participant> support) {
		this.support = support;
	}
	public List<Participant> getConsulted() {
		return consulted;
	}
	public void setConsulted(List<Participant> consulted) {
		this.consulted = consulted;
	}
	public List<Participant> getInformed() {
		return informed;
	}
	public void setInformed(List<Participant> informed) {
		this.informed = informed;
	}
	
	
}
