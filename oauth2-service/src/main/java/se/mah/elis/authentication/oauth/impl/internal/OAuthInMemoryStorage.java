package se.mah.elis.authentication.oauth.impl.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.mah.elis.authentication.oauth.OAuthCode;
import se.mah.elis.authentication.oauth.OAuthCodeKey;
import se.mah.elis.authentication.oauth.OAuthStorage;
import se.mah.elis.services.users.Role;

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
	private Map<OAuthCode, Role> accessTokenRoleMap;

	public OAuthInMemoryStorage() {
		this.authorizationCodes = new HashMap<OAuthCodeKey, OAuthCode>();
		this.accessTokens = new HashMap<String, OAuthCode>();
		this.accessTokenRoleMap = new HashMap<OAuthCode, Role>();
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
	public OAuthCode lookupAccessToken(String token) {
		OAuthCode code = null;
		for (OAuthCode c : accessTokens.values()) {
			if (token.equals(c.getCode())) 
				code = c;
		}
		return code;
	}
	
	@Override
	public Role lookupRole(String token) {
		Role role = null;
		OAuthCode code = lookupAccessToken(token);
		if (code != null && isValidString(token))
			role = accessTokenRoleMap.get(code);
		return role;
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
			OAuthCode accessToken, long ttl, Role role) {
		// make this a transaction
		if (isValidString(clientId) 
				&& isValidCode(accessToken)
				&& isValidRole(role)) {
			accessTokens.put(clientId, accessToken);
			accessTokenRoleMap.put(accessToken, role);
		}
	}

	@Override
	synchronized public void storeAuthorizationCode(String clientId,
			String redirectUri, OAuthCode code, long timeToLive) {
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
	
	private boolean isValidRole(Role role) {
		return role != null;
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

	void setAccessTokenRoleMap(Map<OAuthCode, Role> map) {
		this.accessTokenRoleMap = map;
	}

	Map<OAuthCodeKey, OAuthCode> getAuthorizationCodeMap() {
		return this.authorizationCodes;
	}

	Map<String, OAuthCode> getAccessTokensMap() {
		return this.accessTokens;
	}

}
