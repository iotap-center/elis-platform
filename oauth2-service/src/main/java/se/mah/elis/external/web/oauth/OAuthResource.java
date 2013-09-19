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
import javax.ws.rs.core.UriBuilder;
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
	private static final String invalidClientIdEnvelope = ""
			+ "elisApi({\n"
			+ "  \"status\": \"ERROR\",\n"
			+ "  \"code\": \"400\",\n"
			+ "  \"errorType\": \"invalid_request\",\n"
			+ "  \"errorDetail\": \"Client ID is empty, incorrectly formatted or does not exist.\",\n"
			+ "  \"response\": {}"
			+ "})";
	private static final String invalidRedirectURIEnvelope = ""
			+ "elisApi({\n"
			+ "  \"status\": \"ERROR\",\n"
			+ "  \"code\": \"400\",\n"
			+ "  \"errorType\": \"invalid_request\",\n"
			+ "  \"errorDetail\": \"Redirect URI is empty or incorrectly formatted.\",\n"
			+ "  \"response\": {}"
			+ "})";
	private static final String internalErrorOAuthServiceNotAvailableEnvelope = ""
			+ "elisApi({\n"
			+ "  \"status\": \"ERROR\",\n"
			+ "  \"code\": \"500\",\n"
			+ "  \"errorType\": \"server_error\",\n"
			+ "  \"errorDetail\": \"The OAuth service is not available.\",\n"
			+ "  \"response\": {}"
			+ "})";
	private static final String forbiddenAuthorizationCodeEnvelope = ""
			+ "elisApi({\n"
			+ "  \"status\": \"ERROR\",\n"
			+ "  \"code\": \"403\",\n"
			+ "  \"errorType\": \"access_denied\",\n"
			+ "  \"errorDetail\": \"The client secret is not valid for this client id.\",\n"
			+ "  \"response\": {}"
			+ "})";
	private static final String invalidClientSecretEnvelope = ""
			+ "elisApi({\n"
			+ "  \"status\": \"ERROR\",\n"
			+ "  \"code\": \"400\",\n"
			+ "  \"errorType\": \"invalid_request\",\n"
			+ "  \"errorDetail\": \"The client secret is empty or incorrectly formatted.\",\n"
			+ "  \"response\": {}"
			+ "})"; 
	private static String accessTokenEnvelope = ""
			+ "elisApi({\n"
			+ "  \"status\": \"OK\",\n"
			+ "  \"code\": \"200\",\n"
			+ "  \"response\": {\"access_token\": \"%s\"}\n"
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
			response = handleAuthenticate(clientId, redirectUri);
		else {
			if (!isValidClientId(clientId))
				response = generateInvalidClientId();
			else if (!isValidRedirectUri(redirectUri)) 
				response = generateInvalidRedirectUri();
		}
		
		return response;
	}

	private Response handleAuthenticate(String clientId, String redirectUri) {
		Response response = null;
		if (oauthService != null) {
			String authCode = oauthService.createAuthorizationCode(clientId);
			String redirectUriWithCode = createRedirectWithCode(redirectUri, authCode); 
			response = Response.status(Status.FOUND)
					.header("Location", redirectUriWithCode.toString())
					.build();
		} else
			response = generateOAuthServiceError();
			
		return response;
	}

	private String createRedirectWithCode(String redirectUri, String authCode) {
		return UriBuilder.fromUri(redirectUri)
				.queryParam("code", authCode)
				.toTemplate();
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
		Response response;
		
		if (isValidClientId(clientId) 
			&& isValidRedirectUri(redirectUri)
			&& isValidClientSecret(clientAuthorizationCode)) {
			response = createAccessTokenResponse(clientId, 
					clientAuthorizationCode, redirectUri);
		} else {
			response = generateErrorResponse(clientId, 
					clientAuthorizationCode, redirectUri);
		}
		
		return response;
	}

	private Response createAccessTokenResponse(String clientId, 
			String clientAuthorizationCode, String redirectUri) {
		Response response; 
		if (oauthService != null) {
			response = doCreateAccessTokenResponse(clientId, 
					clientAuthorizationCode, redirectUri);
		} else {
			response = generateOAuthServiceError();
		}
		return response;
	}
	
	private Response doCreateAccessTokenResponse(String clientId, 
			String clientAuthorizationCode,	String redirectUri) {
		Response response; 
		if (oauthService.verifyAuthorizationCode(clientId, redirectUri, 
				clientAuthorizationCode)) {
			String accessTokenJsonResponse = String.format(accessTokenEnvelope, 
					oauthService.createAccessToken(clientId, clientAuthorizationCode)); 
			response = Response.ok(accessTokenJsonResponse).build();
		} else {
			response = generateForbiddenClientSecretError(); 
		}
		return response;
	}

	private Response generateErrorResponse(String clientId,
			String clientAuthorizationCode, String redirectUri) {
		Response response;
		
		// figure out what the error is and generate the appropriate response
		if (!isValidClientId(clientId)) 
			response = generateInvalidClientId();
		else if (!isValidRedirectUri(redirectUri))
			response = generateInvalidRedirectUri();
		else if (!isValidClientSecret(clientAuthorizationCode))
			response = generateInvalidClientSecretError();
		else
			response = generateOAuthServiceError(); 
		
		return response;
	}
	
	/*
	 * Helper methods
	 */
	private boolean isValidRedirectUri(String redirectUri) {
		boolean isValid = true;
		
		try {
			URI uri = new URI(redirectUri);
			uri.toURL();
		} catch (MalformedURLException 
				| URISyntaxException 
				| IllegalArgumentException e) {
			isValid = false;
		}
		
		return isValid;
	}

	private boolean isValidClientId(String clientId) {
		return !clientId.isEmpty();
	}
	
	private boolean isValidClientSecret(String clientAuthorizationCode) {
		return !clientAuthorizationCode.isEmpty();
	}

	/*
	 * Error generator messages
	 */
	private Response generateInvalidClientSecretError() {
		return Response.status(Status.BAD_REQUEST)
				.entity(invalidClientSecretEnvelope)
				.build();
	}

	private Response generateForbiddenClientSecretError() {
		Response response = Response.status(Status.FORBIDDEN)
				.entity(forbiddenAuthorizationCodeEnvelope)
				.build();
		return response;
	}
	
	private Response generateOAuthServiceError() {
		Response response = Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(internalErrorOAuthServiceNotAvailableEnvelope)
				.build();
		return response;
	}

	private Response generateInvalidRedirectUri() {
		Response response = Response.status(Status.BAD_REQUEST)
				.entity(invalidRedirectURIEnvelope)
				.build();
		return response;
	}

	private Response generateInvalidClientId() {
		Response response = Response.status(Status.BAD_REQUEST)
				.entity(invalidClientIdEnvelope)
				.build();
		return response;
	}
}
