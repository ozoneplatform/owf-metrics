environments {
    production {
        dataSource {
            dbCreate = "none"
            username = ${prefs.dataSource.username}
            password = ${prefs.dataSource.password}
            driverClassName = ${prefs.dataSource.driverClassName}
            url = "jdbc:hsqldb:file:metricsDb;shutdown=true"
            pooled = true
            properties {
                minEvictableIdleTimeMillis = 180000
                timeBetweenEvictionRunsMillis = 180000
                numTestsPerEvictionRun = 3
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = true
                validationQuery = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"
            }
        }
        //enable uiperformance plugin which bundles and compresses javascript
        uiperformance.enabled = true
    }
}

beans {
	
	//This block is equivalent to using an org.springframework.beans.factory.config.PropertyOverrideConfigurer
	//See Chapter 14 of the Grails documentation for more information: http://grails.org/doc/1.1/
	
}

//databasemigration settings
grails.plugin.databasemigration.updateOnStart = false


println('MetricConfig.groovy completed successfully.')