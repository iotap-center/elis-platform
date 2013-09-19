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
		OAuthService oauthService = createMockOAuthService();
		when(oauthService.verifyAuthorizationCode(TEST_AUTHCODE)).thenReturn(true);
		oauth = new OAuthResource(oauthService);
	}

	private OAuthService createMockOAuthService() {
		OAuthService oauthService = mock(OAuthService.class);
		when(oauthService.createAuthorizationCode()).thenReturn(TEST_AUTHCODE);
		when(oauthService.createAccessToken()).thenReturn(TEST_ACCESSCODE);
		return oauthService;
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
	public void testAuthenticateOAuthServiceNotAvailable() {
		oauth.setOAuthService(null);
		Response response = oauth.authenticate(TEST_CLIENTID, TEST_REDIRECT_URI);
		testOAuthServiceNotAvailable(response);
	}
	
	@Test 
	public void testAuthenticateClientIdInvalid() {
		Response response = oauth.authenticate("", TEST_REDIRECT_URI);
		testInvalidClientId(response);
	}
	
	@Test 
	public void testAuthenticateEmptyRedirectURI() {
		Response response = oauth.authenticate(TEST_CLIENTID, "");
		testInvalidRedirectURI(response);
	}
	
	@Test
	public void testAuthenticateWithInvalidRedirectURIBrokenProtocol() {
		Response response = oauth.authenticate(TEST_CLIENTID, "htasdfsdfp://www.joker.com");
		testInvalidRedirectURI(response);
	}
	
	@Test
	public void testAuthenticateWithInvalidRedirectURIBrokenPath() {
		Response response = oauth.authenticate(TEST_CLIENTID, "http:// some strange");
		testInvalidRedirectURI(response);
	}
	
	@Test
	public void testAuthenticateWithValidRedirectURIComplexPath() {
		Response response = oauth.authenticate(TEST_CLIENTID, 
				"http://mah.se/target.php?param=test");
		assertEquals(Status.FOUND, Status.fromStatusCode(response.getStatus()));
	}
	
	@Test
	public void testAccessTokenSuccess() {
		Response response = oauth.accessToken(TEST_CLIENTID, TEST_AUTHCODE, 
				TEST_REDIRECT_URI);
		assertEquals(Status.OK, Status.fromStatusCode(response.getStatus()));
		
		String body = (String) response.getEntity();
		String expectedBody = ""
				+ "elisApi({\n"
				+ "  \"status\": \"OK\",\n"
				+ "  \"code\": \"200\",\n"
				+ "  \"response\": {\"access_token\": \"" + TEST_ACCESSCODE + "\"}\n"
				+ "})";
		assertEquals(expectedBody, body);
	}
	
	@Test
	public void testAccessTokenOAuthServiceNotAvailable() {
		oauth.setOAuthService(null);
		Response response = oauth.accessToken(TEST_CLIENTID, TEST_AUTHCODE, 
				TEST_REDIRECT_URI);
		testOAuthServiceNotAvailable(response);
	}

	private void testOAuthServiceNotAvailable(Response response) {
		assertEquals(Status.INTERNAL_SERVER_ERROR, Status.fromStatusCode(response.getStatus()));
		String body = (String) response.getEntity();
		String expectedBody = ""
				+ "elisApi({\n"
				+ "  \"status\": \"ERROR\",\n"
				+ "  \"code\": \"500\",\n"
				+ "  \"errorType\": \"platform error\",\n"
				+ "  \"errorDetail\": \"The OAuth service is not available.\",\n"
				+ "  \"response\": {}"
				+ "})";
		assertEquals(expectedBody, body);
	}
	
	@Test
	public void testAccessTokenEmptyRedirectURI() {
		Response response = oauth.accessToken(TEST_CLIENTID, TEST_AUTHCODE, "");
		testInvalidRedirectURI(response);
	}
	
	@Test 
	public void testAccessTokenInvalidClientId() {
		Response response = oauth.accessToken("", TEST_AUTHCODE, TEST_REDIRECT_URI);
		testInvalidClientId(response);
	}
	
	@Test 
	public void testAccessTokenNotAuthorizedClientSecret() {
		// make sure service denies auth code
		OAuthService service = createMockOAuthService();
		when(service.verifyAuthorizationCode(TEST_AUTHCODE)).thenReturn(false);
		OAuthResource oauthResource = new OAuthResource(service);
		
		// test the response
		Response response = oauthResource.accessToken(TEST_CLIENTID, "not_matching", 
				TEST_REDIRECT_URI);
		assertEquals(Status.FORBIDDEN, Status.fromStatusCode(response.getStatus()));
		String body = (String) response.getEntity();
		String expectedBody = ""
				+ "elisApi({\n"
				+ "  \"status\": \"ERROR\",\n"
				+ "  \"code\": \"403\",\n"
				+ "  \"errorType\": \"unauthorized\",\n"
				+ "  \"errorDetail\": \"The client secret is not valid for this client id.\",\n"
				+ "  \"response\": {}"
				+ "})"; 
		assertEquals(expectedBody, body);
	}
	
	@Test
	public void testAccessTokenInvalidClientSecret() {
		Response response = oauth.accessToken(TEST_CLIENTID, "", 
				TEST_REDIRECT_URI);
		assertEquals(Status.BAD_REQUEST, Status.fromStatusCode(response.getStatus()));
		String body = (String) response.getEntity();
		String expectedBody = ""
				+ "elisApi({\n"
				+ "  \"status\": \"ERROR\",\n"
				+ "  \"code\": \"400\",\n"
				+ "  \"errorType\": \"invalid client secret\",\n"
				+ "  \"errorDetail\": \"The client secret is empty or incorrectly formatted.\",\n"
				+ "  \"response\": {}"
				+ "})"; 
		assertEquals(expectedBody, body);
	}

	private void testInvalidClientId(Response response) {
		assertEquals(Status.BAD_REQUEST, Status.fromStatusCode(response.getStatus()));
		String body = (String) response.getEntity();
		String expectedBody = ""
				+ "elisApi({\n"
				+ "  \"status\": \"ERROR\",\n"
				+ "  \"code\": \"400\",\n"
				+ "  \"errorType\": \"invalid client id\",\n"
				+ "  \"errorDetail\": \"Client ID is empty, incorrectly formatted or does not exist.\",\n"
				+ "  \"response\": {}"
				+ "})";
		assertEquals(expectedBody, body);
	}

	private void testInvalidRedirectURI(Response response) {
		assertEquals(Status.BAD_REQUEST, Status.fromStatusCode(response.getStatus()));
		String body = (String) response.getEntity();
		String expectedBody = ""
				+ "elisApi({\n"
				+ "  \"status\": \"ERROR\",\n"
				+ "  \"code\": \"400\",\n"
				+ "  \"errorType\": \"invalid redirect uri\",\n"
				+ "  \"errorDetail\": \"Redirect URI is empty or incorrectly formatted.\",\n"
				+ "  \"response\": {}"
				+ "})";
		assertEquals(expectedBody, body);
	}
}
