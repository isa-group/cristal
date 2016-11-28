package es.us.isa.cristal.ram;

/**
 * BoundedRole
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class BoundedRole {
    private Activity activity;
    private Responsibility responsibility;
    private Role role;
    private String organisationalContext = null;
    private String additionalRestrictions = null;

    private BoundedRole() {

    }

    public BoundedRole(Activity activity, Responsibility responsibility, Role role) {
        this.activity = activity;
        this.responsibility = responsibility;
        this.role = role;
    }

    public Activity getActivity() {
        return activity;
    }

    public Responsibility getResponsibility() {
        return responsibility;
    }

    public Role getRole() {
        return role;
    }

    public String getOrganisationalContext() {
        return organisationalContext;
    }

    public void setOrganisationalContext(String organisationalContext) {
        this.organisationalContext = organisationalContext;
    }

    public boolean hasOrganisationalContext() {
        return organisationalContext != null && ! organisationalContext.isEmpty();
    }

    public String getAdditionalRestrictions() {
        return additionalRestrictions;
    }

    public void setAdditionalRestrictions(String additionalRestrictions) {
        this.additionalRestrictions = additionalRestrictions;
    }

    public boolean hasAdditionalRestrictions() {
        return additionalRestrictions != null && ! additionalRestrictions.isEmpty();
    }
}
