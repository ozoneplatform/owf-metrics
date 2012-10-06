package ozone.owf.grails.services

import grails.converters.JSON
import ozone.owf.grails.domain.Metric
import ozone.owf.grails.OwfException
import ozone.owf.grails.OwfExceptionTypes
import org.hibernate.CacheMode
import org.codehaus.groovy.grails.web.json.*
// import org.joda.time

class MetricService {

	def create(params) {
		def metrics = []

		if (params.data) {
			def json = JSON.parse(params.data)

			if (json instanceof List) {
				metrics = json
			}
			else {
				metrics << json
			}
		}
		else {
			//no embedded json data assume one group to be updated it's params are directly on the params
			metrics << params
		}
		//create each group
		def results = metrics.collect {
			createMetric(it)
		}

		[success: true, data: results.flatten()]

	}

	def createMetric(params) {
	    def metric = new Metric(
			metricTime: params.metricTime,
			userId: params.userId,
			userName: params.userName,
			site: params.site,
			userAgent: params.userAgent,
			component: params.component,
			componentId: params.componentId,
			instanceId: params.instanceId,
			metricTypeId: params.metricTypeId,
			widgetData: params.widgetData
	    )
	
	    metric.validate()
	    if (metric.hasErrors()) {
			throw new OwfException(message: 'A fatal validation error occurred during the creation of a metric. Params: ' + params.toString() + ' Validation Errors: ' + metric.errors.toString(),
	              exceptionType: OwfExceptionTypes.Validation)
	    }
	
	    metric.save(flush: true, failOnError: true)
	    return metric
	}
	
	def list(params) {
	
	    def criteria = Metric.createCriteria()
	    def opts = [:]
	    if (params?.offset) opts.offset = (params.offset instanceof String ? Integer.parseInt(params.offset) : params.offset)
	    if (params?.max) opts.max = (params.max instanceof String ? Integer.parseInt(params.max) : params.max)
	
	    def results = criteria.list(opts) {
	
			//sorting -- only single sort
			if (params?.sort) {
				order(params.sort, params?.order?.toLowerCase() ?: 'asc')
			}
			else {
				//default sort
				order('metricTime', params?.order?.toLowerCase() ?: 'asc')
			}
			cache(true)
	    }
	
	    [metrics: results, results: results.totalCount]
	}

    def view(params) {
      def criteria = Metric.createCriteria()
      def opts = [:]
      if (params?.offset) opts.offset = (params.offset instanceof String ? Integer.parseInt(params.offset) : params.offset)
      if (params?.max) opts.max = (params.max instanceof String ? Integer.parseInt(params.max) : params.max)

      def rc = Metric.createCriteria().list {
        projections {
          //componentId is the unique key, component may be repeated multiple times per id
          //to make sure we don't split counts for widgets that have changed names only choose one name to be returned
          max('component')
          groupProperty('componentId')
        }

        eq("metricTypeId", params.metricType ?: "ozone.widget.view")
        if (params.fromDate && params.toDate) {
            between("metricTime", params.fromDate, params.toDate)
        }
		if (params.component) {
			ilike("component", "%" + params.component + "%")
		}
		if (params.componentId) {
			ilike("componentId", params.componentId)
		}
      }

      def results = criteria.list() {

        projections {
          //componentId is the unique key, component may be repeated multiple times per id
          //to make sure we don't split counts for widgets that have changed names only choose one name to be returned
          max('component', 'name')
          groupProperty('componentId')
          count('componentId','views')
          countDistinct('userId','users')
        }

        eq("metricTypeId", params.metricType ?: "ozone.widget.view")
        if (params.fromDate && params.toDate) {
            between("metricTime", params.fromDate, params.toDate)
        }
		if (params.component) {
			ilike("component", "%" + params.component + "%")
		}
		if (params.componentId) {
			ilike("componentId", params.componentId)
		}

        //paging params
        if (opts.offset != null) {
          firstResult(opts.offset)
        }

        if (opts.max != null) {
          maxResults(opts.max)
        }

        //sorting -- only single sort
        if (params?.sort) {
			def sortName = params.sort
			if (params.sort.equals("component")) {
				sortName = "name"
			}
			order(sortName, params?.order?.toLowerCase() ?: 'asc')
        }
        else {
          //default sort
          order('name', params?.order?.toLowerCase() ?: 'asc')
        }
      }

      def data = results.collect {
        [
          component: it[0],
          componentId: it[1],
          views: it[2],
          users: it[3]
        ]
      }

      [results: data, total: rc.size()]
    }

