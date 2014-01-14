package es.us.isa.cristal.organization.generator.selectors;

import java.util.ArrayList;
import java.util.List;

public class ConsecutiveSelector<T> implements Selector<T> {

	private Integer currentIndex;
	
	public ConsecutiveSelector(){
		currentIndex=0;
	}
	
	public T getIndividual(List<T> sample) {
		return this.getIndividual(sample, null);
		
	}

	public T getIndividual(List<T> sample, List<T> exclusions) {
		List<T> newSample = new ArrayList<T>();
		newSample.addAll(sample);
		if(exclusions!=null){
			newSample.removeAll(exclusions);
		}
		if(currentIndex>=newSample.size()){
			currentIndex=0;
		}
		T result = newSample.get(currentIndex);
		currentIndex++;
		return result;
	}

}
