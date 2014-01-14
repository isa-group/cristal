package es.us.isa.cristal.organization.generator.functions;

import java.util.Random;

public class RandomFunction implements Function{

	private Integer min;
	
	private Integer max;
	
	
	
	public RandomFunction(Integer min, Integer max) {
		super();
		this.min = min;
		this.max = max+1;
	}



	public Integer getResult() {
		Random rnd = new Random();
		int res = (Math.abs((rnd.nextInt())%(max-min)) + min);
		return res;
		
	}

}
