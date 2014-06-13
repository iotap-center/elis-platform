package se.mah.elis.services.storage.factory;

import java.util.Properties;

/**
 * The DataRecipe interface describes a way to build a user, using a
 * DataFactory.
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 2.0
 */
public interface DataObjectRecipe {
	
	/**
	 * Returns the data type description. This should match the name of the
	 * ElisDataObject interface being implemented. If no such interface name
	 * exists, or if it is not uniquely identifiable within the service, the
	 * class name of the object should be used instead.
	 * 
	 * @return The name of the user type.
	 * @since 2.0
	 */
	public String getDataType();
	
	/**
	 * Returns the service name that provides this data type.
	 * 
	 * @return The name of the providing service.
	 * @since 2.0
	 */
	public String getServiceName();
	
	/**
	 * Returns a list of properties needed to successfully build a data object.
	 * 
	 * @return A list of properties.
	 * @since 2.0
	 */
	public Properties getProperties();
}
