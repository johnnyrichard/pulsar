Pulsar Framework
================

Minimalist Web Framework Java

Requirements
------------

 - Java 

Instalation in few steps
------------------------

 1. Configure web.xml, append filter mapping and listener.

```xml
<filter>
  <filter-name>FrontController</filter-name>
  <filter-class>com.pulsar.framework.core.FrontController</filter-class>
</filter>
<filter-mapping>
  <filter-name>FrontController</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
<listener>
  <listener-class>com.pulsar.framework.listener.ContextStartListener</listener-class>
</listener>
```
 2. Create file /WEB-INF/app-config.properties, define package of controlles and view prefix/suffix.

```properties
scan.controller =   com.yourcompany.projectname.controller
view.prefix     =   /WEB-INF/views/
view.suffix     =   .jsp
```
 3. Create your first controller

```java
@Controller("hello")
public class HelloController {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String index() {
    return "Hello world!";
  }
}
```
Running Application
-------------------

Open your browser and enter:

  http://localhost:8000/hello

Let's Rock!