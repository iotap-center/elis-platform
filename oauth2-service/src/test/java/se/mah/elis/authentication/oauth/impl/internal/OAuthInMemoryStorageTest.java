package se.mah.elis.authentication.oauth.impl.internal;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OAuthInMemoryStorageTest {

	private static final String ACCESS_TOKEN = "access_token";
	private static final String CLIENT_ID = "client_id";
	private static final String REDIRECT_URI = "redirect_uri";
	private static final String CODE = "code";
	private static final int TTL = 0;
	private OAuthInMemoryStorage storage; 
	
	@Before
	public void setUp() {
		storage = new OAuthInMemoryStorage();
	}
	
	@Test
	public void getAccessToken() {
		storage.setAccessTokenMap(defaultAccessTokenMap());
		assertEquals(ACCESS_TOKEN, storage.getAccessToken(CLIENT_ID));
	}
	
	@Test
	public void getAuthorizationCode() {
		storage.setAuthCodeMap(defaultAuthCodeMap());
		assertEquals(CODE, storage.getAuthorizationCode(CLIENT_ID, REDIRECT_URI));
	}
	
	@Test 
	public void getAuthorizationCodeWithNullRedirect() {
		Map<OAuthCodeKey, String> map = new HashMap<OAuthCodeKey, String>();
		OAuthCodeKey key = OAuthCodeKey.createKey(CLIENT_ID, "");
		map.put(key, CODE);
		storage.setAuthCodeMap(map);
		assertEquals(CODE, storage.getAuthorizationCode(CLIENT_ID, null));
	}
	
	@Test
	public void getAuthorizationCodeWithEmptyRedirect() {
		Map<OAuthCodeKey, String> map = new HashMap<OAuthCodeKey, String>();
		OAuthCodeKey key = OAuthCodeKey.createKey(CLIENT_ID, "");
		map.put(key, CODE);
		storage.setAuthCodeMap(map);
		assertEquals(CODE, storage.getAuthorizationCode(CLIENT_ID, ""));
	}
	
	@Test
	public void getNonexistingAuthorizationCode() {
		storage.setAuthCodeMap(defaultAuthCodeMap());
		assertNull(storage.getAuthorizationCode(CLIENT_ID, "RANDOM URI STRING"));
	}
	
	@Test
	public void removeAccessToken() {
		storage.setAccessTokenMap(defaultAccessTokenMap());
		assertEquals(1, storage.getAccessTokensMap().size());
		storage.removeAccessToken(CLIENT_ID);
		assertEquals(0, storage.getAccessTokensMap().size());
		assertNull(storage.getAccessToken(CLIENT_ID));
	}
	
	@Test
	public void removeAuthorizationCode() {
		storage.setAuthCodeMap(defaultAuthCodeMap());
		assertEquals(1, storage.getAuthorizationCodeMap().size());
		storage.removeAuthorizationCode(CLIENT_ID, REDIRECT_URI);
		assertEquals(0, storage.getAuthorizationCodeMap().size());
		assertNull(storage.getAuthorizationCode(CLIENT_ID, REDIRECT_URI));
	}
	
	@Test
	public void storeAccessToken() {
		storage.storeAccessToken(CLIENT_ID, ACCESS_TOKEN, TTL);
		assertEquals(1, storage.getAccessTokensMap().size());
		assertTrue(storage.getAccessTokensMap().containsKey(CLIENT_ID));
	}
	
	@Test
	public void storeAccessTokenWhenClientIdIsEmpty() {
		storage.storeAccessToken("", ACCESS_TOKEN, TTL);
		assertEquals(0, storage.getAccessTokensMap().size());
	}
	
	@Test
	public void storeAccessTokenWhenClientIdIsNull() {
		storage.storeAccessToken(null, ACCESS_TOKEN, TTL);
		assertEquals(0, storage.getAccessTokensMap().size());
	}
	
	@Test
	public void storeAccessTokenWhenTokenIsEmpty() {
		storage.storeAccessToken(CLIENT_ID, "", TTL);
		assertEquals(0, storage.getAccessTokensMap().size());
	}
	
	@Test
	public void storeAccessTokenWhenTokenIsNull() {
		storage.storeAccessToken(CLIENT_ID, null, TTL);
		assertEquals(0, storage.getAccessTokensMap().size());
	}
	
	@Test
	public void storeAuthorizationCode() {
		storage.storeAuthorizationCode(CLIENT_ID, REDIRECT_URI, CODE);
		assertEquals(1, storage.getAuthorizationCodeMap().size());
		
		OAuthCodeKey key = OAuthCodeKey.createKey(CLIENT_ID, REDIRECT_URI);
		assertTrue(storage.getAuthorizationCodeMap().containsKey(key));
	}
	
	@Test
	public void storeAuthorizationCodeWhenClientIdIsEmpty() {
		storage.storeAuthorizationCode("", REDIRECT_URI, CODE);
		assertEquals(0, storage.getAuthorizationCodeMap().size());
	}
	
	@Test
	public void storeAuthorizationCodeWhenClientIdIsNull() {
		storage.storeAuthorizationCode(null, REDIRECT_URI, CODE);
		assertEquals(0, storage.getAuthorizationCodeMap().size());
	}
	
	@Test
	public void storeAuthorizationCodeWhenRedirectIsEmpty() {
		storage.storeAuthorizationCode(CLIENT_ID, "", CODE);
		assertEquals(1, storage.getAuthorizationCodeMap().size()); // this is allowed
	}
	
	@Test
	public void storeAuthorizationCodeWhenRedirectNull() {
		storage.storeAuthorizationCode(CLIENT_ID, null, CODE);
		assertEquals(1, storage.getAuthorizationCodeMap().size()); // this is allowed
	}
	
	@Test
	public void storeAuthorizationCodeWhenCodeIsEmpty() {
		storage.storeAuthorizationCode(CLIENT_ID, REDIRECT_URI, "");
		assertEquals(0, storage.getAuthorizationCodeMap().size());
	}
	
	@Test
	public void storeAuthorizationCodeWhenCodeNull() {
		storage.storeAuthorizationCode(CLIENT_ID, REDIRECT_URI, null);
		assertEquals(0, storage.getAuthorizationCodeMap().size());
	}
	
	private Map<OAuthCodeKey, String> defaultAuthCodeMap() {
		Map<OAuthCodeKey, String> map = new HashMap<OAuthCodeKey, String>();
		OAuthCodeKey key = OAuthCodeKey.createKey(CLIENT_ID, REDIRECT_URI);
		map.put(key, CODE);
		return map;
	}
	
	private Map<String, String> defaultAccessTokenMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(CLIENT_ID, ACCESS_TOKEN);
		return map;
	}
}
