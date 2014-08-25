# The Elis Platform

This project focuses on exploring the potential of mobile services on mobile devices (e.g. smart phones and tablets) to promote energy efficiency in existing buildings. Read more about the background [here](http://elis.mah.se).

Other documents also worth checking out: 

* [Build Guide](super/readme.md)
* [Deployment Guide](Deployment%20guide.md)

The rest of this document describes aspects useful while working on the implementation of Elis. 

## Terminology

* Bundle - a module that is installable in the Felix runtime
* Provider - a bundle which communicates with, for example, E.On and conforms to the Elis APIs

## Version management

**Releases**

Follows the pattern `major.minor.patch`. _major_ is used when significant new releases are made, e.g., several new APIs are added to the platform. _minor_ is used for smaller updates, e.g., improved performance or small features. _patch_ is used for hotfixes. 

**Bundles**

Follows the pattern `major.minor.patch`. _major_ is used when a change in the public API isn't backwards compatible. _minor_ is used when the public API is extended in a backwards compatible way. _patch_ is used when the public API is left unchanged (e.g. a bug fix).

**HTTP API**

Follows the pattern `/api/v#` where `#` is an ever increasing integer. Update version number for any additions or breaking changes to the API. Breaking changes includes both requests and responses. 

## Referencing bundles

The Elis platform consists of a number of API bundles that must be used when implementing a provider. These are: 

* Data definitions
* Elis exceptions
* building api
* automation api
* user service api
* structure api
* storage api

To implement a bundle it is only necessary to reference those that are required. 

For testing it is sometimes necessary to reference an implementation bundle. This is only allowed when testing and, hence, should be marked accordingly in the bundle's POM. For example: 

```xml
<dependency>
  <groupId>org.glassfish.jersey.test-framework.providers</groupId>
  <artifactId>jersey-test-framework-provider-inmemory</artifactId>
  <version>2.6</version>
  <scope>test</scope>
</dependency>
```

## Logging

To use logging in an object it is necessary to reference the OSGI log service. The following example illustrates how. The `bindLog` and `unbindLog` methods are used by the OSGI framework to set a reference to the frameworks's log service.

```java
package se.mah.elis.services.demo.users;

import org.apache.felix.scr.annotations.Reference;
import org.osgi.service.log.LogService;

public class LoggingExample {

  @Reference
  private LogService log;
  
  public void callMeMaybe() {
    if (log != null)
      log.log(LogService.LOG_INFO, "Tada!");
  }
  
  protected void bindLog(LogService service) {
    log = service;
  }
  
  protected void unbindLog(LogService service) {
    log = null;
  }
}
```

Your bundle POM file must include the following dependency:

```xml
<dependency>
  <groupId>org.apache.felix</groupId>
  <artifactId>org.apache.felix.scr.annotations</artifactId>
  <version>1.9.6</version>
</dependency>
```

For testing purposes it may be useful to provide a mock log service. This can be done using [Mockito](https://code.google.com/p/mockito/) in the following manner: 

```java
LogService log = mock(LogService.class);
instanceOfLoggingExample.bindLog(log);
```

## Configuration

Elis uses the OSGI Configuration Admin service to manage configuration properties for bundles. The configuration management interface can be reached in the [Web Console](http://195.178.234.87:8080/system/console/configMgr).

An OSGI service that needs to be configurable must implement the `ManagedService` Java interface. Configuration updates are propagated through the `updated(Dictionary<String, ?>)` method. 

```java
@Component(name = "ExampleConfigurationService", immediate = true)
@Service
public class ExampleService implements ManagedService {

  // properties
  public static String SERVICE_PID = "se.mah.elis.adaptor.example.service";
  public static String PROPERTY_1 = "se.mah.elis.adaptor.example.service.some_property";

  public static Dictionary<String, ?> properties;

  @Reference
  private ConfigurationAdmin configAdmin;

  protected void bindConfigAdmin(ConfigurationAdmin ca) {
    configAdmin = ca;
    setConfig();
  }

  private void setConfig() {
    try {
      Configuration config = configAdmin.getConfiguration(SERVICE_PID);
      properties = config.getProperties();
      
      if (properties == null) 
        properties = getDefaultConfiguration(); // not implemented here
      
      config.update(properties);
    } catch (IOException e) {
      // handle this
    }
  }
  
  protected void unbindConfigAdmin(ConfigurationAdmin ca) {
    configAdmin = null;
  }

  @Override
  public void updated(Dictionary<String, ?> props)
      throws ConfigurationException {
    if (props != null) {
      properties = props;
      // do something is properties change
    }   
  }
}
```

By default, all Elis specific configuration files are stored in `$FELIX_HOME/elis_conf` and persistent across restarts. 

## Development tools

### Tickets

All tickets and tasks related to development are stored in a [Trello board](https://trello.com/b/ynkrnje1/platform).

### Continuous integration

Each push to the master branch is automatically built by the Elis [Jenkins server](http://195.178.234.87:8081/). If all tests pass the generated artifacts are automatically deployed to the [staging environment](http://195.178.234.87:8080/). 

### Branching model

The Elis project uses Nvie's [git flow](http://nvie.com/posts/a-successful-git-branching-model/) branching model. 

## Contributors

* [Johan Holmberg](mailto:johan.holmberg@mah.se)
* [Marcus Ljungblad](mailto:marcus@ljungblad.nu)
* [Axel Olsson](mailto:axel.olsson@mah.se)
* [Joakim Lithell](mailto:joakim.lithell@mah.se)
