# Deploying the Elis platform

Elis is assumed to run on a Linux machine that has, at least, Java 1.7 installed. It is based on the Apache Felix OSGi runtime, so we'll need a copy of that too. 

First, create a user account called `felix` which we can use to run the Elis platform. 

## Installing the OSGi runtime and 

To deploy the Elis Platform first download [Apache Felix](http://felix.apache.org/) 4.2.1 from its website. Extract the tarball to a location of your choice. We shall use `~/felix-framework-4.2.1` as `$FELIX_HOME` 

## Elis dependencies

1. Install [all the bundles available in this repo](felix-configuration/bundle/) to the following destination `$FELIX_HOME/bundle`

2. Create the folder `~/elis/`

3. To enable maven's auto deploy feature, create a symlink in `~/elis/` called `osgi` pointing to `$FELIX_HOME/bundle`. 

4. Test your felix setup. Go to `$FELIX_HOME/` and execute `java -jar bin/felix.jar`. Make sure there are no errors when starting the platform the first time, otherwise report the error indicating which dependencies are missing. 

## Setting up MySQL

1. Install a copy of MySQL if you haven't already. Create a user called `elis` and choose a password. Give it full access to a database called `elis`. 

2. Populate the database with the necessary tables using [this script](Elis%20user%20and%20persistent%20storage%20service%20implementations/src/main/resources/db/production.sql). From the command line run `mysql -u elis -p < production.sql` to populate the database. 

3. Let Elis know about the username and password in the XYZ

