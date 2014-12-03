package se.mah.elis.external.beans.helpers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;

import se.mah.elis.external.beans.EnvelopeBean;
import se.mah.elis.external.beans.ErrorBean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * <p>This class provides methods for building a number of pre-defined HTTP
 * response messages. Currently, the following status codes are available:</p>
 * 
 * <ul>
 *   <li>200 OK</li>
 *   <li>201 Created</li>
 *   <li>204 No Content</li>
 *   <li>400 Bad Request</li>
 *   <li>403 Forbidden</li>
 *   <li>404 Not Found</li>
 *   <li>405 Method Not Allowed</li>
 *   <li>409 Conflict</li>
 *   <li>418 I'm a teapot</li>
 *   <li>500 Internal Server Error</li>
 *   <li>501 Not Implemented</li>
 *   <li>503 Service Unavailable</li>
 * </ul>
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public class ElisResponseBuilder {
	
	/**
	 * Builds a 200 OK response.
	 * 
	 * @param payload An object to send with the response.
	 * @return A 200 response object.
	 * @since 1.0
	 */
	public static Response buildOKResponse(Object payload) {
		Response response = null;
		Gson gson = buildGsonWithAdapters();
		EnvelopeBean envelope = new EnvelopeBean();
		envelope.status = Status.OK.getReasonPhrase();
		envelope.code = Status.OK.getStatusCode();
		envelope.response = payload;
		response = Response.status(Status.OK)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
	
	/**
	 * Builds a 201 OK response.
	 * 
	 * @return A 201 response object.
	 * @since 1.0
	 */
	public static Response buildCreatedResponse() {
		Response response = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new EnvelopeBean();
		envelope.status = Status.CREATED.getReasonPhrase();
		envelope.code = Status.CREATED.getStatusCode();
		response = Response.status(Status.CREATED)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
	
	/**
	 * Builds a 204 No Content response.
	 * 
	 * @return A 204 response object.
	 * @since 1.0
	 */
	public static Response buildNoContentResponse() {
		Response response = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = Status.NO_CONTENT.getReasonPhrase();
		envelope.code = Status.NO_CONTENT.getStatusCode();
		response = Response.status(Status.NO_CONTENT)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
	
	/**
	 * Builds a 400 Bad Request response.
	 * 
	 * @return A 400 response object.
	 * @since 1.0
	 */
	public static Response buildBadRequestResponse() {
		Response response = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.BAD_REQUEST.getStatusCode();
		((ErrorBean) envelope).errorType = Status.BAD_REQUEST
				.getReasonPhrase();
		((ErrorBean) envelope).errorDetail =
				"The request cannot be fulfilled due to bad syntax.";
		response = Response.status(Status.BAD_REQUEST)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
	
	/**
	 * Builds a 403 Forbidden response.
	 * 
	 * @return A 403 response object.
	 * @since 1.0
	 */
	public static Response buildForbiddenResponse() {
		Response response = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.FORBIDDEN.getStatusCode();
		((ErrorBean) envelope).errorType = Status.FORBIDDEN.getReasonPhrase();
		((ErrorBean) envelope).errorDetail = "You were not allowed to access this resource.";
		response = Response.status(Status.FORBIDDEN)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
	
	/**
	 * Builds a 404 Not Found response.
	 * 
	 * @return A 404 response object.
	 * @since 1.0
	 */
	public static Response buildNotFoundResponse() {
		Response response = null;
		Gson gson = buildGsonWithAdapters(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.NOT_FOUND.getStatusCode();
		((ErrorBean) envelope).errorType = Status.NOT_FOUND.getReasonPhrase();
		((ErrorBean) envelope).errorDetail = "The requested URL was not found on this server.";
		response = Response.status(Status.NOT_FOUND)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
	
	/**
	 * Builds a 405 Method Not Allowed response.
	 * 
	 * @return A 405 response object.
	 * @since 1.1
	 */
	public static Response buildMethodNotAllowedResponse() {
		Response response = null;
		Gson gson = buildGsonWithAdapters(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.METHOD_NOT_ALLOWED.getStatusCode();
		((ErrorBean) envelope).errorType = Status.METHOD_NOT_ALLOWED.getReasonPhrase();
		((ErrorBean) envelope).errorDetail = "The requested method was not allowed for this object.";
		response = Response.status(Status.METHOD_NOT_ALLOWED)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}

	/**
	 * Builds a 409 Conflict response.
	 * 
	 * @return A 409 response object.
	 * @since 1.0
	 */
	public static Response buildConflictResponse() {
		Response response = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.CONFLICT.getStatusCode();
		((ErrorBean) envelope).errorType = Status.CONFLICT.getReasonPhrase();
		((ErrorBean) envelope).errorDetail = "The proposed URL already exists on this server.";
		response = Response.status(Status.CONFLICT)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}

	/**
	 * Builds a 418 I'm a teapot response.
	 * 
	 * @return A 418 response object.
	 * @since 1.0
	 */
	public static Response buildImATeapotResponse() {
		Response response = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Not what you expected";
		envelope.code = 418;
		((ErrorBean) envelope).errorType = "I'm a teapot";
		((ErrorBean) envelope).errorDetail = "The resource you're requesting is, in fact, a teapot.";
		response = Response.status(418)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}

	/**
	 * Builds a 500 Internal Server Error response.
	 * 
	 * @return A 500 response object.
	 * @since 1.0
	 */
	public static Response buildInternalServerErrorResponse() {
		Response response = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		((ErrorBean) envelope).errorType = Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
		((ErrorBean) envelope).errorDetail = "Someone set up us the bomb.";
		response = Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}

	/**
	 * Builds a 501 Not Implemented response.
	 * 
	 * @return A 501 response object.
	 * @since 1.0
	 */
	public static Response buildNotImplementedResponse() {
		Response response = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.NOT_IMPLEMENTED.getStatusCode();
		((ErrorBean) envelope).errorType = Status.NOT_IMPLEMENTED.getReasonPhrase();
		((ErrorBean) envelope).errorDetail = "This method isn't implemented.";
		response = Response.status(Status.NOT_IMPLEMENTED)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}

	/**
	 * Builds a 503 Service Unavailable response.
	 * 
	 * @return A 503 response object.
	 * @since 1.1
	 */
	public static Response buildServiceUnavailableResponse() {
		Response response = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		EnvelopeBean envelope = new ErrorBean();
		envelope.status = "Error";
		envelope.code = Status.SERVICE_UNAVAILABLE.getStatusCode();
		((ErrorBean) envelope).errorType = Status.SERVICE_UNAVAILABLE.getReasonPhrase();
		((ErrorBean) envelope).errorDetail = "The requested object is currently unavailable.";
		response = Response.status(Status.SERVICE_UNAVAILABLE)
				.entity(gson.toJson(envelope)).build();
		
		return response;
	}
	
	/**
	 * Builds a Gson object.
	 * 
	 * @return A Gson object.
	 * @since 1.0
	 */
	private static Gson buildGsonWithAdapters() {
		return new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(DateTime.class, new DateTimeAdapter())
				.create();
	}

}
