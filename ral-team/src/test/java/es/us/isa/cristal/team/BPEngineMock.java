package es.us.isa.cristal.team;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

/**
 * BPEngineMock
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class BPEngineMock implements BPEngine {
    @Override
    public String getDataValue(Object pid, String data, String property) {
        return "Anna";
    }

    @Override
    public String getActivityPerformer(Object pid, String activityName) {
        return "Charles";
    }

    @Override
    public RALExpr getResourceExpression(String activityName) {
        return RALParser.parse("HAS ROLE Account Administrator");
    }

}
