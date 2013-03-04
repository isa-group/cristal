package es.us.isa.cristal.resolver;

import es.us.isa.cristal.model.expressions.RALExpr;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 19:53
 */
public interface BPEngine {
    public String getDataValue(Object pid, String data, String property);
    public String getActivityPerformer(Object pid, String activityName);
    RALExpr getResourceExpression(String activityName);
}
