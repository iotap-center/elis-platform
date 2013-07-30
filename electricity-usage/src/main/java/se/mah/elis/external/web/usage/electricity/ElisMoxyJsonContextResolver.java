package se.mah.elis.external.web.usage.electricity;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;

@Provider
public class ElisMoxyJsonContextResolver implements ContextResolver<MoxyJsonConfig> {
	private MoxyJsonConfig config;
	
	public ElisMoxyJsonContextResolver() {
		config = new MoxyJsonConfig()
			.property(JAXBContextProperties.JSON_INCLUDE_ROOT, true);
	}

	public MoxyJsonConfig getContext(Class<?> clazz) {
		return config;
	}	
	
}
