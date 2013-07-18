package es.us.isa.cristal.resolver;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.parser.RALParser;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 20:22
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
