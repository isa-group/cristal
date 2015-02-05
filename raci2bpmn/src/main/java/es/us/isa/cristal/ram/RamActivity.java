package es.us.isa.cristal.ram;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class RamActivity {
	
	private String activityName;
	private Map<String, List<BoundedRole>> duties;

    public RamActivity() {
        duties = new HashMap<>();
    }

    public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

    @JsonIgnore
    public List<BoundedRole> get(String duty) {
        return duties.get(duty);
    }

    @JsonIgnore
    public BoundedRole getOne(String duty) {
        BoundedRole theOne = null;
        List<BoundedRole> roles = get(duty);

        if (roles != null || !roles.isEmpty()) {
            theOne = roles.get(0);
        }

        return theOne;
    }

    public boolean has(String duty) {
        List<BoundedRole> roles = get(duty);
        return roles != null && ! roles.isEmpty();
    }

    public int numberOf(String duty) {
        List<BoundedRole> roles = get(duty);
        int number = (roles == null) ? 0 : roles.size();

        return number;
    }

    public void set(String duty, BoundedRole... boundedRoles) {
        duties.put(duty, Arrays.asList(boundedRoles));
    }

    @JsonIgnore
    public Set<String> getDutiesDefined() {
        return duties.keySet();
    }

    private Map<String, List<BoundedRole>> getDuties() {
        return duties;
    }

    private void setDuties(Map<String, List<BoundedRole>> duties) {
        this.duties = duties;
    }
}
