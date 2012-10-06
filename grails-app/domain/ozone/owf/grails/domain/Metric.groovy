package ozone.owf.grails.domain

class Metric{

	String metricTime
	String userName
	String userId
	String site
	String userAgent
	String component
	String componentId
	String instanceId
	String metricTypeId
	String widgetData

	static constraints = {
		metricTime(nullable: false, blank: false)
		userId(nullable: false,  blank: false)
		userName(nullable: false,  blank: false)
		site(maxsize: 2083)
		userAgent(nullable:false, blank: false)
		component(nullable:false, blank:false, maxSize: 200)
		componentId(nullable:false, blank:false, matches: /^[A-Fa-f\d]{8}-[A-Fa-f\d]{4}-[A-Fa-f\d]{4}-[A-Fa-f\d]{4}-[A-Fa-f\d]{12}$/)
		instanceId(nullable:false, blank:false, matches: /^[A-Fa-f\d]{8}-[A-Fa-f\d]{4}-[A-Fa-f\d]{4}-[A-Fa-f\d]{4}-[A-Fa-f\d]{12}$/)
        widgetData(nullable:true, blank: true)
	}

	static mapping = {
        //table 'owf_metric'
        cache true
	}

	def toServiceModel() {
      ServiceModelUtil.createServiceModel(this)
    }
}