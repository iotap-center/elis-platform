package se.mah.elis.services.storage.factory;

import java.util.Properties;

/**
 * The DataRecipe interface describes a way to build a data object, using a
 * DataFactory.
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 2.0
 */
public interface DataObjectRecipe {
	
	/**
	 * Returns the data type description. This should match the name of the
	 * ElisDataObject class.
	 * 
	 * @return The name of the user type.
	 * @since 2.0
	 */
	public String getDataType();
	
	/**
	 * Returns a list of properties needed to successfully build a data object.
	 * 
	 * @return A list of properties.
	 * @since 2.0
	 */
	public Properties getProperties();
}
