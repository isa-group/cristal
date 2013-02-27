package es.us.isa.cristal.resolver;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 20:22
 */
public class BPEngineMock implements BPEngine {
    @Override
    public String getDataValue(long pid, String data, String property) {
        return "Anna";
    }

    @Override
    public String getActivityPerformer(long pid, String activityName) {
        return "Charles";
    }
}
