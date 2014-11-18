package es.us.isa.cristal.performance.tester.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * 
 * @author Manuel Leon
 *
 */
public class ExperimentConfiguration {
	
	private String export;
	private int modelWeight;
	private boolean processExpressionForEachIteration;
	private int iterations;
	private List<Query> queryList;


	public final List<Query> getQueryList() {
		return queryList;
	}

	public final void setQueryList(List<Query> queryList) {
		this.queryList = queryList;
	}
	
	public final int getModelWeight() {
		return modelWeight;
	}

	public final void setModelWeight(int modelWeight) {
		this.modelWeight = modelWeight;
	}

	public final int getIterations() {
		return iterations;
	}

	public final void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
	public final String getExport() {
		return export;
	}

	public final void setExport(String export) {
		this.export = export;
	}

	public final boolean getProcessExpressionForEachIteration() {
		return processExpressionForEachIteration;
	}

	public final void setProcessExpressionForEachIteration(
			boolean processExpressionForEachIteration) {
		this.processExpressionForEachIteration = processExpressionForEachIteration;
	}

	public final Map<String,Query> getActivityQueryMap(){
		Map<String,Query> map = new HashMap<String,Query>();
		
		for(Query q: queryList){
			map.put(q.getActivity(), q);
		}
		
		return map;
	}
	
	public static final ExperimentConfiguration importFromJson(String json){
		Gson gson = new Gson();
		return gson.fromJson(json, ExperimentConfiguration.class);
	}
	
}
