package ozone.owf.grails.services

import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import grails.converters.JSON
import ozone.owf.grails.AuditOWFWebRequestsLogger
import ozone.owf.grails.OwfException
import ozone.owf.grails.domain.Person
import ozone.owf.grails.OwfExceptionTypes
import ozone.owf.grails.domain.ERoleAuthority
import ozone.security.authentication.OWFUserDetails
import ozone.owf.grails.domain.Group
import ozone.owf.grails.domain.ServiceModelUtil
import org.hibernate.CacheMode
/**
 * Service for account-related operations.
 */
class AccountService {
	 
    //def authenticateService
    def loggingService = new AuditOWFWebRequestsLogger()
    //    def domainMappingService
	
    private static def addFilter = {name, value, c ->
        c.with {
            switch(name) {
                case 'lastLogin':
                System.out.println("Name: " + name + " Value: " + value)
                break;
                default:
                ilike(name, '%' + value + '%')
            }
        }
    }
	 
    def getLoggedInUser() {
        return Person.findByUsername(getLoggedInUsername(),[cache:true])
    }
	
    /*
     * Grab the display name from spring security user detail object if the custom security module implemented it.
     * If not just use user name.
     */
    def getLoggedInUserDisplayName(){
        def p = SCH?.context?.authentication?.principal
        def displayName = p?.username ?: "unknown"
        if (p?.metaClass?.hasProperty(p,"displayName")){
            displayName = p?.displayName ?: displayName
        }
        return displayName
    }
	
    def getLoggedInUsername() {
        return SCH?.context?.authentication?.principal?.username
    }

    def getLoggedInUserIsAdmin() {
        for (role in getLoggedInUserRoles()) {
          
            if(role.authority.equals(ERoleAuthority.ROLE_ADMIN.strVal)){
                return true;
            }
        }
        return false;
    }

    def getLoggedInUserIsUser() {
        for (role in getLoggedInUserRoles()) {
            if(role.authority.equals(ERoleAuthority.ROLE_USER.strVal)){
                return true;
            }
        }
        return false;
    }
    
    def getLoggedInUserRoles() {
       
        return SCH?.context?.authentication?.principal?.authorities
    }

    /**
     * Return a list of owf user groups of which the user is a member
     * The class returned is list of ozone.security.authorization.target.OwfGroup objects.
     *
     * The list may be empty.
     *
     */
    def getLoggedInAutomaticUserGroups()
    {
        def user = SCH?.context?.authentication?.principal
        if(user instanceof OWFUserDetails) 
        {
            return user?.getOwfGroups()
        }
        return []
    }



    def getAllUsers() {
        if (!getLoggedInUserIsAdmin())
        {
            throw new OwfException(message:'You are not authorized to see a list of users in the system.', exceptionType: OwfExceptionTypes.Authorization)
        }
        return Person.listOrderByUsername()
    }
	
    def createOrUpdate(params) {
        def returnValue = null
        def isNewUser = false

        log.debug("AccountService.createOrUpdate with params:: "+params)

        if (params.data && !params.tab) {
            def users = [];
            def json = JSON.parse(params.data)
            json.each { data ->
                data.each {
                    if (!data.isNull(it.key))
                    params[it.key] = it.value
                }
                def user = Person.findByUsername(params.username)
                if (user && !params.id)
                    throw new OwfException(message: 'A user with this name already exists.',exceptionType: OwfExceptionTypes.GeneralServerError)
                if (!user)
                {
                    //Create
                    user = new Person()
                    user.enabled = true
                    user.emailShow = true
                    loggingService.log("Added new User [username:" + user.username +
                      ",userRealName:" + user.userRealName + "]")
                    isNewUser = true
                }
                params.lastLogin = params.lastLogin ? new Date(params.lastLogin) : null
                params.prevLogin = params.prevLogin ? new Date(params.prevLogin) : null
                user.properties = params
                user.save(failOnError: true)
                users << user

                // Add to All Users group
                if (isNewUser) {
                    def grp = Group.findByNameAndAutomatic('OWF Users', true, [cache:true])
                    if (grp) {
                        grp.addToPeople(user)
                        grp.save(flush: true, failOnError: true)
                    }
                }
            }
            returnValue = users*.toServiceModel()
        }

        return [success:true,data:returnValue]
    }
	
}
