# Deploying Elis

This document aims to describe how to deploy and configure the Elis platform. 

## Prerequisites 

Elis is based on the Apache Felix OSGi runtime. Go ahead and download a copy. 

* [Oracle JRE 1.7](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 
* A copy of [Apache Felix 4.2.1](http://felix.apache.org/downloads.cgi)

## Installing the OSGi runtime and dependencies

Elis depends on a number of third-party bundles. These are available as jars in `/felix-configuration/bundle`. 

1. To deploy the Elis Platform first download [Apache Felix](http://felix.apache.org/) 4.2.1 from its website. Extract the tarball to a location of your choice. We shall use `~/felix-framework-4.2.1` as `$FELIX_HOME` 

2. Install all the bundles available in this repo's `bundle` folder to the following destination `$FELIX_HOME/bundle`

3. Create the folder `~/elis/`

4. Create a symlink in `~/elis/` called `osgi` pointing to `$FELIX_HOME/bundle`. This is important for the maven auto deploy script to work. 

5. Test your felix setup. Go to `$FELIX_HOME/` and execute `java -jar bin/felix.jar`. Make sure there are no errors when starting the platform the first time, otherwise report the error indicating which dependencies are missing. 

6. Install a copy of MySQL if you haven't already. Create a user called `elis_test` with the password `elis_test`. Give it full access to a database called `elis_test`. 

7. Populate the database with the necessary tables using [this script](https://github.com/medeamalmo/elis-platform/blob/master/Elis%20user%20and%20persistent%20storage%20service%20implementations/src/main/resources/db/test.sql). From the commandline you can run `mysql -u elis_test -p < test.sql` to populate the database. 

You are done setting up the runtime environment. Continue with building the Elis code. 

### Elis dependencies

The Elis platform depends on a number of dependencies and these are sparsely documented here. A full list of all dependencies [can be found found here](bundles.md). 

**Felix** 

All bundles under `org.apache.felix.*` are required by Felix to run. It will provide a basic web administrative interface, support for declarative services, and logging and configuration. 

**HTTP** 

To create HTTP apis Elis uses [Jersey](http://jersey.java.net), a framework to build RESTful web services in Java. In addition, an adaptation of [OSGI JAX-RS Connector](https://github.com/hstaudacher/osgi-jax-rs-connector) is used to dynamically load the HTTP endpoints. The adaptation is built with the main maven build script. HTTP responses are provided in the JSON format and these are encoded and decoded using [google-gson](https://code.google.com/p/google-gson/). 

**MySQL** 

MySQL is used as the storage backend for the Elis platform and as such depends on a JDBC connector. 

* org.tmatesoft.sqljet_1.1.8.r1303_v20130906_1709.jar 
* mysql-connector-java-5.1.27.jar

## Water data

If one wants to work with the MKB Water data it is necessary to download the data from the Elvaco's FTP. Run the [this script](https://github.com/medeamalmo/elis-platform/blob/master/mkb-water-provider/src/main/resources/sync-from-ftp.sh) to synchronise the data. It will be stored in `/tmp/mkb-water-data` which is also the default directory that Elis will try to load the data from. 

## Configuration

To change port modify the following property in `config.properties`:

```
    org.osgi.service.http.port=8088
```

More configuration possibilities to follow. 
