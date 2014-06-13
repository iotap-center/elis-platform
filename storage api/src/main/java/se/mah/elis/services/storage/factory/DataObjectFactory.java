/**
 * 
 */
package se.mah.elis.services.storage.factory;

import java.util.Properties;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.exceptions.DataInitalizationException;
import se.mah.elis.services.users.PlatformUser;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * The DataObjectFactory builds data objects. A DataObjectProvider can register
 * with a DataObjectFactory so that the factory can produce the data object
 * types provided by the DataObjectProvider.
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 2.0
 */
public interface DataObjectFactory {
	
	/**
	 * Registers a data object provider with this factory.
	 * 
	 * @param provider The DataObjectProvider to register.
	 * @since 2.0
	 */
	public void registerProvider(DataObjectProvider provider);
	
	/**
	 * Unregisters a data provider from this factory.
	 * 
	 * @param provider The DataObjectProvider to unregister.
	 * @since 2.0
	 */
	public void unregisterProvider(DataObjectProvider provider);
	
	/**
	 * Builds a data object. As a data type can be provided by several
	 * different supporting services, we must also provide the name of
	 * the service that the data object should target.
	 * 
	 * @param dataType The data type that we want to build. This should ideally
	 * 		  match the name of the implemented ElisDataObject interface.
	 * @param serviceName The name of the service that we want to target.
	 * @param properties The properties of the new data object.
	 * @return The newly built ElisDataObject instance.
	 * @throws DataInitalizationException if the data object couldn't be built for
	 * 		   whatever reason.
	 * @since 2.0
	 */
	public ElisDataObject build(String dataType, String serviceName, Properties properties)
			throws DataInitalizationException;
	
	/**
	 * Returns a list of all available data object recipes.
	 * 
	 * @return An array of data object recipes. If no recipes are available,
	 * 		   the method returns an empty array.
	 * @since 2.0
	 */
	public DataObjectRecipe[] getAvailableDataRecipes();
	
	/**
	 * Returns a data object recipe.
	 * 
	 * @param dataType The data type that we want a recipe for.
	 * @param systemName The name of the system providing the data object.
	 * @return A data object recipe.
	 * @since 2.0
	 */
	public DataObjectRecipe getRecipe(String dataType, String systemName);
}
