package es.us.isa.cristal.performance.tester.meters;

public interface Meter<T> {

	void start();
	
	void stop();
	
	T getResult();
	
}
