package se.mah.elis.authentication.oauth;

/**
 * Elis OAuth 2 Service 
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 *
 */
public interface OAuthService {

	public String createAuthorizationCode();
	public String createAccessToken();
	public String createRefreshToken();
	public boolean verifyAccessToken(String token);
	
}
