# zuul2-sample

A gateway service using Zuul 2 that load balances backends without Eureka.

This application demonstrates the use of Zuul 2 to build a simple api gateway with minimal configuration. It is ispired from the Zuul 2 sample. It uses Ant style path patterns for mapping urls to backends/origins. 

### Configurations:
* \<client-name>.ribbon.\<property-name>=\<value> // for client configuration
* \<client-name>.ribbon.listOfServers=\<value> // for configuring the static server list to the backends
* routes.\<client-name>.path=\<path-pattern> // pattern to be used for mapping clients to URL's 

The path patterns are based on spring/apache implementation of the same.

## References  
https://github.com/Netflix/zuul
https://shiro.apache.org/static/1.3.2/apidocs/org/apache/shiro/util/AntPathMatcher.html
