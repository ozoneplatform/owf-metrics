<%@ page import="grails.converters.JSON" contentType="text/javascript" %>
<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder" contentType="text/javascript" %>

var Ozone = Ozone || {};
Ozone.metrics = Ozone.metrics || {};

//externalize any config properties here as javascript properties
//this should be only place where these config properties are exposed to javascript

//auto convert to JSON this will take care of special characters
Ozone.metrics.config = ${(applicationContext.OzoneConfiguration as JSON)}

//add in contextPath
Ozone.metrics.config.webContextPath = '${request.contextPath}';

Ozone.metrics.config.version = '${ConfigurationHolder.config.server.version}';

Ozone.metrics.config.currentTheme = ${currentTheme};
