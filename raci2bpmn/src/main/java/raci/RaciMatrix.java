package raci;

import java.util.List;

public class RaciMatrix {

	private String bp;
	private List<RaciActivity> activities;

	public String getBp() {
		return bp;
	}

	public void setBp(String bp) {
		this.bp = bp;
	}

	public List<RaciActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<RaciActivity> activities) {
		this.activities = activities;
	}

	public RaciActivity getActivityByName(String name) {
		RaciActivity result = null;
		for (RaciActivity a : activities) {
			if (a.getActivityName().equals(name)) {
				result = a;
				break;
			}
		}

		return result;
	}

}
