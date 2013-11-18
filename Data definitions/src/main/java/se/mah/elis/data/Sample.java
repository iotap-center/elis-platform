/**
 * 
 */
package se.mah.elis.data;

import java.util.Date;
import java.util.List;

/**
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Sample {
	
	/**
	 * Gets the sample length measured in milliseconds.
	 * 
	 * @return The sample length in milliseconds.
	 * @since 1.0
	 */
	int getSampleLength();

	/**
	 * Gets the moment in time when this sample was taken. The timestamp is
	 * generated when the object is initialized, or in the case of saved data,
	 * at the moment the sample was stored.
	 * 
	 * @return The time when the sample was taken.
	 * @since 1.0
	 */
	Date getSampleTimestamp();
	
	/**
	 * Gets a list of names of all methods that are traversable by a
	 * {@link se.mah.elis.auxiliaries.SampleTraverser}. The names are used to
	 * specify what data the traverser should look at. This is helpful for e.g.
	 * summarizing power consumption.
	 * 
	 * @return A list of String objects.
	 * @since 1.0
	 */
	List<String> getTraversableMethods();
}
