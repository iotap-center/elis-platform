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

	/**
	 * Check if the code has expired or not.
	 *  
	 * @return true if code is expired
	 */
	public boolean isExpired();

	/**
	 * Get at string representation of the code
	 * 
	 * @return code as string
	 */
	public String getCode();
	
	/**
	 * Returns the time to live in millis
	 * 
	 * @return time in millis
	 */
	public long getTimeToLive();

	/**
	 * Returns the time of code creation 
	 * 
	 * @return time in millis
	 */
	public long getCreationTime();

	public boolean equals(Object other);

	public int hashCode();

	public String toString();

}
