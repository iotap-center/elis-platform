package se.mah.elis.authentication.oauth;

import se.mah.elis.services.users.Role;

/**
 * Elis OAuth 2.0 storage backend
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
public interface OAuthStorage {

	/**
	 * Temporarily store an authorization code. Recommended TTL is 10 min
	 * according to the RFC6749.
	 *  
	 * @param clientId
	 * @param redirectUri
	 * @param code
	 */
	public void storeAuthorizationCode(String clientId, String redirectUri,
			OAuthCode code, long timeToLive);

	/**
	 * Retrieve an authorization code from storage. 
	 * 
	 * @param clientId
	 * @param redirectUri
	 * @return the authorization code
	 */
	public OAuthCode getAuthorizationCode(String clientId, String redirectUri);
	
	/**
	 * Remove an authorization code from storage. 
	 * 
	 * @param clientId
	 * @param redirectUri
	 */
	public void removeAuthorizationCode(String clientId, String redirectUri);

	/**
	 * Store an access token. 
	 * 
	 * @param clientId
	 * @param {@link OAuthCode}
	 * @param timeToLiveInMillis
	 */
	public void storeAccessToken(String clientId, OAuthCode token,
			long timeToLiveInMillis, Role role);

	/**
	 * Retrieve an access token from storage. 
	 * 
	 * @param clientId
	 * @return the access token as {@link OAuthCode}
	 */
	public OAuthCode getAccessToken(String clientId);
	
	/**
	 * Look up an access token from storage
	 * 
	 * @param token
	 * @return the {@link OAuthCode} object
	 */
	public OAuthCode lookupAccessToken(String token); 
	
	/**
	 * Look up a user role that is assigned to a specific token
	 * 
	 * @param token
	 * @return the access role which the token is linked to
	 */
	public Role lookupRole(String token); 
	
	/**
	 * Remove an access token
	 * 
	 * @param token
	 */
	public void removeAccessToken(String token); 
	
}
