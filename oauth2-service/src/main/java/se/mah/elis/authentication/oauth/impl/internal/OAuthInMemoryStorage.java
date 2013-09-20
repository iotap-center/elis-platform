package se.mah.elis.authentication.oauth.impl.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private Map<String, Map<String, String>> authorizationCodes;
	private Map<String, String> accessTokens;

	public OAuthInMemoryStorage() {
		this.authorizationCodes = new HashMap<String, Map<String, String>>();
		this.accessTokens = new HashMap<String, String>();
	}

	@Override
	public String getAccessToken(String clientId) {
		return accessTokens.get(clientId);
	}

	@Override
	public String getAuthorizationCode(String clientId, String redirectUri) {
		String code = null;

		if (authorizationCodes.containsKey(clientId))
			code = authorizationCodes.get(clientId).get(redirectUri);

		return code;
	}

	@Override
	public void removeAccessToken(String clientId) {
		accessTokens.remove(clientId);
	}

	@Override
	synchronized public void removeAuthorizationCode(String clientId,
			String redirectUri) {
		if (authorizationCodes.containsKey(clientId)) {
			authorizationCodes.get(clientId).remove(redirectUri);
			if (authorizationCodes.get(clientId).isEmpty())
				authorizationCodes.remove(clientId);
		}
	}

	/**
	 * TTL is ignored for now
	 * 
	 * @version 1.0.0
	 * @since 1.0
	 */
	@Override
	synchronized public void storeAccessToken(String clientId,
			String accessToken, int ttl) {
		if (isValidString(clientId) && isValidString(accessToken))
			accessTokens.put(clientId, accessToken);
	}

	@Override
	synchronized public void storeAuthorizationCode(String clientId,
			String redirectUri, String code) {
		if (isValidString(clientId) && isValidString(code)) { 
			if (authorizationCodes.containsKey(clientId)) {
				redirectUri = redirectUri == null ? "" : redirectUri;
				authorizationCodes.get(clientId).put(redirectUri, code);
			} else {
				Map<String, String> entry = new HashMap<String, String>();
				entry.put(redirectUri, code);
				authorizationCodes.put(clientId, entry);
			}
		}
	}

	/*
	 * Helpers 
	 */	
	private boolean isValidString(String clientId) {
		return clientId != null && !clientId.isEmpty();
	}

	/*
	 * For testing purposes
	 */
	void setAuthCodeMap(Map<String, Map<String, String>> map) {
		this.authorizationCodes = map;
	}

	void setAccessTokenMap(Map<String, String> map) {
		this.accessTokens = map;
	}

	Map<String, Map<String, String>> getAuthorizationCodeMap() {
		return this.authorizationCodes;
	}

	Map<String, String> getAccessTokensMap() {
		return this.accessTokens;
	}
}
