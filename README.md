spring-rest-api
===============

Illustration of the best practices for designing and testing client-server application with REST API interface
The present approach introduces the following artifacts:
- app-domain          Domain objects and service interfaces, no framework dependencies
- app-rest-common     Common entities for rest exposure
- app-rest-provider   Server-side REST controllers for Spring MVC infrastructure
- client-app          Client application demo
```
 client-app uses: app-domain, app-rest-common and (for testing only, hence in test scope) app-rest-provider
```
- server-app          Server application demo
```
 server-app uses: app-domain, app-rest-common, app-rest-provider
```

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

## Adding as maven project

Repository:

```xml
    <repositories>
        <repository>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <id>custom-central</id>
            <url>https://github.com/avshabanov/maven-repo/raw/master/libs-release</url>
        </repository>
    </repositories>
```

Project:

```xml
    <dependency>
        <groupId>com.alexshabanov.springrestapi</groupId>
        <artifactId>rest-api-test</artifactId>
        <version>1.0.2</version>
        <scope>test</scope>
    </dependency>
```


## How to run demo
To run the illustrated sample, first you need to mvn clean install then run server
```
    mvn jetty:run -Pjetty-local
```    
<- this will start server on http://127.0.0.1:9090/server-app
In order to check REST API just type http://127.0.0.1:9090/server-app/rest/users in your browser or curl it.

In order to run client you'll need to launch App in the client-app application:
```
    mvn exec:java -Dexec.mainClass=com.alexshabanov.springrestapi.App
```

Have fun!


![alt text](http://images.paraorkut.com/img/pics/images/b/bob_kelso-9324.jpg "Bob Kelso")
