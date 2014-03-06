package es.us.isa.cristal.owl.analysers;

import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;

import java.util.HashSet;
import java.util.Set;

/**
 * StardogHelper
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
public class StardogHelper {
    public static Set<String> getElements(TupleQueryResult tuples, String bindingName) {
        Set<String> result = new HashSet<String>();
        try {
            while (tuples.hasNext()) {
                result.add(tuples.next().getValue(bindingName).stringValue());
            }
        } catch (QueryEvaluationException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
