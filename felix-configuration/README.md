# Deploying Elis

This repository and readme aims to describe how to configure and deploy the Elis platform using the Apache Felix OSGi runtime. 

## Prerequisites 

* [Oracle JRE 1.7](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 
* A copy of [Apache Felix 4.2.1](http://felix.apache.org/downloads.cgi)


## Installing the OSGi runtime and dependencies

Elis depends on a number of third-party bundles. These are available as jars in `/bundle` of this repository. 

1. To deploy the Elis Platform first download [Apache Felix](http://felix.apache.org/) 4.2.1 from its website. Extract the tarball to a location of your choice. We shall use `~/felix-framework-4.2.1` as `$FELIX_HOME` 
2. Copy the config.properties file available in this repo to `$FELIX_HOME/conf`, overriding the default configuration file. In essence, the changes made to the configuration is the removal of some packages which OSGi exports by default. Two external bundles will provide these exports instead. 
3. Install all the bundles available in this repo's `bundle` folder to the following destination `$FELIX_HOME/bundle`

From the command-line, assuming you are in `$FELIX_HOME` run `java -jar bin/felix.jar`. Make sure there are no errors when starting the platform the first time. 

## Installing Elis bundles

_TODO_