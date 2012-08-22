Pulsar Framework
================

Minimalist Web Framework Java

Requirements
------------

 - Java 

Instalation
-----------
Configure filter mapping in web.xml

```xml
<filter>
 <filter-name>FrontController</filter-name>
 <filter-class>com.pulsar.framework.core.FrontController</filter-class>
</filter>
<filter-mapping>
 <filter-name>FrontController</filter-name>
 <url-pattern>/*</url-pattern>
</filter-mapping>
```

Running Application
-------------------

Open your browser and enter:

  http://localhost:8000

Let's Rock!