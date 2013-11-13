package se.mah.elis.console;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.osgi.service.log.LogService;

import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.factory.UserFactory;

@Component
@Service(value=ConsoleCommands.class)
@Properties({
	@Property(name = CommandProcessor.COMMAND_SCOPE, value = "elis"),
	@Property(name = CommandProcessor.COMMAND_FUNCTION,
	value = {"batman", "listloggers", "listuserservices", "listuserfactories"})
})
public class ConsoleCommands {
	
	@Reference(bind = "addLogger",
			unbind = "removeLogger",
			referenceInterface = LogService.class,
			cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE)
	private Collection<LogService> loggers;
	
	@Reference(bind = "addUserService",
			unbind = "removeUserService",
					referenceInterface = UserService.class,
			cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE)
	private Collection<UserService> userServices;
	
	@Reference(bind = "addUserFactory",
			unbind = "removeUserFactory",
					referenceInterface = UserFactory.class,
			cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE)
	private Collection<UserFactory> userFactories;
	
	public ConsoleCommands() {
		loggers = new ArrayList<LogService>();
		userServices = new ArrayList<UserService>();
		userFactories = new ArrayList<UserFactory>();
	}
	
	@Descriptor("Call the Batman")
	public void batman() {
		System.out.println("Nananana Batman!");
	}
	
	@Descriptor("List available loggers")
	public void listloggers() {
		if (loggers.size() > 0) {
			for (LogService logger : loggers) {
				System.out.println("LogService: " + logger.toString());
			}
		} else {
			System.out.println("No loggers yet");
		}
	}
	
	@Descriptor("List available user services")
	public void listuserservices() {
		if (userServices.size() > 0) {
			for (UserService userService : userServices) {
				System.out.println("UserService: " + userService.toString());
			}
		} else {
			System.out.println("No user services yet");
		}
	}
	
	@Descriptor("List available user factories")
	public void listuserfactories() {
		if (userFactories.size() > 0) {
			for (UserFactory userFactory : userFactories) {
				System.out.println("UserFactory: " + userFactory.toString());
			}
		} else {
			System.out.println("No user factories yet");
		}
	}

	protected void addLogger(LogService l) {
		loggers.add(l);
	}

	protected void removeLogger(LogService l) {
		loggers.remove(l);
	}

	protected void addUserService(UserService us) {
		System.out.println("Adding user service");
		userServices.add(us);
	}
	
	protected void removeUserService(UserService us) {
		userServices.remove(us);
	}

	protected void addUserFactory(UserFactory uf) {
		System.out.println("Adding user factory");
		userFactories.add(uf);
	}
	
	protected void removeUserFactory(UserFactory uf) {
		userFactories.remove(uf);
	}
}
