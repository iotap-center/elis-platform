package se.mah.elis.authentication.oauth;

import se.mah.elis.services.users.Role;

/**
 * Elis OAuth 2 Service 
 * 
 * Heavily influenced by the Apache OLTU implementation
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 *
 */
public interface OAuthService {

	/**
	 * Create an authorization code (step 1 in OAuth 2.0)
	 * 
	 * @param clientId
	 * @param redirectUri
	 * @return a hashed token used for authorizing the client
	 */
	public String createAuthorizationCode(String clientId, String redirectUri);
	
	/**
	 * Create an access token for the application (step 2 in OAuth 2.0)
	 * 
	 * The access token may be time restricted. If so it can be renewed using the 
	 * {@link #createRefreshToken()}. Time restrictions are optional. 
	 * 
	 * The access token created is linked to the client id and authorization code.
	 * 
	 * @param clientId
	 * @param redirectUri
	 * @param authCode
	 * @param role that access token should be linked to
	 * @return a hashed access token used by the application
	 */
	public String createAccessToken(String clientId, String redirectUri, 
			String authCode, Role role);
	
	/**
	 * Used to create a refresh token if access token is time restricted. This token
	 * is usually delivered with the access token to the application (step 2).
	 *  
	 * The refresh token is linked to the client id and authorization code.
	 *  
	 * @param clientId
	 * @param redirectUri
	 * @param authCode
	 * @return a hashed token used by the application to renew the access token
	 */
	public String createRefreshToken(String clientId, String redirectUri, String authCode);
	
	/**
	 * Service method to verify the validity (may include freshness) of the access 
	 * token. 
	 * 
	 * @param token
	 * @return true if token is valid
	 */
	public boolean verifyAccessToken(String token);
	
	/**
	 * Service method to verify the validity of a client authorization code. 
	 *
	 * A code MUST be rendered invalid once used and SHOULD be invalid if not
	 * used within 10 minutes of issuing.
	 *
	 * @param clientId
	 * @param redirectUri is optional, but MUST match previous redirectUri if used.
	 * @param code
	 * @return true if the authorization code is valid
	 */
	public boolean verifyAuthorizationCode(String clientId, String redirectUri, String code);

}
