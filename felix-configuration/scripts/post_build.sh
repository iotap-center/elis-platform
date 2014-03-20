#!/bin/bash

# clean up
rm -rf /var/lib/jenkins/elis/deployed/*.jar

# copy dependencies in place
cp -R /var/lib/jenkins/workspace/elis-platform/code/felix-configuration/bundle/*.jar /var/lib/jenkins/elis/deployed
cp -R /var/lib/jenkins/elis/osgi/*.jar /var/lib/jenkins/elis/deployed

# restart felix
sudo /etc/init.d/felix-run restart 
