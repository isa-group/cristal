package es.us.isa.cristal.neo4j.analyzer.exceptions;

import es.us.isa.cristal.model.TaskDuty;

/**
 * 
 * @author Manuel Leon
 *
 */
public class TaskDutyNotSupportedException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1801867697099893713L;

	public TaskDutyNotSupportedException() {
		super();
		
	}
	
	
	public TaskDutyNotSupportedException(TaskDuty duty) {
		super(duty.name() + " TaskDuty is not supported yet.");
		
	}
	public TaskDutyNotSupportedException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public TaskDutyNotSupportedException(String s) {
		super(s);
		
	}

	public TaskDutyNotSupportedException(Throwable cause) {
		super(cause);
		
	}

	
	
}
