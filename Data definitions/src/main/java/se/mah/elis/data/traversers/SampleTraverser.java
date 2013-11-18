/**
 * 
 */
package se.mah.elis.data.traversers;

import java.lang.reflect.Method;

import se.mah.elis.auxiliaries.data.Sample;
import se.mah.elis.auxiliaries.exceptions.MismatchingSampleException;

/**
 * The SampleTraverser class provides a number of methods for traversing lists
 * of samples. All methods are static, so instantiation of this class is never
 * needed.
 * 
 * <p>
 * The inner workings of this class aren't beautiful. Don't judge me.
 * </p>
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class SampleTraverser {
	
	/**
	 * Sums up values from a set of samples.
	 * 
	 * @param samples An array of Sample objects. 
	 * @param method The name of the method that should be used for the summing
	 * 		  up.
	 * @return The calculated sum.
	 * @throws MismatchingSampleException if a sample in the set isn't an
	 * 		   instance of a class with the provided method.
	 * @since 1.0
	 */
	public static double sumUp(Sample[] samples, String method)
			throws MismatchingSampleException {
		double sum = 0;
		Method m = null;
		
		for (Sample sample : samples) {
			try {
				m = sample.getClass().getMethod(method, null);
				sum += (Double) m.invoke(sample);
			} catch (Exception e) {
				throw new MismatchingSampleException();
			}
		}
		
		// cast samples to their real classes
		// for each sample, apply method and add the result to sum
		// return sum
		
		return sum;
	}

	/**
	 * Finds the highest value from a set of samples.
	 * 
	 * @param samples An array of Sample objects. 
	 * @param method The name of the method that should be used for the summing
	 * 		  up.
	 * @return The calculated sum.
	 * @throws MismatchingSampleException if a sample in the set isn't an
	 * 		   instance of a class with the provided method.
	 * @since 1.0
	 */
	public static double findHighestValue(Sample[] samples, String method)
			throws MismatchingSampleException {
		double highestValue = Double.MIN_VALUE;
		double currentValue = 0;
		Method m = null;
		
		for (Sample sample : samples) {
			try {
				m = sample.getClass().getMethod(method, null);
				currentValue = (Double) m.invoke(sample);
				
				if (currentValue > highestValue) {
					highestValue = currentValue;
				}
			} catch (Exception e) {
				throw new MismatchingSampleException();
			}
		}
		
		return highestValue;
	}

	/**
	 * Finds the lowest value from a set of samples.
	 * 
	 * @param samples An array of Sample objects. 
	 * @param method The name of the method that should be used for the summing
	 * 		  up.
	 * @return The calculated sum.
	 * @throws MismatchingSampleException if a sample in the set isn't an
	 * 		   instance of a class with the provided method.
	 * @since 1.0
	 */
	public static double findLowestValue(Sample[] samples, String method)
			throws MismatchingSampleException {
		double lowestValue = Double.MAX_VALUE;
		double currentValue = 0;
		Method m = null;
		
		for (Sample sample : samples) {
			try {
				m = sample.getClass().getMethod(method, null);
				currentValue = (Double) m.invoke(sample);
				
				if (currentValue < lowestValue) {
					lowestValue = currentValue;
				}
			} catch (Exception e) {
				throw new MismatchingSampleException();
			}
		}
		
		return lowestValue;
	}
}