	// TODO rework; current code is temporary
	def graphData(params) {

	    def criteria = Metric.createCriteria()
	    def opts = [:]
	    if (params?.offset) opts.offset = (params.offset instanceof String ? Integer.parseInt(params.offset) : params.offset)
	    if (params?.max) opts.max = (params.max instanceof String ? Integer.parseInt(params.max) : params.max)
		
		def monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
		def groupedDates = []
		def map
		def metrics

		if(!params.componentId){
			return []
		}
		
	    metrics = criteria.list(opts) {
			eq("componentId", params.componentId)
			between("metricTime", params.fromDate, params.toDate)
		    order('metricTime', params?.order?.toLowerCase() ?: 'desc')
		    cache(true)
	    }

		metrics.each { metric ->
			def date = new Date(Long.parseLong(metric.metricTime))
			def year = date.getYear() + 1900
			def month = monthNames[date.getMonth()]
			def index = month + year

			if (map == null) {
				map = [(index): [year, month, 1]]
			} else if (map[index] == null){ 
				map[index] = [year, month, 1]
			} else {
				map[index][2] = map[index][2] + 1
			}
		}

		def timestampFrom = Long.parseLong(params.fromDate);
		def timestampTo = Long.parseLong(params.toDate);
		def fromDate = new Date(timestampFrom)
		def toDate = new Date(timestampTo)

		def fromMonth = fromDate.getMonth()
		def fromYear = fromDate.getYear() + 1900

		def toMonth = toDate.getMonth()
		def toYear = toDate.getYear() + 1900

		// calculates the number of months between the from and to dates
		def totalMonths = (toYear - fromYear) * 12 + (toMonth - fromMonth) + 1
		def currentMonth = fromMonth
		def currentYear = fromYear
		for (int i = 0; i < totalMonths; i++) {
			if(currentMonth == 12){
				currentMonth = 0
				currentYear++
			}
			
			def month = monthNames[currentMonth]
			def index = month + currentYear
			if (map == null) {
				groupedDates.add([currentYear, month, 0])
			} else if (map[index] == null) {
				groupedDates.add([currentYear, month, 0])
			} else {
				groupedDates.add(map[index])
			}
			
			currentMonth++
		}
		
		return groupedDates
	}

	def getTagCloud(params){
		
	    def criteria = Metric.createCriteria()
	    def opts = [:]
	    if (params?.offset) opts.offset = (params.offset instanceof String ? Integer.parseInt(params.offset) : params.offset)
	    if (params?.max) opts.max = (params.max instanceof String ? Integer.parseInt(params.max) : params.max)
	
		// Collect view metrics
	    def metrics = criteria.list(opts) {
			eq("metricTypeId", "ozone.widget.view")
			if (params.fromDate && params.toDate) {
				between("metricTime", params.fromDate, params.toDate)
			}
			if (params.component) {
				ilike("component", "%" + params.component + "%")
			}
		    order('component', params?.order?.toLowerCase() ?: 'asc')
		    cache(true)
	    }
		
		// Tally metrics
		def processedMetrics = []
		def map
		metrics.each { metric ->
			def index = metric.componentId

			if (map == null) {
				map = [(index): [metric.componentId, metric.component, 1]]
			} else if (map[index] == null){ 
				map[index] = [metric.componentId, metric.component, 1]
			} else {
				map[index][2] = map[index][2] + 1
			}
		}
		
		// Reformat the array to only contain the widget name and view amount
		metrics.each { metric ->
			if (map[metric.componentId]) {
				processedMetrics.add([componentId: map[metric.componentId][0], component: map[metric.componentId][1], total: map[metric.componentId][2]])
				map.remove(metric.componentId)
			}
		}
		
		return [results: processedMetrics]
	}
}