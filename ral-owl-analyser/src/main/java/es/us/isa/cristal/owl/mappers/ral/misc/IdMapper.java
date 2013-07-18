package es.us.isa.cristal.owl.mappers.ral.misc;

import es.us.isa.cristal.owl.OntologyNamespaces;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:55
 */
public class IdMapper {

    private String personPrefix;
    private String groupPrefix;
    private String activityPrefix;

    public IdMapper(OntologyNamespaces ontologyNamespaces) {
        this.personPrefix = ontologyNamespaces.getPerson().getPrefix();
        this.groupPrefix = ontologyNamespaces.getGroup().getPrefix();
        this.activityPrefix = ontologyNamespaces.getActivity().getPrefix();
    }

    public String mapPerson(String person) {
        return personPrefix + ":" + person;
    }

    public String mapGroup(String group) {
        return groupPrefix + ":" + group;
    }

    public String mapActivity(String activity) {
        return activityPrefix + ":" + activity;
    }
}
