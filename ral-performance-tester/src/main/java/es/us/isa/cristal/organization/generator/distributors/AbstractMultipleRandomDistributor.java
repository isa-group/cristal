package es.us.isa.cristal.organization.generator.distributors;

import java.util.List;

import es.us.isa.cristal.organization.generator.functions.Function;
import es.us.isa.cristal.organization.generator.functions.RandomFunction;
import es.us.isa.cristal.organization.generator.selectors.Selector;

/**
 * 
 * @author Manuel Leon
 *
 */
public abstract class AbstractMultipleRandomDistributor<S,T> implements Distributor<S,T> {
	
	protected Integer min;
	
	protected Integer max;
	
	protected Function relationDistributionFunction;
	
	protected Selector<S> entitySelector;
	
	public AbstractMultipleRandomDistributor(Integer min, Integer max, Selector<S> selector){
		this.min = min;
		this.max = max;
		relationDistributionFunction = new RandomFunction(min,max);
		this.entitySelector = selector;
	}
	
	public void distribute(List<S> entities, List<T> entities2) {
		for(T entity2: entities2){
			Integer numberOfEntitiesToLink = relationDistributionFunction.getResult();
			for(int i=0; i<numberOfEntitiesToLink; i++){
				S entity = entitySelector.getIndividual(entities, getExclusions(entity2));
				if(entity!=null){
					setRelation(entity, entity2);
				}
			}
		}
	}
	
	public abstract void setRelation(S entity,T entity2);
	
	public abstract List<S> getExclusions(T entity2);
	
}
