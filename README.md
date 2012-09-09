spring-rest-api
===============

Illustration of the best practices for designing and testing client-server application with REST API interface

## Install
mvn clean install

## Usages

Sample usage when testing:
    @Inject
    private RestApiTestSupport support;
    // ...
    MockHttpServletResponse response = support.handle(mockHttpServletRequest);
    // verify response

## Notes
The illustrated sample uses

    * Class-driven configuration for spring -  [@Configuration annotation](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/context/annotation/Configuration.html).
    * Standardized annotation **@Inject** instead of **@Autowired**.


Now REST API testing should not be a problem :)


![alt text](http://images.paraorkut.com/img/pics/images/b/bob_kelso-9324.jpg "Bob Kelso")
