package es.us.isa.cristal.ram2bpmn.templates.rasci;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * RasciDuties
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public final class RasciDuties {
    public static final String RESPONSIBLE = "responsible";
    public static final String ACCOUNTABLE = "accountable";
    public static final String CONSULT = "consult";
    public static final String SUPPORT = "support";
    public static final String INFORMED = "informed";

    public static Collection<String> all() {
        return Arrays.asList(RESPONSIBLE, ACCOUNTABLE, CONSULT, SUPPORT, INFORMED);
    }

}
