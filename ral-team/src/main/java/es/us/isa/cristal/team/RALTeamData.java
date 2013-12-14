package es.us.isa.cristal.team;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

/**
 * RALTeamData
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class RALTeamData extends RALTeamDefinitions {
    private BPEngine bpEngine;
    private String pid;
    private RALTeamCore core;

    public RALTeamData(IdMapper idMapper, BPEngine bpEngine, String pid) {
        this.bpEngine = bpEngine;
        this.pid = pid;
        this.core = new RALTeamCore(idMapper);
    }

    public String inDataField(String data, String field) {
        return core.teamID(bpEngine.getDataValue(pid, data, field));
    }

    public String ofTypeInDataField(String data, String field) {
        return core.ofType(bpEngine.getDataValue(pid, data, field));
    }

    public String createdByPersonInDataField(String data, String field) {
        return core.createdBy(bpEngine.getDataValue(pid, data, field));
    }

    public String containingTeamRoleTypeInDataField(String data, String field) {
        return core.containing(bpEngine.getDataValue(pid, data, field));
    }

}
