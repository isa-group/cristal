package es.us.isa.cristal.owl.mappers.ral.designtime;

import es.us.isa.cristal.model.TaskDuty;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.misc.TaskDutyMapper;

/**
 * User: resinas
 * Date: 07/07/13
 * Time: 12:05
 */
public class DTClassicTaskDutyMapper implements TaskDutyMapper {
    @Override
    public String map(TaskDuty duty) {
        String isPotentialDuty;
        switch(duty) {
            case RESPONSIBLE:
                isPotentialDuty = Definitions.ISPOTENTIALRESPONSIBLE; break;
            case ACCOUNTABLE:
                isPotentialDuty = Definitions.ISPOTENTIALACCOUNTABLE; break;
            case SUPPORT:
                isPotentialDuty = Definitions.ISPOTENTIALSUPPORT; break;
            case INFORMED:
                isPotentialDuty = Definitions.ISPOTENTIALINFORMED; break;
            case CONSULTED:
                isPotentialDuty = Definitions.ISPOTENTIALCONSULTED; break;
            default:
                isPotentialDuty = Definitions.ISPOTENTIALPARTICIPANT;
        }

        return isPotentialDuty;
    }
}
