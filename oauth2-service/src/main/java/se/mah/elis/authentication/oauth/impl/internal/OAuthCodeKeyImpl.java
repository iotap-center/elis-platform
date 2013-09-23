package se.mah.elis.authentication.oauth.impl.internal;

import se.mah.elis.authentication.oauth.OAuthCodeKey;

/**
 * Key to be used for identifying authorization code requests.
 * 
 * Create keys using {@link #createKey(String, String)}.
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 *
 */
public class OAuthCodeKeyImpl implements OAuthCodeKey {

	private String clientId;
	private String redirectUri;

	// force use of factory
	private OAuthCodeKeyImpl() { }
	
	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	@Override
	public int hashCode() {
		return this.clientId.hashCode() + this.redirectUri.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof OAuthCodeKeyImpl)) 
			return false;
		
		return this.toString().equals(other.toString());
	}
	
	@Override
	public String toString() {
		return this.getClientId() + this.redirectUri;
	}
	
	/**
	 * Factory to create keys based on client id and redirect uri
	 * 
	 * @param clientId
	 * @param redirectUri
	 * @return a key that can be used with {@link Map}
	 */
	public static OAuthCodeKey createKey(String clientId, String redirectUri) {
		redirectUri = (redirectUri == null) ? "" : redirectUri;
		OAuthCodeKeyImpl key = new OAuthCodeKeyImpl();
		key.setClientId(clientId);
		key.setRedirectUri(redirectUri);
		return key;
	}
}
