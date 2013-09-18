package se.mah.elis.external.web.oauth.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.authentication.oauth.OAuthService;
import se.mah.elis.external.web.oauth.OAuthResource;

public class OAuthResourceTest {

	// test variables
	private static final String TEST_CLIENTID = "batman";
	private static final String TEST_REDIRECT_URI = "http://batcave.mah.se/redirect.php";
	private static final String TEST_AUTHCODE = "ec0e2603172c73a8b644bb9456c1ff6e";
	private static final String TEST_ACCESSCODE = "8ee60a2e00c90d7e00d5069188dc115b";
	
	// internals
	private OAuthResource oauth;
	
	@Before
	public void setUp() {
		OAuthService oauthService = mock(OAuthService.class);
		when(oauthService.createAuthorizationCode()).thenReturn(TEST_AUTHCODE);
		when(oauthService.createAccessToken()).thenReturn(TEST_ACCESSCODE);
		oauth = new OAuthResource(oauthService);
	}
	
	@Test
	public void testAuthenticateSuccess() {
		Response response = oauth.authenticate(TEST_CLIENTID, TEST_REDIRECT_URI);
		assertEquals(Status.FOUND, Status.fromStatusCode(response.getStatus()));
		
		String authCode = response.getHeaderString("Elis-Authorization-Code");
		String redirectTo = response.getHeaderString("Location");
		
		assertNotNull(authCode);
		assertTrue(!authCode.isEmpty());
		assertEquals(TEST_AUTHCODE, authCode);
		
		assertNotNull(redirectTo);
		assertEquals(TEST_REDIRECT_URI, redirectTo);
	}
	
	@Test
	public void testOAuthServiceNotAvailableOnSuccessRequest() {
		fail();
	}
	
	@Test 
	public void testAuthenticateClientIdInvalid() {
		Response response = oauth.authenticate("", TEST_REDIRECT_URI);
		assertEquals(Status.BAD_REQUEST, Status.fromStatusCode(response.getStatus()));
		String body = (String) response.getEntity();
		String expectedBody = ""
				+ "elisApi({\n"
				+ "  \"status\": \"ERROR\",\n"
				+ "  \"code\": \"400\",\n"
				+ "  \"errorType\": \"invalid client id\",\n"
				+ "  \"errorDetail\": \"Client ID is empty, incorrectly formatted or does not exist.\","
				+ "  \"response\": {}"
				+ "})";
		assertEquals(expectedBody, body);
	}
	
	@Test 
	public void testAuthenticateInvalidRedirectURI() {
		Response response = oauth.authenticate(TEST_CLIENTID, "");
		assertEquals(Status.BAD_REQUEST, Status.fromStatusCode(response.getStatus()));
		String body = (String) response.getEntity();
		String expectedBody = ""
				+ "elisApi({\n"
				+ "  \"status\": \"ERROR\",\n"
				+ "  \"code\": \"400\",\n"
				+ "  \"errorType\": \"invalid redirect uri\",\n"
				+ "  \"errorDetail\": \"Redirect URI is empty or incorrectly formatted.\","
				+ "  \"response\": {}"
				+ "})";
		assertEquals(expectedBody, body);
	}
	
	@Test
	public void testAuthenticateMissingParams() {
		fail();
	}
	
	@Test
	public void testAccessTokenSuccess() {
		fail();
	}
	
	@Test
	public void testAccessTokenMissingParams() {
		fail();
	}
	
	@Test
	public void testAccessTokenMismatchClientIdAndClientSecret() {
		fail();
	}
	
	@Test
	public void testAccessTokenInvalidRedirectURI() {
		fail();
	}
}
