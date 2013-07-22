/**
 * 
 */
package se.mah.elis.adaptor.building.api.data;

import java.net.URL;

/**
 * The Image interface is used to encapsulate information regarding images from
 * camera sensors.
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public interface Image {
	
	/**
	 * This method is used to get the URL from an image object.
	 * 
	 * @return A URL object encapsulating the image's URL.
	 */
	URL getURL();
}
