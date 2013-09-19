package se.mah.elis.authentication.oauth;

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
	 * @return a hashed token used for authorizing the client
	 */
	public String createAuthorizationCode();
	
	/**
	 * Create an access token for the application (step 2 in OAuth 2.0)
	 * 
	 * The access token may be time restricted. If so it can be renewed using the 
	 * {@link #createRefreshToken()}. Time restrictions are optional. 
	 * 
	 * @return a hashed access token used by the application
	 */
	public String createAccessToken();
	
	/**
	 * Used to create a refresh token if access token is time restricted. This token
	 * is usually delivered with the access token to the application (step 2).
	 *  
	 * @return a hashed token used by the application to renew the access token
	 */
	public String createRefreshToken();
	
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
	 * @param code
	 * @return true if the authorization code is valid
	 */
	public boolean verifyAuthorizationCode(String code);
}
