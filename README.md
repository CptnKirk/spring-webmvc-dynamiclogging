Dynamic Logging with Logback
============================

# Introduction

The project builds on top of the basic Spring webmvc project and adds better logging support.  It leverages the [Logback](http://logback.qos.ch/) logging library, which in turn is a provider for the [SLF4J](http://www.slf4j.org/) API. The goal is to demonstrate how to add some powerful logging to your Spring project, including runtime dynamic logging.

Note:  Logback and SLF4J are general logging frameworks and work well outside of a Spring project.  I use Spring a lot so I've created some utility REST controllers on top of Logback APIs to provide runtime dynamic configuration.  This same functionality could be implemented in your REST framework of choice.

# Getting Started

Until I figure out how to cleanly host API docs for this project (yea, I see that wiki thing over there) I'll just include all the APIs here.  They aren't hard to grok.  GETs are used everywhere to make it easier on the API via Browser crowd.

## APIs

* Logger based APIs
    * GET /1.0/admin/logging/logger
    * GET /1.0/admin/logging/logger/\_add?logger=&lt;name>&level=&lt;level>
    * GET /1.0/admin/logging/logger/\_drop?logger=&lt;name>
    * GET /1.0/admin/logging/logger/\_dropAll
* MDC based APIs
    * GET /1.0/admin/logging/mdc
    * GET /1.0/admin/logging/mdc/\_add?key=&lt;key>&value=&lt;value>&level=&lt;level>
    * GET /1.0/admin/logging/mdc/\_drop?key=&lt;key>&value=&lt;value>
    * GET /1.0/admin/logging/mdc/\_dropAll 

## Usage
	git clone https://github.com/CptnKirk/spring-webmvc-dynamiclogging.git
	mvn tomcat:run
	
	(new console)

	//MDC tests	

	curl "http://localhost:8080/logging-demo/healthcheck" //see if we're up
	curl "http://localhost:8080/logging-demo/1.0/admin/logging/mdc/_add?key=user&value=jim" //set MDC k/v
	curl "http://localhost:8080/logging-demo/healthcheck" //no expected change
	curl "http://localhost:8080/logging-demo/healthcheck?user=bob" //no expected change (we used value=jim)
	curl "http://localhost:8080/logging-demo/healthcheck?user=jim" //whole ton of output
	curl "http://localhost:8080/logging-demo/1.0/admin/logging/mdc/_dropAll" //reset
	curl "http://localhost:8080/logging-demo/healthcheck?user=jim" //back to no output
	
	//Logger tests

	curl "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?logger=HealthcheckController" //note this is the logger suffix
	curl "http://localhost:8080/logging-demo/healthcheck"
	curl "http://localhost:8080/logging-demo/healthcheck?user=jim" //only a single logger impacted

	//Combo tests
	//You can use both APIs at the same time to narrow scope of MDC'd output
	//You probably want to put these in a named script for easy reuse in your environment

	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/mdc/_add?key=user&value=jim"

	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?logger=FilterChainProxy&level=info" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=MultiSchemeUserDetailsService" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=RethrowingAccessDeniedHandler" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=DispatcherServlet" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=ErrorController" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=ExceptionTranslationFilter" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=HandlerMethodReturnValueHandlerComposit > /dev/nulle"
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=AffirmativeBased" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=MockCredentials" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=ErrorController" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=FilterChainProxy" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=HttpSessionSecurityContextRepository" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=FilterSecurityInterceptor" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=SimpleAuthenticationProvider" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=ProviderManager" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=MultiSchemeUserDetailsService" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=RequestMappingHandlerMapping" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=MockTokenUserDetailsService" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=XmlWebApplicationContext" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=CacheInterceptor" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=warn&logger=SSMCache" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=DefaultListableBeanFactory" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=HandlerMethod" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=MethodSecurityInterceptor" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=ElasticSearchBinaryTransportProvider" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=ExceptionHandlerExceptionResolver" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=ResponseStatusExceptionResolver" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=DefaultHandlerExceptionResolver" > /dev/null
	curl -s "http://localhost:8080/logging-demo/1.0/admin/logging/logger/_add?level=info&logger=HandlerMethodReturnValueHandlerComposite" > /dev/null

	curl "http://localhost:8080/logging-demo/healthcheck?user=bob" //no expected change (we used value=jim)
	curl "http://localhost:8080/logging-demo/healthcheck?user=jim" //just Jim's request flow, minus all the Spring extras

# Notes

*	_Q: How easy is it to migrate to SLF4J + Logback from my logging framework?_

	A: **Really simple**.  SLF4J includes a number of bridges back from "legacy" logging frameworks.  Including Log4J, Jakarta Commons Logging, and Java Util Logging.  All that is required is for you to include the appropriate lib dependency in your POM.  This project includes all of them for reference.  It also includes an example of both a Logback and Log4J logger being used.
*	The Spring core framework doesn't use SLF4J natively.  This can be useful if seeing all of the Spring related logs ends up being more of a curse than a boon when dealing with MDC.  Just drop off jcl support (remove the dependency from the pom).
*	While I tried to keep this project minimalistic there is some polish that I couldn't force myself not to include, try not to let that code detract from the logging work.  Those are:
    * Validation of request params within the monitoring controllers.  This is nice for the user; not sure about the required params to \_add, leave em off an you'll get a pretty good response back.
    * Error handling.  You could expose nasty Hibernate Validator stack traces to your user, but I wouldn't.  There's hooks within web.xml back to our Error controller and it's responsible for returning appropriate error payloads instead of the standard server error pages.  If you haven't seen this done before, well...you have now.
*	Next step:  Refactor all of the stuff in the logging package into a library that can be imported as a dependency to other Spring aware projects. 
