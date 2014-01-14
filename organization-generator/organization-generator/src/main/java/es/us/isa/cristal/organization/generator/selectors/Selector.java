package es.us.isa.cristal.organization.generator.selectors;

import java.util.List;

public interface Selector<T> {
	
	T getIndividual(List<T> sample, List<T> exclusions);

	T getIndividual(List<T> sample);
	
}
