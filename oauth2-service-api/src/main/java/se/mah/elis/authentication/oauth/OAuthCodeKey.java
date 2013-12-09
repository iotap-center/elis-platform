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

	/**
	 * Get the redirect URI for the client
	 * 
	 * @return the URI as String
	 */
	public String getRedirectUri();

	/**
	 * Set a redirect URI
	 * 
	 * @param redirectUri
	 */
	public void setRedirectUri(String redirectUri);

	/**
	 * Get the client identifier
	 * 
	 * @return client id as String
	 */
	public String getClientId();

	/**
	 * Set client identifier
	 * 
	 * @param clientId
	 */
	public void setClientId(String clientId);

	public String toString();

	public int hashCode();

	public boolean equals(Object other);
}
