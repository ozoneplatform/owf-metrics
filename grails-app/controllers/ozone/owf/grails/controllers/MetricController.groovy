package ozone.owf.grails.controllers

import grails.converters.JSON
import ozone.owf.grails.OwfException

class MetricController extends BaseOwfRestController{
	def metricService
	def modelName = 'metric'

	def create = {
		def statusCode
		def jsonResult

		try{
			def result = metricService.create(params)
			statusCode = 200
			jsonResult = result as JSON
		}
		catch (OwfException owe){
			handleError(owe)
			statusCode = owe.exceptionType.normalReturnCode
			jsonResult = "Error during create: " + owe.exceptionType.generalMessage + " " + owe.message
		}

		renderResult(jsonResult, statusCode)
	}

	def list = {
		def statusCode
		def jsonResult

		try{
			def result = metricService.list(params)
			statusCode = 200
			jsonResult = result as JSON
		}
		catch (OwfException owe){
			handleError(owe)
			statusCode = owe.exceptionType.normalReturnCode
			jsonResult = "Error during list: " + owe.exceptionType.generalMessage + " " + owe.message
		}

		renderResult(jsonResult, statusCode)
	}
	
	def view = {
		def results
		def statusCode
		def jsonResult

		try{
			results = metricService.view(params)
			statusCode = 200
			jsonResult = results as JSON
		}
		catch(OwfException owe){
			handleError(owe)
			statusCode = owe.exceptionType.normalReturnCode
			jsonResult = "Error during view: " + owe.exceptionType.generalMessage + " " + owe.message
		}

		renderResult(jsonResult, statusCode)
	}

	def graphData = {
		def results
		def statusCode
		def jsonResult

		try{
			results = metricService.graphData(params)
			statusCode = 200
			jsonResult = results as JSON
		}
		catch(OwfException owe){
			handleError(owe)
			statusCode = owe.exceptionType.normalReturnCode
			jsonResult = "Error during graphData: " + owe.exceptionType.generalMessage + " " + owe.message
		}

		renderResult(jsonResult, statusCode)
	}

	def getTagCloud = {

		def results
		def statusCode
		def jsonResult

		try{
			results = metricService.getTagCloud(params)
			statusCode = 200
			jsonResult = results as JSON
		}
		catch(OwfException owe){
			handleError(owe)
			statusCode = owe.exceptionType.normalReturnCode
			jsonResult = "Error during get tag cloud: " + owe.exceptionType.generalMessage + " " + owe.message
		}

		renderResult(jsonResult, statusCode)
		
	}

}