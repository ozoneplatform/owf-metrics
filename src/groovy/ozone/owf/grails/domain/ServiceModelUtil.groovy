package ozone.owf.grails.domain

import ozone.owf.grails.services.model.ServiceModel
import ozone.owf.grails.services.model.GroupServiceModel

import ozone.owf.grails.util.OWFDate
import ozone.owf.grails.services.model.PersonServiceModel
import ozone.owf.grails.services.model.MetricServiceModel
/**
 *
 * @author ntabernero
 * @version $Id: $
 * @since May 27, 2010 3:55:50 PM
 */
public class ServiceModelUtil {

  public static ServiceModel createServiceModel(obj, params = [:]) {
    ServiceModel model
    Class clazz = obj?.class

    switch (clazz) {

      case Person:
        Person domain = (Person) obj
        model = new PersonServiceModel(
                id: domain.id,
                username: domain.username,
                userRealName: domain.userRealName,
                email: domain.email ?: '',
                lastLogin: domain.lastLogin ? domain.lastLogin.getTime() : null
        )
        break

      case Group:
        Group domain = (Group) obj
        model = new GroupServiceModel(
                id: domain.id,
                name: domain.name,
                description: domain.description,
                email: domain.email,
                automatic: domain.automatic,
                totalUsers: params.totalUsers ? params.totalUsers : 0,
                totalWidgets: params.totalWidgets ? params.totalWidgets : 0,
                status: domain.status
        )
        break

      case Metric:
        Metric domain = (Metric) obj
        model = new MetricServiceModel(
                id: domain.id,
                metricTime: domain.metricTime,
                userName: domain.userName,
                userId: domain.userId,
                site: domain.site,
				userAgent: domain.userAgent,
				component: domain.component,
				componentId: domain.componentId,
				instanceId: domain.instanceId,
				metricTypeId: domain.metricTypeId,
				widgetData: domain.widgetData
        )
        break

      default:
        model = null
        break
    }


    return model
  }
}
