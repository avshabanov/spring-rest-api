spring-rest-api
===============

Illustration of the best practices for designing and testing client-server application with REST API interface
The present approach introduces the following artifacts:
    app-domain          Domain objects and service interfaces, no framework dependencies.
    app-rest-common     Common entities for rest exposure
    app-rest-provider   Server-side REST controllers for Spring MVC infrastructure
    client-app          Client application demo
        Uses: app-domain, app-rest-common and (for testing only, hence in test scope) app-rest-provider
    server-app          Server application demo
        Uses: app-domain, app-rest-common, app-rest-provider

## Install
mvn clean install

## Usage

Sample usage when testing:

```java
    @Inject
    private RestApiTestSupport support;
    // ...
    MockHttpServletResponse response = support.handle(mockHttpServletRequest);
    // verify response
```

## Notes
The illustrated sample uses
+ Class-driven configuration for spring -  [@Configuration annotation](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/context/annotation/Configuration.html).
+ Standardized annotation **@Inject** instead of **@Autowired**.


Now REST API testing should not be a problem :)


![alt text](http://images.paraorkut.com/img/pics/images/b/bob_kelso-9324.jpg "Bob Kelso")
