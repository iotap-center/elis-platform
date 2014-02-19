package se.mah.elis.adaptor.water.mkb;

import javax.naming.AuthenticationException;

import se.mah.elis.adaptor.device.api.entities.GatewayUser;
import se.mah.elis.adaptor.device.api.exceptions.MethodNotSupportedException;
import se.mah.elis.adaptor.device.api.providers.GatewayUserProvider;

public class MkbGatewayUserFactory implements GatewayUserProvider {

	@Override
	public GatewayUser getUser(String meterId, String _ignored)
			throws MethodNotSupportedException, AuthenticationException {
		MkbGatwayUser mkbUser = createUser(meterId);
		MkbGateway gateway = createGateway(mkbUser);
		mkbUser.setGateway(gateway);
		gateway.setUser(mkbUser);
		return mkbUser;
	}

	private MkbGateway createGateway(MkbGatwayUser mkbUser) {
		return new MkbGateway();
	}

	private MkbGatwayUser createUser(String meterId) {
		MkbGatwayUser user = new MkbGatwayUser();
		user.setIdentifier(new MkbUserIdentifier(meterId));
		return user;
	}

}
