package se.mah.elis.authentication.oauth.impl.internal;

import se.mah.elis.authentication.oauth.OAuthCode;
import se.mah.elis.authentication.oauth.OAuthService;
import se.mah.elis.authentication.oauth.OAuthStorage;

public class OAuthServiceImpl implements OAuthService {

	// backend
	private OAuthStorage storage;

	public OAuthServiceImpl(OAuthStorage storage) {
		this.storage = storage;
	}

	@Override
	public String createAuthorizationCode(String clientId) {
		return createAuthorizationCode(clientId, null);
	}

	@Override
	public String createAuthorizationCode(String clientId, String redirectUri) {
		OAuthCode authCode = OAuthCodeImpl.create();
		storage.storeAuthorizationCode(clientId, redirectUri, authCode,
				authCode.getTimeToLive());
		return authCode.getCode();
	}

	@Override
	public String createAccessToken(String clientId, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createRefreshToken(String clientId, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean verifyAccessToken(String token) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyAuthorizationCode(String clientId, String code) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyAuthorizationCode(String clientId, String redirectUri,
			String code) {
		// TODO Auto-generated method stub
		return false;
	}

}
