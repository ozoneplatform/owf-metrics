package ozone.owf.grails.test.integration

import ozone.owf.grails.controllers.MetricController
import grails.converters.JSON
import ozone.owf.grails.domain.ERoleAuthority
import ozone.owf.grails.domain.Metric

class MetricControllerTests extends OWFGroovyTestCase {
	
	def metricService
	def controller
	
	void createForTest() {
		Metric.build(
                metricTime: (new Date()).getTime().toString(),
                userName: 'testUser1',
                userId: '100',
                site: 'test site',
                userAgent: 'test',
                component: 'testWidget',
                componentId: 'b3b1d04f-97c2-4726-9575-82bb1cf1af6a',
                instanceId: '6f70bb25-16a6-c6e3-b961-3d213c4fe667',
                metricTypeId: 'ozone.widget.view'
        )
		Metric.build(
                metricTime: (new Date()).getTime().toString(),
                userName: 'testUser2',
                userId: '200',
                site: 'test site',
                userAgent: 'test',
                component: 'testWidget',
                componentId: 'b3b1d04f-97c2-4726-9575-82bb1cf1af6a',
                instanceId: 'b967294e-38d4-4a93-9bbe-9064a4550d09',
                metricTypeId: 'ozone.widget.view'
        )
	}
	
	void testList() {
		loginAsUsernameAndRole('testAdmin1', ERoleAuthority.ROLE_ADMIN.strVal)
		createForTest()
		
		controller = new ozone.owf.grails.controllers.MetricController()
		controller.metricService = metricService
		controller.request.contentType = "text/json"
		
		controller.list()

		assertNotNull JSON.parse(controller.response.contentAsString).metrics
		assertEquals '6f70bb25-16a6-c6e3-b961-3d213c4fe667', JSON.parse(controller.response.contentAsString).metrics[0].instanceId
	}
	
	void testCreate() {
		loginAsUsernameAndRole('testAdmin1', ERoleAuthority.ROLE_ADMIN.strVal)
		
		controller = new ozone.owf.grails.controllers.MetricController()
		controller.metricService = metricService
		controller.request.contentType = "text/json"
		
		controller.params.putAll([
                metricTime: (new Date()).getTime().toString(),
                userName: 'testUser1',
				userId: '1',
                site: 'test site',
                userAgent: 'test',
                component: 'testWidget',
                componentId: 'b3b1d04f-97c2-4726-9575-82bb1cf1af6a',
                instanceId: '6f70bb25-16a6-c6e3-b961-3d213c4fe667',
                metricTypeId: 'ozone.widget.view'
		])
		controller.create()

		assertNotNull  Metric.findByInstanceId('6f70bb25-16a6-c6e3-b961-3d213c4fe667')
		assertEquals '6f70bb25-16a6-c6e3-b961-3d213c4fe667', JSON.parse(controller.response.contentAsString).data[0].instanceId
	}

	void testView() {
		loginAsUsernameAndRole('testAdmin1', ERoleAuthority.ROLE_ADMIN.strVal)
		createForTest()
		
		controller = new ozone.owf.grails.controllers.MetricController()
		controller.metricService = metricService
		controller.request.contentType = "text/json"
		
		controller.view()

		assertNotNull JSON.parse(controller.response.contentAsString)
		assertEquals 2, JSON.parse(controller.response.contentAsString).results[0].users
		assertEquals 2, JSON.parse(controller.response.contentAsString).results[0].views
	}
	
	void testGraphData() {
		loginAsUsernameAndRole('testAdmin1', ERoleAuthority.ROLE_ADMIN.strVal)
		createForTest()
		
		controller = new ozone.owf.grails.controllers.MetricController()
		controller.metricService = metricService
		controller.request.contentType = "text/json"
		
    	def dateObj = new Date()
    	def year = dateObj.getYear()
    	def month = dateObj.getMonth()
    	def day = dateObj.getDate()
		controller.params.putAll([
            componentId: "b3b1d04f-97c2-4726-9575-82bb1cf1af6a",
            fromDate: (new Date(year, month, day - 1)).getTime().toString(),
            toDate: dateObj.getTime().toString()
		])
		controller.graphData()

		assertNotNull JSON.parse(controller.response.contentAsString)
		assertEquals 2, JSON.parse(controller.response.contentAsString)[0][2]
	}
	
	void testGetTagCloud() {
		loginAsUsernameAndRole('testAdmin1', ERoleAuthority.ROLE_ADMIN.strVal)
		createForTest()
		
		controller = new ozone.owf.grails.controllers.MetricController()
		controller.metricService = metricService
		controller.request.contentType = "text/json"
		
    	def dateObj = new Date()
    	def year = dateObj.getYear()
    	def month = dateObj.getMonth()
    	def day = dateObj.getDate()
		controller.params.putAll([
            fromDate: (new Date(year, month, day - 1)).getTime().toString(),
            toDate: dateObj.getTime().toString()
		])
		controller.getTagCloud()

		assertNotNull JSON.parse(controller.response.contentAsString).results
		assertEquals "b3b1d04f-97c2-4726-9575-82bb1cf1af6a", JSON.parse(controller.response.contentAsString).results[0].componentId
		assertEquals 2, JSON.parse(controller.response.contentAsString).results[0].total
	}
	
}