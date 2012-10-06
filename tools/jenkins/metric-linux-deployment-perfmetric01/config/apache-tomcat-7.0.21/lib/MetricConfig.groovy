environments {
    production {
		dataSource {
            pooled = true
            dbCreate = "none"
            driverClassName = "com.mysql.jdbc.Driver"
            url="jdbc:mysql://perfowfdb01.goss.owfgoss.org:3306/owf_metric"
            username = "owf_metric"
            password = "owf_metric"
            dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
            properties {
                minEvictableIdleTimeMillis = 180000
                timeBetweenEvictionRunsMillis = 180000
                numTestsPerEvictionRun = 3
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = true
                validationQuery = "SELECT 1"
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


println('Metric.groovy completed successfuly.')
