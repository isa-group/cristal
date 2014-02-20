package es.us.isa.cristal.performance.tester.meters;

/**
 * 
 * @author Manuel Leon
 *
 */
public interface Meter<T> {

	void start();
	
	void stop();
	
	T getResult();
	
}
