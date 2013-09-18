package se.mah.elis.external.web.oauth;

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
	
	@GET
	@Path("/authenticate")
	public Response authenticate(
			@DefaultValue("") @QueryParam("client_id") String clientId, 
			@DefaultValue("") @QueryParam("redirect_uri") String redirectUri) {
		Response response = null;
		
		if (!clientId.isEmpty() && !redirectUri.isEmpty())
			response = handle_authenticate(clientId, redirectUri);
		else {
			if (clientId.isEmpty())
				response = invalidClientId();
			else if (redirectUri.isEmpty()) 
				response = invalidRedirectUri();
		}
		
		return response;
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

	private Response invalidRedirectUri() {
		Response response = Response.status(Status.BAD_REQUEST)
				.entity(invalidRedirectURI)
				.build();
		return response;
	}

	private Response invalidClientId() {
		Response response = Response.status(Status.BAD_REQUEST)
				.entity(invalidClientId)
				.build();
		return response;
	}

	@GET
	@Path("/access_token")
	public Response accessToken(String clientId,
			String clientAuthorizationCode, String redirectUri) {
		return null;
	}
}
