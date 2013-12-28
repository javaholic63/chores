package com.choremart

import groovy.transform.ToString

@ToString(includeNames=true)
class Chore {

	ChoreUser user
	String title
	ChoreType choreType
	String description
	Address address
	Date startDate
	Date endDate
	Date dateCreated
	Date lastUpdated
	String duration
	String notes

	static constraints = {
		title(blank:false)
		choreType(blank:false)
		description(maxSize:1000)
		duration(blank:false)
		notes(maxSize:1000)
	}
}
