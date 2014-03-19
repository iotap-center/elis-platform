The Elis Platform
=============

This project focuses on exploring the potential of mobile services on mobile devices (e.g. smart phones and tablets) to promote energy efficiency in existing buildings. Read more about the background [here](http://elis.mah.se).

This document describes aspects useful while working on the implementation of Elis.

## Terminology

* Bundle - a module that is installable in the Felix runtime
* Provider - a bundle which communicates with, for example, E.On and conforms to the Elis APIs

## Building the platform

Elis uses [Maven](http://maven.apache.org/) to compile, test and deploy Elis artifacts. 

Each bundle can be compiled using `mvn compile` or to test `mvn test`. To create a bundle jar, install it to your local Maven repository and upload a copy to your local Elis deployment run: 

`mvn clean compile test scr:scr bundle:bundle install:install wagon:upload`

### Compiling everything

There is a special project called `super` which contains a Maven specification to compile the entire Elis project. To make things simple, this project contains a Makefile. Run `make build` to deploy to your local Elis installation. To build everything, including documentation, use the following command: 

`mvn clean compile test bundle:bundle install:install wagon:upload javadoc:javadoc site`

## Demo users

The `elis-demo-users` bundle provides a set of demo users that can be used for system and integration testing. It installs a demo platform user and connects two providers to it: E.On and MKB Water data. For the water data to work it is necessary to download a copy of the data from Elvaco ([instructions here](https://github.com/medeamalmo/elis-platform/tree/master/felix-configuration#deploying-elis)). 

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
