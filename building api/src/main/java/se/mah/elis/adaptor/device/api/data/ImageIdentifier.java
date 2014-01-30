/**
 * 
 */
package se.mah.elis.adaptor.device.api.data;

import se.mah.elis.data.Identifier;

/**
 * The ImageIdentifier interface is used to address an image originating from a
 * camera. Since the internal data structures in various camera systems differ
 * considerably, we will abstain from make any assumptions about the way that
 * the identifier is actually represented.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface ImageIdentifier extends Identifier {

}
