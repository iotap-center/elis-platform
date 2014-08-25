# Notes on Felix configuration and dependencies

## Elis dependencies

The Elis platform depends on a number of dependencies and these are sparsely documented here. A full list of all dependencies [can be found found here](bundles.md). 

**Felix** 

All bundles under `org.apache.felix.*` are required by Felix to run. It will provide a basic web administrative interface, support for declarative services, and logging and configuration. 

**HTTP** 

To create HTTP apis Elis uses [Jersey](http://jersey.java.net), a framework to build RESTful web services in Java. In addition, an adaptation of [OSGI JAX-RS Connector](https://github.com/hstaudacher/osgi-jax-rs-connector) is used to dynamically load the HTTP endpoints. The adaptation is built with the main maven build script. HTTP responses are provided in the JSON format and these are encoded and decoded using [google-gson](https://code.google.com/p/google-gson/). 

**MySQL** 

MySQL is used as the storage backend for the Elis platform and as such depends on a JDBC connector. 

* mysql-connector-java-5.1.27.jar

## Configuration

To change port modify the following property in `config.properties`:

```
org.osgi.service.http.port=8088
```
