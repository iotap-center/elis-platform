package se.mah.elis.adaptor.energy.eon;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

import se.mah.elis.adaptor.energy.eon.internal.user.EonUserProvider;
import se.mah.elis.services.users.factory.UserFactory;
import se.mah.elis.services.users.factory.UserProvider;

@Component(name = "se.mah.elis.adaptor.energy.eon", description = "Eon Adaptor", immediate = true)
@Service(value = {EonAdaptor.class, ManagedService.class})
public class EonAdaptor implements ManagedService {

	// properties
	public static String SERVICE_PID = "se.mah.elis.adaptor.energy.eon";
	public static String TARGET_HOST = "host";
	public static String TARGET_PORT = "port";
	public static String TARGET_APIPREFIX = "prefix";
	
	public static Dictionary<String, ?> properties = getDefaultConfiguration();

	@Reference
	private LogService log;
	
	@Reference
	private UserFactory userFactoryService;
	
	@Reference
	private ConfigurationAdmin configAdmin;
	
	private UserProvider eonProvider;
	
	public EonAdaptor() {
		eonProvider = new EonUserProvider();
	}
	
	protected void bindUserFactoryService(UserFactory uf) {
		logThis("Binding to user factory " + uf.toString());
		this.userFactoryService = uf;
		userFactoryService.registerProvider(eonProvider);
	}
	
	protected void unbindUserFactoryService(UserFactory uf) {
		userFactoryService.unregisterProvider(eonProvider);
		this.userFactoryService = null;
	}
	
	protected void bindLog(LogService ls) {
		log = ls;
	}
	
	protected void unbindLog(LogService ls) {
		log = null;
	}
	
	protected void bindConfigAdmin(ConfigurationAdmin ca) {
		configAdmin = ca;
		setConfig();
	}
	
	protected void unbindConfigAdmin(ConfigurationAdmin ca) {
		configAdmin = null;
	}

	private void setConfig() {
		try {
			Configuration config = configAdmin.getConfiguration(SERVICE_PID);
			properties = config.getProperties();
			if (properties == null) {
				setDefaultConfiguration();
				config.update(properties);
			}
		} catch (IOException e) {
			logThis(LogService.LOG_ERROR, "Failed to get configuration from Configuration Admin service.");
			setDefaultConfiguration();
		} finally {
			logThis("Installed configuration");
		}
		
	}

	private void setDefaultConfiguration() {
		if (properties == null) {
			properties = getDefaultConfiguration();
			logThis("Using default configuration.");
		}
	}

	@Override
	public void updated(Dictionary<String, ?> props)
			throws ConfigurationException {
		if (props != null) {
			properties = props;
			// TODO: restart existing instances of HTTP bridges
			logThis("Updated configuration: " + properties.toString());
		}
	}

	private static Dictionary<String, ?> getDefaultConfiguration() {
		Dictionary<String, Object> props = new Hashtable<>();
		props.put(TARGET_HOST, "https://smarthome.eon.se");
		props.put(TARGET_PORT, 443);
		props.put(TARGET_APIPREFIX, "/v0_2/api/");
		return props;
	}
	
	private void logThis(String msg) {
		logThis(LogService.LOG_INFO, msg);
	}

	private void logThis(int level, String msg) {
		if (log != null) 
			log.log(level, msg);
	}
}
