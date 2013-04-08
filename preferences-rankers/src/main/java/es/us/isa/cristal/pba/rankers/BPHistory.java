package es.us.isa.cristal.pba.rankers;

/**
 * User: resinas
 * Date: 07/04/13
 * Time: 11:37
 */
public interface BPHistory {
    public long countActivityByPerson(String person, String activity, String processId);
}
