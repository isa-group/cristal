package raci;

import java.util.List;

public class RaciMatrix {
	
	private String bp;
	private List<ActivityEntry> activities;
	
	public String getBp() {
		return bp;
	}
	public void setBp(String bp) {
		this.bp = bp;
	}
	public List<ActivityEntry> getActivities() {
		return activities;
	}
	public void setActivities(List<ActivityEntry> activities) {
		this.activities = activities;
	}

}
