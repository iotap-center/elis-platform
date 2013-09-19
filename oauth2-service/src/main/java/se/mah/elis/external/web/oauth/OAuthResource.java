package se.mah.elis.external.web.oauth;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import se.mah.elis.authentication.oauth.OAuthService;

/**
 * Elis HTTP end-point for OAuth authentication for clients and applications
 * 
 * @author Marcus Ljungblad
 * @version 1.0.0
 * @since 1.0
 */
@Path("/oauth2")
@Produces("application/json")
@Consumes("application/json")
public class OAuthResource {

	// error envelopes 
	private static final String invalidClientId = ""
			+ "elisApi({\n"
			+ "  \"status\": \"ERROR\",\n"
			+ "  \"code\": \"400\",\n"
			+ "  \"errorType\": \"invalid client id\",\n"
			+ "  \"errorDetail\": \"Client ID is empty, incorrectly formatted or does not exist.\",\n"
			+ "  \"response\": {}"
			+ "})";
	private static final String invalidRedirectURI = ""
			+ "elisApi({\n"
			+ "  \"status\": \"ERROR\",\n"
			+ "  \"code\": \"400\",\n"
			+ "  \"errorType\": \"invalid redirect uri\",\n"
			+ "  \"errorDetail\": \"Redirect URI is empty or incorrectly formatted.\",\n"
			+ "  \"response\": {}"
			+ "})";
	private static final Object internalErrorOAuthServiceNotAvailable = ""
			+ "elisApi({\n"
			+ "  \"status\": \"ERROR\",\n"
			+ "  \"code\": \"500\",\n"
			+ "  \"errorType\": \"platform error\",\n"
			+ "  \"errorDetail\": \"The OAuth service is not available.\",\n"
			+ "  \"response\": {}"
			+ "})";
	
	// internals
	private OAuthService oauthService;

	public OAuthResource(OAuthService oauthService) {
		this.oauthService = oauthService;
	}
	
	public void setOAuthService(OAuthService oauthService) {
		this.oauthService = oauthService;
	}
	
	/**
	 * HTTP End-point for apps to acquire an authorization code as the first 
	 * step of the OAuth 2.0 authentication process.
	 *  
	 * @param clientId
	 * @param redirectUri
	 * @return Response with authorization code in 'Elis-Authorization-Code' and 
	 * 	redirect to the specified 'Location' header
	 */
	@GET
	@Path("/authenticate")
	public Response authenticate(
			@DefaultValue("") @QueryParam("client_id") String clientId, 
			@DefaultValue("") @QueryParam("redirect_uri") String redirectUri) {
		Response response = null;
		
		if (isValidClientId(clientId) && isValidRedirectUri(redirectUri))
			response = handle_authenticate(clientId, redirectUri);
		else {
			if (!isValidClientId(clientId))
				response = generateInvalidClientId();
			else if (!isValidRedirectUri(redirectUri)) 
				response = generateInvalidRedirectUri();
		}
		
		return response;
	}

	private boolean isValidRedirectUri(String redirectUri) {
		boolean isValid = true;
		
		try {
			URI uri = new URI(redirectUri);
			uri.toURL();
		} catch (MalformedURLException | URISyntaxException e) {
			isValid = false;
		}
		
		return isValid;
	}

	private boolean isValidClientId(String clientId) {
		return !clientId.isEmpty();
	}

	private Response handle_authenticate(String clientId, String redirectUri) {
		Response response = null;
		if (oauthService != null)
			response = Response.status(Status.FOUND)
					.header("Elis-Authorization-Code", oauthService.createAuthorizationCode())
					.header("Location", redirectUri)
					.build();
		else 
			response = Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(internalErrorOAuthServiceNotAvailable)
					.build();
			
		return response;
	}

	private Response generateInvalidRedirectUri() {
		Response response = Response.status(Status.BAD_REQUEST)
				.entity(invalidRedirectURI)
				.build();
		return response;
	}

	private Response generateInvalidClientId() {
		Response response = Response.status(Status.BAD_REQUEST)
				.entity(invalidClientId)
				.build();
		return response;
	}

	/**
	 * HTTP End-point for apps to collect the OAuth 2.0 access token generated
	 * by the Elis platform. 
	 * @param clientId
	 * @param clientAuthorizationCode
	 * @param redirectUri
	 * @return Response with JSON body including the access token and header with 'Location'
	 */
	@GET
	@Path("/access_token")
	public Response accessToken(
			@DefaultValue("") @QueryParam("client_id") String clientId,
			@DefaultValue("") @QueryParam("client_secret") String clientAuthorizationCode, 
			@DefaultValue("") @QueryParam("redirect_uri") String redirectUri) {
		return null;
	}
}
