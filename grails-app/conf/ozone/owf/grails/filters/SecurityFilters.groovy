package ozone.owf.grails.filters

import org.apache.commons.lang.time.StopWatch
import ozone.owf.grails.domain.Person
import ozone.owf.grails.domain.Group

class SecurityFilters {
    def accountService
    def groupService

    def filters = {
//        exceptPublic(uri : '/public/**', invert: true) {
//            before = {
//                log.debug("Entering SecurityFilter filters: before");
//                log.debug("controllerName:${controllerName}");
//                log.debug("actionName:${actionName}");
//
//                try {
//                    def username = accountService.getLoggedInUsername()
//                    def userDisplayName = accountService.getLoggedInUserDisplayName()
//                    StopWatch stopWatch
//
//                    //find the user (if possible) and stuff them into the session
//                    if (username == null) {
//                        log.debug("username is null, erroring out with 401");
//                        response.sendError(401)
//                        return false
//                    } else {
//
//                        log.debug("Username is " + username);
//
//                        if (log.isInfoEnabled()) {
//                            stopWatch = new StopWatch();
//                            stopWatch.start();
//                            log.info("SecurityFilter finding person");
//                        }
//                        def personInDB = Person.findByUsername(username, [cache:true])
//                        if (log.isInfoEnabled()) {
//                            stopWatch.stop();
//                            log.info("SecurityFilter found person in "+stopWatch);
//                        }
//
//                        if (!personInDB)
//                        {
//                            log.info 'Adding New User to Database'
//                            //add user to DB since they don't exist
//                            personInDB = new Person(
//                                username     : username,
//                                userRealName : userDisplayName,
//                                //passwd       : 'password',
//                                lastLogin    : new Date(),
//                                email        : '',
//                                emailShow    : false,
//                                description  : '',
//                                enabled      : true)
//
//                            if (log.isInfoEnabled()) {
//                                stopWatch = new StopWatch();
//                                stopWatch.start();
//                                log.info("securityfilters.save new person");
//                            }
//                            personInDB.save(flush:true)
//                            if (log.isInfoEnabled()) {
//                                stopWatch.stop();
//                                log.info("securityfilters.saved new person"+stopWatch);
//                            }
//
//                            session["savedLastLogin"] = true
//                        }
//                        session.personID = personInDB.id
//
//                        def sezzion = session
//                        // Last login value should exist if user has logged in before
//                        if(personInDB.lastLogin == null)
//                        {
//                            log.debug("Setting default lastlogin");
//                            personInDB.lastLogin = new Date()
//                        }
//
//                        //update last logged in if we haven't already done so and update name in case it changed (marriage, divorce etc)
//                        // The real name is the display name provided by the custom security module.
//                        if (!session["savedLastLogin"])
//                        {
//                            personInDB.prevLogin = personInDB.lastLogin
//                            personInDB.lastLogin = new Date()
//
//                            if (userDisplayName != username)
//                            {
//                                personInDB.userRealName = userDisplayName
//                            }
//                            if (log.isInfoEnabled())
//                            {
//                                stopWatch = new StopWatch();
//                                stopWatch.start();
//                                log.info("securityfilters.save existing person");
//                            }
//                            personInDB.save(flush:true)
//                            if (log.isInfoEnabled())
//                            {
//                                stopWatch.stop();
//                                log.info("securityfilters.saved existing person"+stopWatch);
//                            }
//                            session["savedLastLogin"] = true
//                        }
//
//                        //for now these are disabled because there is no user management yet
////                        loadUserData(personInDB)
////                        loadAdminData(personInDB)
//
//                        // Check Automatic Groups and add/remove them as necessary
//                        if (!session["savedUserGroups"])
//                        {
//                            def newAutomaticUserGroups = accountService.getLoggedInAutomaticUserGroups()
//
//                            log.info("Analyzing New Automatic User Groups for " + username + " from Security Plugin--list size is " + newAutomaticUserGroups.size())
//
//                            // reset all the person's groups.  We'll update them in the end.
//
//                            for ( newGroup in newAutomaticUserGroups )
//                            {
//                                // check if the group already exists
//                                def matches = Group.findAllByNameAndAutomatic(newGroup.getOwfGroupName(), true, [cache:true])
//                                def myGroup
//                                if (matches == null || matches?.size == 0)
//                                {
//                                    // add it
//                                    myGroup = new Group()
//                                    myGroup.people =[personInDB]
//                                    myGroup.properties = [
//                                        name: newGroup.getOwfGroupName(),
//                                        description: newGroup.getOwfGroupDescription(),
//                                        email: newGroup.getOwfGroupEmail(),
//                                        automatic: true,
//                                        status: newGroup.isActive() ? 'active' : 'inactive'
//                                    ]
//                                }
//                                else
//                                {
//                                    // it already exists--update it rather than creating a new one
//                                    myGroup = matches[0]
//                                    myGroup.people << personInDB
//                                }
//
//                                myGroup.save(flush: true)
//                            }
//
//                            // retrieve all  groups assigned to the user--if an
//                            // automatic group has been removed from the security
//                            // plugin, the relationship must be removed from the OWF database
//                            def c = Group.createCriteria()
//                            def existingGroupAssignments = c.list{
//                                eq('automatic',true)
//                                people {
//                                    eq('username', username)
//                                }
//                            }
//                            // we must turn newAutomaticUserGroups into a map
//                            def newAutomaticUserGroupsMap = new HashMap()
//                            for(group in newAutomaticUserGroups)
//                            {
//                                newAutomaticUserGroupsMap.put(group.getOwfGroupName(), group)
//                            }
//                            //
//                            // search to see if any groups assigned to the user are
//                            // not in the list from the security plugin, now a map
//                            // called newAutomaticUserGroupsMap
//                            for(group in existingGroupAssignments)
//                            {
//                                if(! newAutomaticUserGroupsMap.get(group.name))
//                                {
//
//                                    // we must delete the user/group association in the database--remove the user and save the group.
//
//                                    def personToRemove
//                                    for(person in group.people)
//                                    {
//                                        if (person.username == username)
//                                        {
//                                            personToRemove = person
//                                            break
//                                        }
//                                    }
//                                    if( personToRemove != null )
//                                    {
//                                        def criteria = Person.createCriteria()
//                                        group.people = criteria.list{
//                                            groups {
//                                                eq('id', group.id)
//                                            }
//                                        }
//
//                                        group.people = group.people - personToRemove
//                                        group.save()
//                                    }
//                                }
//                            }
//
//                            // now
//                            session["savedUserGroups"] = true
//                        }
//
//                        //verify they have and admin OR user role
//                        if(accountService.getLoggedInUserIsAdmin() || accountService.getLoggedInUserIsUser()){
//                            return true;
//                        }
//                        else{
//                            //println("401: NO ROLES!!")
//                            response.sendError(401)
//                            return false;
//                        }
//
//                    }
//                } catch (Exception e) {
//                    log.error e.getMessage();
//                    response.sendError(401);
//                    return false;
//                }
//            }
//        }
    }

