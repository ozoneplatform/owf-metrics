//import org.codehaus.groovy.grails.commons.ApplicationHolder
import grails.util.GrailsUtil
import ozone.owf.grails.OwfExceptionResolver
import ozone.owf.Configuration
import ozone.owf.grails.services.AutoLoginAccountService
import ozone.owf.grails.domain.ERoleAuthority
import ozone.owf.grails.services.AccountService
import ozone.owf.nineci.hibernate.AuditTrailInterceptor
import org.codehaus.groovy.grails.commons.ConfigurationHolder

beans = {

	entityInterceptor(AuditTrailInterceptor)
	
    // wire up a different account service if -Duser=something and environment is development
    if (GrailsUtil.environment == "development") {
        switch (System.properties.user) {
            case "testUser1":
                println("Using AutoLoginAccountService - you will be logged in as testUser1")
                accountService(AutoLoginAccountService) {
                    autoAccountName = "testUser1"
					autoAccountDisplayName = "Test User 1"
                    autoRoles = [ERoleAuthority.ROLE_USER.strVal]
                }
                break
            case "testAdmin1":
                println("Using AutoLoginAccountService - you will be logged in as testAdmin1")
                accountService(AutoLoginAccountService) {
                    autoAccountName = "testAdmin1"
					autoAccountDisplayName = "Test Admin 1"
                    autoRoles = [ERoleAuthority.ROLE_USER.strVal, ERoleAuthority.ROLE_ADMIN.strVal]
                }
                break
          case "testAdmin2":
              println("Using AutoLoginAccountService - you will be logged in as testAdmin2")
              accountService(AutoLoginAccountService) {
                  autoAccountName = "testAdmin2"
                  autoAccountDisplayName = "Test Admin 2"
                  autoRoles = [ERoleAuthority.ROLE_USER.strVal, ERoleAuthority.ROLE_ADMIN.strVal]
              }
              break
            default :
               accountService(AccountService)
               break
        }
    } else {
        accountService(AccountService)
    }

    // Do not remove, this bean must be here even though it is empty
    OzoneConfiguration(Configuration) {

        serverVersion=ConfigurationHolder.config.server.version

        def server = System.properties['server.host'] ?: 'localhost'
        def port = System.properties['server.port'] ?: '8443'
        def protocol = port.endsWith('443') ? 'https' : 'http'
        def context = ConfigurationHolder.config.grails.app.context
		
		defaultTheme = "a_default"
    }

	exceptionHandler(OwfExceptionResolver)
	{
		exceptionMappings = [
				'java.lang.Exception': '/error'
				]		
	}
}
