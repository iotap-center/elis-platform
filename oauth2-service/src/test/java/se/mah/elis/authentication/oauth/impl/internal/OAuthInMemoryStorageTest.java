package se.mah.elis.authentication.oauth.impl.internal;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.authentication.oauth.OAuthCode;
import se.mah.elis.authentication.oauth.OAuthCodeKey;

public class OAuthInMemoryStorageTest {

	private static OAuthCode ACCESS_TOKEN;
	private static final String CLIENT_ID = "client_id";
	private static final String REDIRECT_URI = "redirect_uri";
	private static OAuthCode CODE;
	private static final int TTL = 0;
	private OAuthInMemoryStorage storage; 
	
	@Before
	public void setUp() {
		CODE = OAuthCodeImpl.create();
		ACCESS_TOKEN = OAuthCodeImpl.create();
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
		Map<OAuthCodeKey, OAuthCode> map = new HashMap<OAuthCodeKey, OAuthCode>();
		OAuthCodeKey key = OAuthCodeKeyImpl.createKey(CLIENT_ID, "");
		map.put(key, CODE);
		storage.setAuthCodeMap(map);
		assertEquals(CODE, storage.getAuthorizationCode(CLIENT_ID, null));
	}
	
	@Test
	public void getAuthorizationCodeWithEmptyRedirect() {
		Map<OAuthCodeKey, OAuthCode> map = new HashMap<OAuthCodeKey, OAuthCode>();
		OAuthCodeKey key = OAuthCodeKeyImpl.createKey(CLIENT_ID, "");
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
	public void lookupAccessToken() {
		storage.setAccessTokenMap(defaultAccessTokenMap());
		assertEquals(ACCESS_TOKEN, storage.lookupAccessToken(ACCESS_TOKEN.getCode()));
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
	public void storeAccessTokenWhenTokenIsExpired() throws InterruptedException {
		OAuthCode token = OAuthCodeImpl.create(0);
		Thread.sleep(10); // ensure token expires
		storage.storeAccessToken(CLIENT_ID, token, TTL);
		assertEquals(0, storage.getAccessTokensMap().size());
	}
	
	@Test
	public void storeAccessTokenWhenTokenIsNull() {
		storage.storeAccessToken(CLIENT_ID, null, TTL);
		assertEquals(0, storage.getAccessTokensMap().size());
	}
	
	@Test
	public void storeAuthorizationCode() {
		storage.storeAuthorizationCode(CLIENT_ID, REDIRECT_URI, CODE, TTL);
		assertEquals(1, storage.getAuthorizationCodeMap().size());
		
		OAuthCodeKey key = OAuthCodeKeyImpl.createKey(CLIENT_ID, REDIRECT_URI);
		assertTrue(storage.getAuthorizationCodeMap().containsKey(key));
	}
	
	@Test
	public void storeAuthorizationCodeWhenClientIdIsEmpty() {
		storage.storeAuthorizationCode("", REDIRECT_URI, CODE, TTL);
		assertEquals(0, storage.getAuthorizationCodeMap().size());
	}
	
	@Test
	public void storeAuthorizationCodeWhenClientIdIsNull() {
		storage.storeAuthorizationCode(null, REDIRECT_URI, CODE, TTL);
		assertEquals(0, storage.getAuthorizationCodeMap().size());
	}
	
	@Test
	public void storeAuthorizationCodeWhenRedirectIsEmpty() {
		storage.storeAuthorizationCode(CLIENT_ID, "", CODE, TTL);
		assertEquals(1, storage.getAuthorizationCodeMap().size()); // this is allowed
	}
	
	@Test
	public void storeAuthorizationCodeWhenRedirectNull() {
		storage.storeAuthorizationCode(CLIENT_ID, null, CODE, TTL);
		assertEquals(1, storage.getAuthorizationCodeMap().size()); // this is allowed
	}
	
	@Test
	public void storeAuthorizationCodeWhenCodeIsExpired() throws InterruptedException {
		OAuthCode code = OAuthCodeImpl.create(0);
		Thread.sleep(10); // sleep some to ensure it is expired
		storage.storeAuthorizationCode(CLIENT_ID, REDIRECT_URI, code, TTL);
		assertEquals(0, storage.getAuthorizationCodeMap().size());
	}
	
	@Test
	public void storeAuthorizationCodeWhenCodeNull() {
		storage.storeAuthorizationCode(CLIENT_ID, REDIRECT_URI, null, TTL);
		assertEquals(0, storage.getAuthorizationCodeMap().size());
	}
	
	private Map<OAuthCodeKey, OAuthCode> defaultAuthCodeMap() {
		Map<OAuthCodeKey, OAuthCode> map = new HashMap<OAuthCodeKey, OAuthCode>();
		OAuthCodeKey key = OAuthCodeKeyImpl.createKey(CLIENT_ID, REDIRECT_URI);
		map.put(key, CODE);
		return map;
	}
	
	private Map<String, OAuthCode> defaultAccessTokenMap() {
		Map<String, OAuthCode> map = new HashMap<String, OAuthCode>();
		map.put(CLIENT_ID, ACCESS_TOKEN);
		return map;
	}
}
