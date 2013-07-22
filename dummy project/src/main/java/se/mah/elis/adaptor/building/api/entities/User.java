/**
 * 
 */
package se.mah.elis.adaptor.building.api.entities;

/**
 * The User interface describes a user of the local building system, e.g. a
 * home owner or a system administrator.
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public interface User {
	
	/**
	 * 
	 * 
	 * @return
	 */
	int getId();
	
	/**
	 * 
	 * 
	 * @param id
	 */
	void setId(int id);
}
