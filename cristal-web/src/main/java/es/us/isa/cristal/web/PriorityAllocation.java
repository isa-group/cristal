package es.us.isa.cristal.web;

import com.google.common.base.Splitter;

import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 07/04/13
 * Time: 17:22
 */
public class PriorityAllocation {
    private String preference;
    private String potentialPerformers;

    public PriorityAllocation() {
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getPotentialPerformers() {
        return potentialPerformers;
    }

    public void setPotentialPerformers(String potentialPerformers) {
        this.potentialPerformers = potentialPerformers;
    }

    public Set<String> listPotentialPerformers() {
        Set<String> potential = new HashSet<String>();
        for (String person : Splitter.on(",").trimResults().split(potentialPerformers)) {
            potential.add(person);
        }
        return potential;
    }
}
