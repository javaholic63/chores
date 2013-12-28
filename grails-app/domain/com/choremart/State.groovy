package com.choremart

class State {

	String name
	String abbreviation
	Date dateCreated
	Date lastUpdated

	static constraints = {
		abbreviation maxSize: 2
		name maxSize: 20
	}
	static mapping = {
		sort "abbreviation"
	}
}
