package se.mah.elis.authentication.oauth.impl.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.mah.elis.authentication.oauth.OAuthCode;
import se.mah.elis.authentication.oauth.OAuthCodeKey;
import se.mah.elis.authentication.oauth.OAuthStorage;

/**
 * In-memory storage for authorization codes and access tokens.
 * 
 * Note that this implementation does not remember which authorization code was
 * used to produce which access token, hence, access tokens cannot be revoked if
 * the authorization code is compromised.
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public class OAuthInMemoryStorage implements OAuthStorage {

	private Map<OAuthCodeKey, OAuthCode> authorizationCodes;
	private Map<String, OAuthCode> accessTokens;

	public OAuthInMemoryStorage() {
		this.authorizationCodes = new HashMap<OAuthCodeKey, OAuthCode>();
		this.accessTokens = new HashMap<String, OAuthCode>();
	}

	@Override
	public OAuthCode getAccessToken(String clientId) {
		return accessTokens.get(clientId);
	}

	@Override
	public OAuthCode getAuthorizationCode(String clientId, String redirectUri) {
		OAuthCode code = null;
		
		OAuthCodeKey key = OAuthCodeKeyImpl.createKey(clientId, redirectUri);
		
		if (authorizationCodes.containsKey(key))
			code = authorizationCodes.get(key);

		return code;
	}

	@Override
	synchronized public void removeAccessToken(String clientId) {
		accessTokens.remove(clientId);
	}

	@Override
	synchronized public void removeAuthorizationCode(String clientId,
			String redirectUri) {
		OAuthCodeKey key = OAuthCodeKeyImpl.createKey(clientId, redirectUri);
		authorizationCodes.remove(key);
	}

	/**
	 * TTL is ignored for now
	 * 
	 * @version 1.0.0
	 * @since 1.0
	 */
	@Override
	synchronized public void storeAccessToken(String clientId,
			OAuthCode accessToken, int ttl) {
		if (isValidString(clientId) && isValidCode(accessToken))
			accessTokens.put(clientId, accessToken);
	}

	@Override
	synchronized public void storeAuthorizationCode(String clientId,
			String redirectUri, OAuthCode code, int timeToLive) {
		if (isValidString(clientId) && isValidCode(code)) {
			OAuthCodeKey key = OAuthCodeKeyImpl.createKey(clientId, redirectUri);
			authorizationCodes.put(key, code);
		}
	}


	/*
	 * Helpers 
	 */	
	private boolean isValidString(String clientId) {
		return clientId != null && !clientId.isEmpty();
	}

	private boolean isValidCode(OAuthCode code) {
		return code != null && !code.isExpired();
	}
	
	/*
	 * For testing purposes
	 */
	void setAuthCodeMap(Map<OAuthCodeKey, OAuthCode> map) {
		this.authorizationCodes = map;
	}

	void setAccessTokenMap(Map<String, OAuthCode> map) {
		this.accessTokens = map;
	}

	Map<OAuthCodeKey, OAuthCode> getAuthorizationCodeMap() {
		return this.authorizationCodes;
	}

	Map<String, OAuthCode> getAccessTokensMap() {
		return this.accessTokens;
	}
}
