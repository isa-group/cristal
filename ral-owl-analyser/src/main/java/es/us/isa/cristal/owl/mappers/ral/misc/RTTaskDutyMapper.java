package es.us.isa.cristal.owl.mappers.ral.misc;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.Definitions;

/**
 * User: resinas
 * Date: 07/07/13
 * Time: 12:05
 */
public class RTTaskDutyMapper implements TaskDutyMapper {
    @Override
    public String map(TaskDuty duty) {
        String hasDuty;
        switch(duty) {
            case RESPONSIBLE:
                hasDuty = Definitions.POTRESPONSIBLE; break;
            case ACCOUNTABLE:
                hasDuty = Definitions.POTACCOUNTABLE; break;
            case SUPPORT:
                hasDuty = Definitions.POTSUPPORT; break;
            case INFORMED:
                hasDuty = Definitions.POTINFORMED; break;
            case CONSULTED:
                hasDuty = Definitions.POTCONSULTED; break;
            default:
                hasDuty = Definitions.POTPARTICIPANT;
        }

        return hasDuty;
    }
}
