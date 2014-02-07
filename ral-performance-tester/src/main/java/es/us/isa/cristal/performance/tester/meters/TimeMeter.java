package es.us.isa.cristal.performance.tester.meters;

import java.util.Date;

/**
 * 
 * @author Manuel Leon
 *
 */
public class TimeMeter implements Meter<Long> {

	private long startMillis;
	
	private long stopMillis;
	
	@Override
	public void start() {
		startMillis = (new Date()).getTime();
		
	}

	@Override
	public void stop() {
		stopMillis = (new Date()).getTime();
		
	}

	@Override
	public Long getResult() {
		return (stopMillis - startMillis);
	}
	

}
