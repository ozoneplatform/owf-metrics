package ozone.owf.grails.domain
/**
 * Authority domain class.
 */

class Role {

	static hasMany = [people: Person]

	/** description */
	String description
	/** ROLE String */
	String authority

	static constraints = {
		authority(blank: false, unique: true)
		description()
	}

    static mappings = {
      //table 'owf_role'

      cache true
      people cache:true
    }
    
    def toServiceModel() {
      ServiceModelUtil.createServiceModel(this)
    }
}
