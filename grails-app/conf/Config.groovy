import org.apache.log4j.RollingFileAppender
import org.apache.log4j.net.SyslogAppender
import grails.util.BuildSettingsHolder

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

grails.config.locations = [
                           "classpath:OzoneConfig.properties",
                           "classpath:MetricConfig.groovy"
]

println "grails.config.locations = ${grails.config.locations}"

grails.mime.file.extensions = false // enables the parsing of file extensions from URLs into the request format
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
    xml: ['text/xml', 'application/xml'],
    text: 'text-plain',
    js: 'text/javascript',
    rss: 'application/rss+xml',
    atom: 'application/atom+xml',
    css: 'text/css',
    csv: 'text/csv',
    all: '*/*',
    json: ['application/json','text/json'],
    form: 'application/x-www-form-urlencoded',
    multipartForm: 'multipart/form-data'
]
// The default codec used to encode data with ${}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
grails.converters.encoding="UTF-8"

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

server.version = appVersion
server.baseVersion = appVersion?.toString()?.split("-")[0]
//println "server.baseVersion = ${server.baseVersion}"

//databasemigration settings
grails.plugin.databasemigration.updateOnStart = true

//context to use when upgrading automatically - 'upgrade' assumes there is a database
grails.plugin.databasemigration.updateOnStartContexts = "create,upgrade"

//list changelog's to run automatically
grails.plugin.databasemigration.updateOnStartFileNames  = [
        'changelog.groovy'
]

stamp{
    audit{
        //the created and edited fields should be present or they won't get added during AST
        createdBy="createdBy" //id who created
        createdDate="createdDate" // if you want a date stamp that is not the grails default dateCreated
        editedBy="editedBy" //id who updated/edited
        editedDate="editedDate"//use this field instead of the grails default lastUpdated
    }
}


//
//PROPERTY  DEFAULT VALUE   MEANING
//uiperformance.enabled true    set to false to disable processing
//uiperformance.processImages   true    set to false to disable processing for all images
//uiperformance.processCSS  true    set to false to disable processing for all .css files
//uiperformance.processJS   true    set to false to disable processing for all .js
//uiperformance.imageExtensions 'gif', 'png', 'jpg', 'ico' (+)  specify different values to change image types that are processed
//uiperformance.minifyJs    true    set to false to disable minification for all .js
//uiperformance.minifyJsAsErrorCheck    false   set to true to minify .js files in-memory for error checking but discard
//uiperformance.continueAfterMinifyJsError  false   set to true to only warn about .js minification problems rather than failing the build
//uiperformance.minifyCss   true    set to false to disable minification for all .css
//uiperformance.minifyCssAsErrorCheck   false   set to true to minify .css files in-memory for error checking but discard
//uiperformance.continueAfterMinifyCssError false   set to true to only warn about .css minification problems rather than failing the build
//uiperformance.keepOriginals   false   set to true to keep the original uncompressed and unversioned files in the war along with the compressed/versioned files
//uiperformance.html.compress   false   set to true to enable gzip for dynamic text content
//uiperformance.html.urlPatterns    none    set to restrict dynamic text url patterns that should be processed
//uiperformance.html.debug  false   set to true to enable PJL filter debug logging
//uiperformance.html.statsEnabled   false   set to true to enable PJL filter stats tracking
//uiperformance.html.compressionThreshold   1024    sets the minimum content length for compression
//uiperformance.html.jakartaCommonsLogger   none    set the category of the commons/log4j logger to debug to

uiperformance.enabled = true
uiperformance.processImages = false
uiperformance.inclusionsForCaching = [
    //        "**/examples/*.html",
        "**/*.ico",
        "**/*.jpg",
        "**/*.png",
        "**/*.gif"
]
uiperformance.exclusions = [
        "**/help/**",
        "**/sampleWidgets/**",
        "**/jsunit/**",
        "**/js-test/**",
        "**/js-doh/**",
        "**/js-lib/**",
        "**/js-min/**"

]

//baseDir exists then use svn version num as part of the version number
def basedir = BuildSettingsHolder.settings?.baseDir
if (basedir != null) {
    uiperformance.determineVersion = {->
        def version = System.getenv('SVN_REVISION')

        if (!version)
        //if SVN_REVISION is not defined (it is typically only defined by jenkins),
        //pick a random number instead
        version = new Random().nextInt()

        if (version.toString().charAt(0) != '-' ) version = '-' + version
        uiperformance.exclusions << "**/*${server.version + version}*"
        server.version + version
    }
}

