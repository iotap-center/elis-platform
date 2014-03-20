/**
 * 
 */
package se.mah.elis.adaptor.device.api.data;

import se.mah.elis.services.users.UserIdentifier;

/**
 * The GatewayUserIdentifier interface is used identify a gateway user. Since
 * the internal data structures in various gateway systems differ considerably,
 * we will abstain from make any assumptions about the way that the identifier
 * is actually represented.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface GatewayUserIdentifier extends UserIdentifier {

}
