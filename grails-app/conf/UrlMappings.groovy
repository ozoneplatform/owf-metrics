import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class UrlMappings {
    static mappings = {
        "/js/config/config.js" {
            controller="config"
            action="config"
        }

        "/metric"{
            controller='metric'
            action={
                GrailsWebRequest webRequest = (GrailsWebRequest) RequestContextHolder.getRequestAttributes();
                String method = webRequest.getCurrentRequest().getMethod().toUpperCase()
                Map params = webRequest.getParameterMap()

                // parse _method to map to RESTful controller action
                String methodParam = params?."_method"?.toUpperCase()
                if (methodParam == 'PUT' || methodParam == 'DELETE' || methodParam == 'GET' || methodParam == 'POST') {
                    method = methodParam
                }
                // scan through methods to assign action
                if (method == 'GET') {
                    return "list"
                } else if (method == 'POST') {
                    return "create"
                }
                else if (method == 'DELETE') {
                    return "delete"
                }
                else {
                    return "list"
                }
            }
        }
        "/metricView"{
            controller='metric'
            action='view'
        }
        "/metricGraphData"{
            controller='metric'
            action='graphData'
        }
        "/tagCloudMetric"{
            controller='metric'
            action='getTagCloud'
        }
        "500"(controller: 'error')
    }
}
