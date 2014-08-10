package es.us.isa.cristal.owl.analysers.operations;

import es.us.isa.cristal.model.TaskDuty;

import java.util.Set;

/**
 * PotentialParticipants
 *
 *  The Potential Participants operation calculates the resources that are candidate
 *  to perform a specific task duty for one activity, e.g., the individuals that can
 *  potentially become responsible for the activity indicated.
 *
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public interface PotentialParticipants extends AnalysisOperation {
    public Set<String> run(String activityName, TaskDuty duty);
}
