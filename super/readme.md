# Building the Super POM

This repository and readme aims to describe how to configure and build the Elis platform using Apache Maven.

## Prerequisites 

* Follow the [Deploying Elis](https://github.com/medeamalmo/elis-platform/tree/master/felix-configuration). You may omit the _Installing Elis bundles_ section, as Maven will build, install and deploy all Elis bundles anyway.
* Install [Apache Maven](http://maven.apache.org/) on your computer.
* Install and configure [JUnit 4](http://junit.org/).

For sake of simplicty, we'll define the location of your Super POM directory (e.g. `/home/batman/git/elis-platform/super`) as `$SUPER_POM_HOME`.

## Setting up your environment

Elis's Super POM will, when set up, test, build and deploy your project. It will also install the artifacts in your local Maven repository. In order to do so, the Super POM needs to know where to put stuff created by the build. We do this by creating a directory called `elis` in the root of your home directory. In that directory, create a symlink called `osgi`, pointing at `$FELIX_HOME/bundle` (see the _Deploying Maven_ guide mentioned in the section above).

### Import the project into Eclipse

In Eclipse, navigate to this project in the _Git Repository Exploring_ view and right click the top folder. From there, select _Import Projects..._. Click the _Next >_ button and then click the _Finish_ button. That's it, you're done.

## Running the build

### Build using Eclipse

From Eclipse, right click the _Super POM_ project and select _Maven build..._ from the _Run As_ menu. You need to define what goals to taget. The targets needed to run the full build and deploy cycle are `clean test bundle:bundle install:install wagon:upload`. From now on, you should also be able to find this run configuration in Eclipse's global run menu.

### Build from CLI

Navigate to this project's folder. Within it, build the project by typing `clean test bundle:bundle install:install wagon:upload` and hitting enter. That's it.

## Build with documentation

Not only is the Super POM able to build, test and deploy your project, but is also able to generate documentation, such as javadoc, project metadata, etc. However, this build is far slower.

### Setting up your documentation environment

From within the `elis` directory within your home directory, create a symlink called `documentation` pointing to `$SUPER_POM_HOME/target/site`. This link will now point to where the html-based documentation will be found. The `documentation` symlink isn't necessary per se, but makes navigation much easier.

### Building a build with documentation

Follow the _Running the build_ guide above, but add `javadoc:javadoc site` at the end of the list of targets. Your build will now also produce the documentation site.
