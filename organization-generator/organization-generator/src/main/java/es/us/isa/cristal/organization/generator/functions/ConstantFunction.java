package es.us.isa.cristal.organization.generator.functions;

public class ConstantFunction implements Function {

	private Integer value;
	
	
	
	public ConstantFunction(Integer value) {
		super();
		this.value = value;
	}


	public Integer getResult() {
		return value;
	}

}
