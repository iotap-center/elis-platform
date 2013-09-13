/**
 * 
 */
package se.mah.elis.services.users;

/**
 * @author "Johan Holmberg, Malmö University"
 *
 */
public interface PlatformUser extends AbstractUser {
	void setFirstName(String name);
	
	String getFirstName();
	
	void setLastName(String name);
	
	String getLastName();
	
	void setEmail(String address);
	
	String getEmail();
}
