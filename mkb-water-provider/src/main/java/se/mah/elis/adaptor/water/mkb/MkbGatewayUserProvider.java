package se.mah.elis.adaptor.water.mkb;

import javax.naming.AuthenticationException;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.device.api.providers.GatewayUserProvider;
import se.mah.elis.services.users.exceptions.UserInitalizationException;

/**
 * Factory which is able to create instances of MKB water meter users.  
 * 
 * @author Marcus Ljungblad
 * @since 1.0
 * @version 1.0
 *
 */
public class MkbGatewayUserProvider implements GatewayUserProvider {

	@Override
	public GatewayUser getUser(String meterId, String _ignored)
			throws MethodNotSupportedException, AuthenticationException {
		MkbGatewayUser mkbUser = createUser(meterId);
		MkbGateway gateway = createGateway(mkbUser);
		mkbUser.setGateway(gateway);
		gateway.setUser(mkbUser);
		try {
			mkbUser.initialize();
		} catch (UserInitalizationException e) {
			e.printStackTrace();
		}
		return mkbUser;
	}

	/**
	 * Create a new water gateway
	 * 
	 * @param mkbUser
	 * @return
	 */
	public MkbGateway createGateway(MkbGatewayUser mkbUser) {
		return new MkbGateway();
	}

	private MkbGatewayUser createUser(String meterId) {
		MkbGatewayUser user = new MkbGatewayUser();
		user.setIdentifier(new MkbUserIdentifier(meterId));
		return user;
	}

}
