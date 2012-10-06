package ozone.owf.grails.controllers

import grails.converters.JSON

/**
 * User controller.
 */
class ConfigController {

    def accountService
    def grailsApplication
    def preferenceService
    def themeService
    def serviceModelService

    def config = {
//        def curUser = accountService.getLoggedInUser()
//
//        def pDate = new Date()
//        def pDateString = null
//        if (curUser.prevLogin != null) {
//          pDate = curUser.prevLogin
//        }
//        pDateString = prettytime.display(date: pDate).toString()
//        if ("1 day ago".equalsIgnoreCase(pDateString)) { pDateString = 'Yesterday' }
//
//        def groups = curUser.groups.collect{ serviceModelService.createServiceModel(it) }
//        def emailString = curUser.email != null ? curUser.email : ''
//
//        def isAdmin = accountService.getLoggedInUserIsAdmin()
//
//        def curUserResult = [displayName: curUser.username, userRealName:curUser.userRealName,
//                prevLogin: pDate, prettyPrevLogin: pDateString, id:curUser.id, groups:groups, email: emailString,
//                isAdmin: isAdmin] as JSON

        def themeResults = themeService.getCurrentTheme()
        def theme = [:]

        //use only key information
        theme["themeName"] =  themeResults["name"]
        theme["themeContrast"] =  themeResults["contrast"]
        theme["themeFontSize"] =  themeResults["owf_font_size"]

        render(view: 'config_js',
                model: [
//                  user: curUserResult,
                  currentTheme: theme as JSON
                 ],
                contentType: 'text/javascript')
    }

}
