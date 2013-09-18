package se.mah.elis.external.web.oauth;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/oauth2")
@Produces("application/json")
@Consumes("application/json")
public class OAuthResource {

	@Path("/authenticate")
	public Response authenticate(String clientId, String redirectUri) {
		return null;
	}

	@Path("/access_token")
	public Response accessToken(String clientId,
			String clientAuthorizationCode, String redirectUri) {
		return null;
	}
}
