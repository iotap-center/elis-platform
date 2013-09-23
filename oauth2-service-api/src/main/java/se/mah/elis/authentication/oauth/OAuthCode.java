package se.mah.elis.authentication.oauth;

/**
 * 
 * Representation of a authorization code or access token.
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 *
 */
public interface OAuthCode {

	public boolean isExpired();
	public String getCode();
	public long getTimeToLive();
	public long getCreationTime();
	public boolean equals(Object other);
	public int hashCode();
	public String toString();
	
}
