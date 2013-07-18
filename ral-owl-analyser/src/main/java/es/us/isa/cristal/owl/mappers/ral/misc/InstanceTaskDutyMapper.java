package es.us.isa.cristal.owl.mappers.ral.misc;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.Definitions;

/**
 * User: resinas
 * Date: 07/07/13
 * Time: 12:05
 */
public class InstanceTaskDutyMapper implements TaskDutyMapper {
    @Override
    public String map(TaskDuty duty) {
        String hasDuty;
        switch(duty) {
            case RESPONSIBLE:
                hasDuty = Definitions.HASRESPONSIBLE; break;
            case ACCOUNTABLE:
                hasDuty = Definitions.HASACCOUNTABLE; break;
            case SUPPORT:
                hasDuty = Definitions.HASSUPPORT; break;
            case INFORMED:
                hasDuty = Definitions.HASINFORMED; break;
            case CONSULTED:
                hasDuty = Definitions.HASCONSULTED; break;
            default:
                hasDuty = Definitions.HASPARTICIPANT;
        }

        return hasDuty;
    }
}
