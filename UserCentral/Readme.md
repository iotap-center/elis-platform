# UserCentral 

The UserCentral registers two OSGi services: 

* a user repository (current implementation is memory store only)
* a user http resource accessible on /services/users/

Furthermore, it exports a user object. 

## Dependencies

UserCentral is built using BndTools 2.0

It depends on the following external java libraries:

* [javax.ws.rs 2.0](http://mvnrepository.com/artifact/javax.ws.rs/javax.ws.rs-api)

 