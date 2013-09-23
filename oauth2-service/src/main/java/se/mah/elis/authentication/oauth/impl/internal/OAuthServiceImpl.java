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
	public String createAuthorizationCode(String clientId, String redirectUri) {
		OAuthCode authCode = OAuthCodeImpl.create();
		storage.storeAuthorizationCode(clientId, redirectUri, authCode,
				authCode.getTimeToLive());
		return authCode.getCode();
	}

	@Override
	public String createAccessToken(String clientId, String redirectUri, String code) {
		String token = null;
		OAuthCode authCode = storage.getAuthorizationCode(clientId, redirectUri);
		if (authCode != null && !authCode.isExpired()) {
			long ttlSixMonths = 1000*60*60*24*30*6;
			OAuthCode tokenCode = OAuthCodeImpl.create(ttlSixMonths);
			storage.storeAccessToken(clientId, tokenCode, ttlSixMonths);
			token = tokenCode.getCode();
		}
		return token;
	}

	@Override
	public String createRefreshToken(String clientId, String redirectUri, String code) {
		return createAccessToken(clientId, redirectUri, code);
	}

	@Override
	public boolean verifyAccessToken(String token) {
		OAuthCode accessTokenCode = storage.lookupAccessToken(token);
		return accessTokenCode != null && !accessTokenCode.isExpired();
	}

	@Override
	public boolean verifyAuthorizationCode(String clientId, String redirectUri,
			String code) {
		OAuthCode authCode = storage.getAuthorizationCode(clientId, redirectUri);
		return validateAuthCode(code, authCode);
	}

	private boolean validateAuthCode(String code, OAuthCode authCode) {
		return authCode != null 
				&& !authCode.isExpired() 
				&& code.equals(authCode.toString());
	}

}
