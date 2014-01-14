package es.us.isa.cristal.organization.generator.distributors;

import java.util.List;

public interface Distributor<S,T> {
	
	void distribute(List<S> entities, List<T> entities2);
	
}
