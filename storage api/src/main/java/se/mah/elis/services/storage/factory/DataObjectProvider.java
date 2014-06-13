/**
 * 
 */
package se.mah.elis.services.storage.factory;

import java.util.Properties;

import se.mah.elis.data.ElisDataObject;
import se.mah.elis.data.exceptions.DataInitalizationException;
import se.mah.elis.services.users.User;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * The DataObjectProvider interface is used to register user providing services
 * to a DataObjectFactory. Each service that provides ElisDataObjects of some
 * kind should implement DataObjectProvider to facilitate recreation of those
 * objects by DataObjectFactory. 
 * 
 * @author "Johan Holmberg, Malm\u00f6 University"
 * @since 2.0
 */
public interface DataObjectProvider {
	
	/**
	 * Builds a data object.
	 * 
	 * @param properties A set of properties needed to build this kind of user.
	 * @return The newly built data object.
	 * @throws DataInitalizationException if the user couldn't be built for
	 * 		   whatever reason.
	 * @since 2.0
	 */
	public ElisDataObject build(Properties properties)throws DataInitalizationException;
	
	/**
	 * Returns the user recipe describing the data needed to successfully build
	 * a data of this kind. 
	 * 
	 * @return The data recipe.
	 * @since 2.0
	 */
	public DataObjectRecipe getRecipe();
}
