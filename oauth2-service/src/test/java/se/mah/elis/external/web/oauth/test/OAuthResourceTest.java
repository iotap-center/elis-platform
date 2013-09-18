package se.mah.elis.external.web.oauth.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;

import se.mah.elis.external.web.oauth.OAuthResource;

public class OAuthResourceTest {

	// test variables
	private static final String TEST_CLIENTID = "batman";
	private static final String TEST_REDIRECT_URI = "http://batcave.mah.se/redirect.php";
	
	// internals
	private OAuthResource oauth;
	
	@Before
	public void setUp() {
		oauth = new OAuthResource();
	}
	
	@Test
	public void testAuthenticateSuccess() {
		Response response = oauth.authenticate(TEST_CLIENTID, TEST_REDIRECT_URI);
		assertEquals(Status.FOUND, response.getStatus());
		
		String authCode = response.getHeaderString("Elis-Authorization-Code");
		String redirectTo = response.getHeaderString("Location");
		
		assertNotNull(authCode);
		assertTrue(!authCode.isEmpty());
		
		assertNotNull(redirectTo);
		assertEquals(TEST_REDIRECT_URI, redirectTo);
	}
	
	@Test 
	public void testAuthenticateClientIdInvalid() {
		Response response = oauth.authenticate("", TEST_REDIRECT_URI);
		assertEquals(Status.BAD_REQUEST , response.getStatus());
		String body = response.readEntity(String.class);
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
		assertEquals(Status.BAD_REQUEST , response.getStatus());
		String body = response.readEntity(String.class);
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
		
	}
	
	@Test
	public void testAccessTokenSuccess() {
		
	}
	
	@Test
	public void testAccessTokenMissingParams() {
		
	}
	
	@Test
	public void testAccessTokenMismatchClientIdAndClientSecret() {
		
	}
	
	@Test
	public void testAccessTokenInvalidRedirectURI() {
		
	}
}
