import grails.util.GrailsUtil
import org.apache.log4j.helpers.Loader
import org.apache.log4j.xml.DOMConfigurator
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import org.apache.commons.lang.time.StopWatch
import ozone.owf.grails.domain.Metric
import java.util.Date

class BootStrap {
	
    def grailsApplication
    def sessionFactory

    def init = {servletContext ->
        def appName = grailsApplication.metadata['app.name']

        println 'BootStrap running!'
        //println "appName:${appName}"

        if (GrailsUtil.environment == 'production') {

            def log4jConfigure
            URL url = Loader.getResource("${appName}-override-log4j.xml")
            String fileName = url.toString()
            if (fileName.startsWith('file:/')) {


                File file;
                try {
                    file = new File(url.toURI());
                } catch(URISyntaxException e) {
                    file = new File(url.getPath());
                }

                if (!file.exists()) {
                    url = null
                }

                log4jConfigure = {
                    def watchTime = grailsApplication.config.watchTime
                    println "########## Found ${appName}-override-log4j.xml at: ${file.getAbsolutePath()} ${watchTime}"
                    DOMConfigurator.configureAndWatch(file.getAbsolutePath(),watchTime ? watchTime : 180000)
                }
            }
            else {
                url = null
            }
          
            if (!url) {
                url = Loader.getResource("${appName}-log4j.xml")
                log4jConfigure = {
                    println "########## Found ${appName}-log4j.xml at: ${url.toString()}"
                    DOMConfigurator.configure(url)
                }
            }

            //execute closure
            if (url) {
                try {
                    log4jConfigure()
                } catch(Throwable t) {
                    println "########## Error loading log4j configuration ${t.getMessage()}"
                    t.printStackTrace()
                }
            }
        }

        switch (GrailsUtil.environment) {
            case ["development", "testUser1", "testAdmin1"]:
            StopWatch stopWatch = new StopWatch();
            log.info('Loading development fixture data')
            stopWatch.start();
            loadDevelopmentData()
            stopWatch.stop();
            log.info('Finished Loading development fixture data:'+stopWatch)
            break;
            default:
            //do nothing
            break
        }
        println 'BootStrap finished!'
    }

    private def saveInstance(instance) {
        if (instance.save(flush:true) == null) {
            log.info "ERROR: ${instance} not saved - ${instance.errors}"
        } else {
            log.debug "${instance.class}:${instance} saved"
        }
        return instance
    }

    private def loadDevelopmentData() {
        def dbCreate = grailsApplication.config.dataSource.dbCreate
        if (dbCreate == 'create-drop' || dbCreate == 'create') {
            log.info "datasource.dbCreate = ${dbCreate} : Loading bootstrap data"

            def enabled = grailsApplication.config?.perfTest?.enabled

            def clearCacheEvery = ((grailsApplication.config?.perfTest?.clearCacheEvery && enabled)? grailsApplication.config.perfTest?.clearCacheEvery : 10);
            log.info 'clearCacheEvery: ' + clearCacheEvery

            loadMetrics()
            sessionFactory.currentSession.clear()
        }
        else {
            log.info "datasource.dbCreate = ${dbCreate} : No bootstrap data loaded"
        }
        sessionFactory.currentSession.clear()
    }
	
    private def loadMetrics(){

        saveInstance(new Metric(
            metricTime: 1335683400000,
            userId: '1',
            userName: 'Test Admin 1',
            site: 'https://localhost:8443/owf',
            userAgent: "Mozilla Firefox",
            component: 'Widget One',
            componentId: 'd6543ccf-4021-4f2a-ba69-dde451d12551',
            instanceId: 'd6543ccf-4021-4f2a-ba69-dde451d12551',
            metricTypeId: 'ozone.widget.view',
            widgetData: ''
        ))

        saveInstance(new Metric(
            metricTime: 1335683400000,
            userId: '1',
            userName: 'Test Admin 1',
            site: 'https://localhost:8443/owf',
            userAgent: "Mozilla Firefox",
            component: 'Widget Two',
            componentId: 'e65431cf-4021-4f2a-ba69-dde451d12551',
            instanceId: 'e65431cf-4021-4f2a-ba69-dde451d12551',
            metricTypeId: 'ozone.widget.view',
            widgetData: ''
        ))

        saveInstance(new Metric(
            metricTime: 1335683400000,
            userId: '1',
            userName: 'Test Admin 1',
            site: 'https://localhost:8443/owf',
            userAgent: "Mozilla Firefox",
            component: 'Nearly Empty',
            componentId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            instanceId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            metricTypeId: 'ozone.widget.view',
            widgetData: ''
        ))

        saveInstance(new Metric(
            metricTime: 1315897800000,
            userId: '1',
            userName: 'Test Admin 1',
            site: 'https://localhost:8443/owf',
            userAgent: "Mozilla Firefox",
            component: 'Nearly Empty',
            componentId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            instanceId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            metricTypeId: 'ozone.widget.view',
            widgetData: ''
        ))

        saveInstance(new Metric(
            metricTime: 1315897800000,
            userId: '1',
            userName: 'Test Admin 1',
            site: 'https://localhost:8443/owf',
            userAgent: "Mozilla Firefox",
            component: 'Nearly Empty',
            componentId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            instanceId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            metricTypeId: 'ozone.widget.view',
            widgetData: ''
        ))

        saveInstance(new Metric(
            metricTime: 1320131400000,
            userId: '1',
            userName: 'Test Admin 1',
            site: 'https://localhost:8443/owf',
            userAgent: "Mozilla Firefox",
            component: 'Nearly Empty',
            componentId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            instanceId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            metricTypeId: 'ozone.widget.view',
            widgetData: ''
        ))

        saveInstance(new Metric(
            metricTime: 1299226200000,
            userId: '1',
            userName: 'Test Admin 1',
            site: 'https://localhost:8443/owf',
            userAgent: "Mozilla Firefox",
            component: 'Nearly Empty',
            componentId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            instanceId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            metricTypeId: 'ozone.widget.view',
            widgetData: ''
        ))

        saveInstance(new Metric(
            metricTime: 1320131400000,
            userId: '1',
            userName: 'Test Admin 1',
            site: 'https://localhost:8443/owf',
            userAgent: "Mozilla Firefox",
            component: 'Nearly Empty',
            componentId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            instanceId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            metricTypeId: 'ozone.widget.view',
            widgetData: ''
        ))

        saveInstance(new Metric(
            metricTime: 1320131400000,
            userId: '2',
            userName: 'Test Admin 2',
            site: 'https://localhost:8443/owf',
            userAgent: "Mozilla Firefox",
            component: 'Nearly Empty',
            componentId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            instanceId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            metricTypeId: 'ozone.widget.view',
            widgetData: ''
        ))

        saveInstance(new Metric(
            metricTime: 1322338167000,
            userId: '2',
            userName: 'Test Admin 2',
            site: 'https://localhost:8443/owf',
            userAgent: "Mozilla Firefox",
            component: 'Nearly Empty',
            componentId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            instanceId: 'bc5435cf-4021-4f2a-ba69-dde451d12551',
            metricTypeId: 'ozone.widget.view',
            widgetData: ''
        ))
    }

	def destroy = {
    }

}