uiperformance.continueAfterMinifyJsError = true
uiperformance.keepOriginals = true
uiperformance.bundles = [
    [
        type: 'js',
        name: 'ozone-metric-widget',

        files: [
            'owf-widget-debug',
            'util/util',
            'lang/DateJs/globalization/en-US',
            '../js-lib/ext-4.0.7/ext-all-debug',
            '../js-lib/patches/BorderLayoutAnimation',
            '../js-lib/patches/GridScroller',
            'components/MessageBoxPlus',
            'components/admin/MetricsGraph',
            'components/admin/grid/MetricsGrid',
            'components/admin/MetricsPanel',
            'components/admin/TagCloud',
            'components/chart/theme/accessibility-wob',
            'components/focusable/CircularFocus',
            'components/focusable/EscCloseHelper',
            'components/focusable/Focusable',
            'components/focusable/FocusableGridPanel',
            'components/focusable/FocusableView',
            'components/focusable/FocusCatch',
            'components/layout/SearchBoxLayout',
            'components/form/field/SearchBox'
        ]
    ]
]

// set per-environment serverURL stem for creating absolute links
// and turn on gsp reloading for the 2 auto login environments
environments {
    production {

        //grails.serverURL = "https://localhost:8443/admin"
        log4j = {rootLogger ->
            //get rid of stdout logging
            rootLogger.removeAllAppenders()

            //disable the creation of stacktrace.log since we 
            //don't log anything to it
            appenders {
                'null' name: 'stacktrace'
            }

            appenders {
                rollingFile name: "${appName}StackTraceLog",
                layout:pattern(conversionPattern: '%d [%t] %-5p %c %x - %m%n'),
                maxFileSize: '1MB',
                file: "logs/${appName}-stacktrace.log"
            }

            //this configuration is only active
            //until the bootstrap phase when 
            //our log4j.xml gets loaded and overrides
            //these settings
            appenders {
                rollingFile name: 'initialLog',
                layout: pattern(conversionPattern: '%d [%t] %-5p %c %x - %m%n'),
                maxFileSize: '1MB',
                file: "logs/${appName}-initial.log"
            }

            error additivity: false, metricStackTraceLog: 'StackTrace'
            root {
                error 'initialLog'
                additivity = false
            }
        }
    }

    development {
        uiperformance.enabled = false

        alternateHostName = System.properties.alternateHostName ?: '127.0.0.1'

        log4j = {
            error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
             'org.codehaus.groovy.grails.web.pages' //  GSP
            warn 'org.mortbay.log'
            debug 'grails.app'
            info 'grails.plugin.databasemigration'
            //trace 'org.hibernate'
            //debug 'org.hibernate.SQL'
            //trace 'org.hibernate.type'
            //trace 'org.hibernate.cache'
            appenders {
                appender new RollingFileAppender(name:"${appName}StackTraceLog", maxFileSize:8192,
                    file:"logs/${appName}-stacktrace.log",
                    layout: pattern(conversionPattern: '%d [%t] %-5p %c %x - %m%n'))
                appender new SyslogAppender( name:"syslogTraceLog",
                    syslogHost:'localhost:8014',
                    facility: 'USER',
                    layout: pattern(conversionPattern: '%d [%t] %-5p %c %x - %m%n'))
            }
            root {
                debug 'stdout', "${appName}StackTraceLog", 'syslogTraceLog'
                error()
                additivity = true
            }

        }
    }

    test {
        log4j =
        {
            appenders {
                appender new RollingFileAppender(name:"${appName}TestStackTraceLog", maxFileSize:8192,
                    file:"logs/${appName}test-stacktrace.log",
                    layout: pattern(conversionPattern: '%d [%t] %-5p %c %x - %m%n'))
            }
            root {
                info "${appName}TestStackTraceLog"
                info()
                additivity = true
            }
        }
    }
}

//ssl stuff
keystore = 'certs/keystore.jks'

casSettings.useCas=true
casSettings.FullServiceURL='https://localhost:8443/cas'

grails.app.context = "/${appName}"
