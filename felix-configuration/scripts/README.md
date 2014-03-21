# Scripts for Elis

## Running Elis on Linux

The OSGI environment Felix runs as a user called `felix` and belongs to a group called `felix`. 

To setup boot scripts for Elis on Linux do the following. 

1. Copy the `init-felix-run` script to `/etc/init.d/` and rename it to `felix-run`

2. Copy the `felix-run` script to `$FELIX_HOME/bin/` 

3. If necessary, modify the path to felix-run in `/etc/init.d/felix-run`. The default value assumes `$FELIX_HOME` points to `/home/felix/felix/bin/felix-run`.

## Pre and post build scripts for Jenkins

The Jenkins server is located at http://195.178.234.87:8081. The [elis-platform job]() runs the code in `pre_build.sh` before executing the build and the the code in `post_build.sh` after executing the build. 

The Jenkins user is added to the `felix` group which has sudo access to restart the process. To set this up: 

1. As an admin `sudo usermod -a -G felix jenkins`

2. Add the file `/etc/sudoers.d/felix` with the content `%felix  ALL=NOPASSWD: /etc/init.d/felix-run restart` 

