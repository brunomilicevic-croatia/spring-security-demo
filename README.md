# Http

Http is a protocol we use to send requests between computer over the internet.

> Hypertext Transfer Protocol (HTTP) is an application-layer protocol for transmitting hypermedia documents, such as HTML. It was designed for communication between web browsers and web servers, but it can also be used for other purposes

![HTTP Requests](https://miro.medium.com/max/853/1*MoxFEabKGx6NxlKoZ0lXJQ.png)

# Java Servlets

Java has a specification for web applications called javaEE and servlets are part of that specification. Servlet is a unit of code that will handle a HTTP request send to the servlet container. 
Servlet container is a implementation of JavaEE specification like Tomcat or Jetty.

# Spring MVC

Spring can run on servlet container and comply with the servlet spec or with new Spring WebFlux if uses Netty server which does not implement the JavaEE.

![HTTP Requests](https://i.stack.imgur.com/3sYFm.png)

# Securing HTTP requests

On the server side when we process the HTTP request we want to know who sent the request and is he allowed to do the action.

To determine who is the user we first require him to login. Login is the authentication step, where user inputs his credentials.
When credentials are checked user is logged in and each HTTP request will be authenticated.

> With spring security authorization or checking of credentials is done in the ProviderManager class. ProviderManager will call all registered AuthenticationProviders these are
> DaoAuthenticationProvider for example, the one that is used if we create a UserDetailsService.

> We can also set our own custom authentication manager if needed you can see that in the CustomAuthenticationProvider configuration

After we check users credentials we will need some information about the user with each request. This information can be his roles, username etc.
This data we is called session data and we create this session object when user authenticates.

Since session data is stored on the backend we need to transfer this data to the client also. Since clients are creating HTTP requests on the client side they need to know what session data to send with each request.
Here HTTP cookies come into play. Cookies are a simple map of key value pairs that are transferred with each HTTP request from the browser. 

Now since our session object can grow it will slow down the communication between the client and server since each HTTP request will have to contain the session object.
To reduce the amount of data we transfer we just send a session id and then server stores the map of session ids and actual session objects.

This type of security is called session based security and is the most common type in web applications.

## Literature:
1. [Spring security guide](https://www.marcobehler.com/guides/spring-security)