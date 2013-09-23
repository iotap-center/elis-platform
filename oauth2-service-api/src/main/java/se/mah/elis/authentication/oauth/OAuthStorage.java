package se.mah.elis.authentication.oauth;

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
			OAuthCode code, int timeToLive);

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
	 * @param token
	 * @param timeToLiveInMillis
	 */
	public void storeAccessToken(String clientId, String token,
			int timeToLiveInMillis);

	/**
	 * Retrieve an access token from storage. 
	 * 
	 * @param clientId
	 * @return the access token 
	 */
	public String getAccessToken(String clientId);
	
	/**
	 * Remove an access token
	 * 
	 * @param token
	 */
	public void removeAccessToken(String token); 
	
}
