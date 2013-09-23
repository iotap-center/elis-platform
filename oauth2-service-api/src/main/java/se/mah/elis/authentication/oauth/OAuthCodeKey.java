package se.mah.elis.authentication.oauth;

/**
 * Key to be used for identifying authorization code requests.
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 *
 */
public interface OAuthCodeKey {

	public String getRedirectUri();
	public void setRedirectUri(String redirectUri);
	public String getClientId();
	public void setClientId(String clientId);
	
	public String toString();
	public int hashCode();
	public boolean equals(Object other);
}
