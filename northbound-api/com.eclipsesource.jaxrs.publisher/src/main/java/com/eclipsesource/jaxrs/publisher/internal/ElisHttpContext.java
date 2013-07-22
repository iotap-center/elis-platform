package com.eclipsesource.jaxrs.publisher.internal;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.http.HttpContext;

public class ElisHttpContext implements HttpContext {

	public boolean handleSecurity(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println(">>> handling security in Elis specific HttpContext");
		return true;
	}

	public URL getResource(String name) {
		return null;
	}

	public String getMimeType(String name) {
		return null;
	}

}
