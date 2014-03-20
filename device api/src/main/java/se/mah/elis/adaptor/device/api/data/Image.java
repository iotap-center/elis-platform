/**
 * 
 */
package se.mah.elis.adaptor.device.api.data;

import java.net.URL;

/**
 * The Image interface is used to encapsulate information regarding images from
 * camera sensors.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Image {
	
	/**
	 * This method is used to get the URL from an image object.
	 * 
	 * @return A URL object encapsulating the image's URL.
	 * @since 1.0
	 */
	URL getURL();
	
	/**
	 * Gets the image's identifier object.
	 * 
	 * @return An ImageIdentifier object.
	 * @since 1.0
	 */
	ImageIdentifier getIdentifier();
}
