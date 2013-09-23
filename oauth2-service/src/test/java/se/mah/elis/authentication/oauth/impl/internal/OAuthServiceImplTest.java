package se.mah.elis.authentication.oauth.impl.internal;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.authentication.oauth.OAuthCode;
import se.mah.elis.authentication.oauth.OAuthCodeKey;
import se.mah.elis.authentication.oauth.OAuthStorage;

public class OAuthServiceImplTest {

	private static final String ACCESS_TOKEN = "SOMEACCESSTOKEN";
	private static final String AUTHCODE = "SOMECODE";
	private static final String CLIENT_ID = "CLIENT_ID";
	private static final String REDIRECT_URI = "http://mah.se/elis/rules";
	private OAuthStorage storage;
	private OAuthServiceImpl oauth;
	private OAuthCode authorizationCode;
	private OAuthCode accessTokenCode;

	@Before
	public void setUp() {
		authorizationCode = mock(OAuthCode.class);
		when(authorizationCode.toString()).thenReturn(AUTHCODE);
		when(authorizationCode.getCode()).thenReturn(AUTHCODE);
		when(authorizationCode.isExpired()).thenReturn(false);

		accessTokenCode = mock(OAuthCode.class);
		when(accessTokenCode.toString()).thenReturn(ACCESS_TOKEN);
		when(accessTokenCode.getCode()).thenReturn(ACCESS_TOKEN);
		when(accessTokenCode.isExpired()).thenReturn(false);

		storage = mock(OAuthStorage.class);
		when(storage.getAuthorizationCode(CLIENT_ID, REDIRECT_URI)).thenReturn(
				authorizationCode);
		when(storage.getAccessToken(CLIENT_ID)).thenReturn(accessTokenCode);
		when(storage.lookupAccessToken(anyString()))
				.thenReturn(accessTokenCode);

		oauth = new OAuthServiceImpl(storage);
	}

	@Test
	public void createAuthorizationCodeWithClientIdAndRedirectUri() {
		assertNotNull(oauth.createAuthorizationCode(CLIENT_ID, REDIRECT_URI));
		verify(storage).storeAuthorizationCode(anyString(), 
				anyString(), any(OAuthCode.class), anyInt());
	}

	@Test
	public void createAccessTokenWithClientId() {
		assertNotNull(oauth.createAccessToken(CLIENT_ID, REDIRECT_URI, AUTHCODE));
		verify(storage).storeAccessToken(anyString(), any(OAuthCode.class), anyInt());
	}

	@Test
	public void createRefreshTokenCodeWithClientId() {
		assertNotNull(oauth.createRefreshToken(CLIENT_ID, REDIRECT_URI, AUTHCODE));
	}

	@Test
	public void ensureValidAccessTokensAreVerified() {
		assertTrue(oauth.verifyAccessToken(ACCESS_TOKEN));
	}

	@Test
	public void ensureValidAuthorizationCode() {
		assertTrue(oauth.verifyAuthorizationCode(CLIENT_ID, REDIRECT_URI, AUTHCODE));
	}

	@Test
	public void ensureValidAuthorizationCodeWithClientIdAndRedirect() {
		assertTrue(oauth.verifyAuthorizationCode(CLIENT_ID, REDIRECT_URI,
				AUTHCODE));
	}
}
