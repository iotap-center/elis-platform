package se.mah.elis.authentication.oauth.impl.internal;

import se.mah.elis.authentication.oauth.OAuthService;

public class OAuthServiceImpl implements OAuthService {

	@Override
	public String createAuthorizationCode(String clientId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String createAuthorizationCode(String clientId, String redirectUri) {
		// TODO Auto-generated method stub
		return null;
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
	public boolean verifyAuthorizationCode(String clientId, String redirectUri, String code) {
		// TODO Auto-generated method stub
		return false;
	}

}