    private loadAdminData(admin) {
      if (accountService.getLoggedInUserIsAdmin()) {
          // Create OWF Admins group if it doesn't already exist
          def adminGroup = Group.findByName('OWF Admins', [cache:true])
          if (adminGroup == null)
          {
              // add it
              adminGroup = new Group(
                  name: 'OWF Admins',
                  description: 'OWF Administrators',
                  automatic: true,
                  status: 'active'
              )
              adminGroup.people =[admin]
              adminGroup.save(flush: true)
          }
          else
          {
              // it already exists--update it rather than creating a new one
              if (admin.groups == null || !admin.groups.contains(adminGroup)) {
                  adminGroup.people << admin
                  adminGroup.properties = [
                      status: 'active'
                  ]
                  adminGroup.save(flush: true)
              }
          }
      }
  }
    private loadUserData(user) {

        if (user) {

            // Create All Users group if it doesn't already exist
            def allUsers = Group.findByName('All Users', [cache:true])

            if (allUsers == null) {
                allUsers = new Group(
                    name: 'All Users',
                    description: 'All Users',
                    automatic: true,
                    status: 'active'
                )

                allUsers.people = [user]
                allUsers.save(flush: true)

            } else {
                // it already exists--update it rather than creating a new one
                if (user.groups == null || !user.groups.contains(allUsers)) {
                    allUsers.people << user
                    allUsers.properties = [
                        status: 'active'
                    ]
                    allUsers.save(flush: true)
                }
            }
        }
    }
}
