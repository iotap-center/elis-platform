package se.mah.elis.console;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.osgi.service.log.LogService;

import se.mah.elis.services.users.UserService;
import se.mah.elis.services.users.factory.UserFactory;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(properties={
		CommandProcessor.COMMAND_SCOPE + ":String=elis",
		CommandProcessor.COMMAND_FUNCTION + ":String=batman",
		CommandProcessor.COMMAND_FUNCTION + ":String=listloggers",
		CommandProcessor.COMMAND_FUNCTION + ":String=listuserservices",
		CommandProcessor.COMMAND_FUNCTION + ":String=listuserfactories"
	},
	provide = ConsoleCommands.class)
public class ConsoleCommands {
	
	private Collection<LogService> loggers;
	private Collection<UserService> userServices;
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
	
	
	@Reference(name="loggers", type='*')
	public void addLogger(LogService l) {
		loggers.add(l);
	}

	public void removeLogger(LogService l) {
		loggers.remove(l);
	}

	@Reference(name="userServices", type='*')
	public void addUserService(UserService us) {
		System.out.println("Adding user service");
		userServices.add(us);
	}
	
	public void removeUserService(UserService us) {
		userServices.remove(us);
	}

	@Reference(name="userFactories", type='*')
	public void addUserFactory(UserFactory uf) {
		System.out.println("Adding user factory");
		userFactories.add(uf);
	}
	
	public void removeUserFactory(UserFactory uf) {
		userFactories.remove(uf);
	}
}
