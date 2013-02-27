package es.us.isa.cristal.resolver;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 19:53
 */
public interface BPEngine {
    public String getDataValue(long pid, String data, String property);
    public String getActivityPerformer(long pid, String activityName);
}
