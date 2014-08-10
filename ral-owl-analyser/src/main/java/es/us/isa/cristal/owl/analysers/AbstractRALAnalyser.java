package es.us.isa.cristal.owl.analysers;

import es.us.isa.cristal.analyser.RALAnalyser;
import es.us.isa.cristal.owl.DLQueryEngine;
import es.us.isa.cristal.owl.mappers.ral.misc.IdMapper;

public abstract class AbstractRALAnalyser implements RALAnalyser {

	protected DLQueryEngine engine;
    protected IdMapper idMapper;

	public AbstractRALAnalyser(DLQueryEngine engine, IdMapper mapper) {
		super();
        this.engine = engine;
        this.idMapper = mapper;
    }


}