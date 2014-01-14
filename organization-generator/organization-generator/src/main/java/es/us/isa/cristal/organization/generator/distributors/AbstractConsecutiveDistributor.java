package es.us.isa.cristal.organization.generator.distributors;

import java.util.List;

public abstract class AbstractConsecutiveDistributor<S,T> implements Distributor<S,T>{
	
	
	
	public void distribute(List<S> entities, List<T> entities2) {
		int limit = entities.size() > entities2.size() ? entities.size() : entities2.size();
		int j=0;
		int i=0;
		for(int k=0; k<limit; k++){
			setRelation(entities.get(i),entities2.get(j));
			
			i++;
			if(i>=entities.size()){
				i=0;
			}
			j++;
			if(j>=entities2.size()){
				j=0;
			}
		}
	}
	
	public abstract void setRelation(S entity,T entity2);

}
