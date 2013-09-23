package se.mah.elis.authentication.oauth.impl.internal;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.authentication.oauth.OAuthCode;
import se.mah.elis.authentication.oauth.OAuthCodeKey;
import se.mah.elis.authentication.oauth.OAuthStorage;

public class OAuthServiceImplTest {

	private static final String CLIENT_ID = "CLIENT_ID";
	private static final String REDIRECT_URI = "http://mah.se/elis/rules";
	private OAuthServiceImpl oauth;
	private OAuthCode code; 
	private OAuthCodeKey key;
	
	@Before
	public void setUp() {
		code = mock(OAuthCode.class);
		key = mock(OAuthCodeKey.class);
		OAuthStorage storage = mock(OAuthStorage.class);
		oauth = new OAuthServiceImpl(storage);
	}
	
	@Test
	public void createAuthorizationCodeWithClientIdOnly() {
		assertNotNull(oauth.createAuthorizationCode(CLIENT_ID));
	}
	
	@Test
	public void createAuthorizationCodeWithClientIdAndRedirectUri() {
		assertNotNull(oauth.createAuthorizationCode(CLIENT_ID, REDIRECT_URI));
	}
	
	@Test
	public void createAccessTokenWithClientId() {
		fail();
	}
	
	@Test
	public void createRefreshTokenCodeWithClientId() {
		fail();
	}
	
	@Test
	public void ensureValidAccessTokensAreVerified() {
		fail();
	}
	
	@Test
	public void ensureValidAuthorizationCode() {
		fail();
	}
}
