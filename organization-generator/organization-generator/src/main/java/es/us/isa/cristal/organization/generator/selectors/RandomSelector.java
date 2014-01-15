package es.us.isa.cristal.organization.generator.selectors;

import java.util.ArrayList;
import java.util.List;

import es.us.isa.cristal.organization.generator.functions.Function;
import es.us.isa.cristal.organization.generator.functions.RandomFunction;

public class RandomSelector<T> implements Selector<T>{

	public T getIndividual(List<T> sample, List<T> exclusions) {
		T result = null;
		
		List<T> newSample = new ArrayList<T>();
		newSample.addAll(sample);
		if(exclusions!=null){
			newSample.removeAll(exclusions);
		}
		if(newSample.size()>0){
			Function f = new RandomFunction(0, newSample.size()-1);
			result = newSample.get(f.getResult());
		}
		return result;
	}

	public T getIndividual(List<T> sample) {
		
		return getIndividual(sample, null);
	}
	
}
