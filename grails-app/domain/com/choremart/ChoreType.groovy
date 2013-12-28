package com.choremart

class ChoreType {

	String typeName
	static belongsTo = [choreCategory : ChoreCategory]
//	Long choreCategory
	Date dateCreated
	Date lastUpdated

	static constraints = {
		typeName blank: false, maxsize: 32, unique: 'choreCategory'
	}
	
	static mapping = {
		sort "typeName"
		cache usage: 'read-only'
	}

	String toString() {
		"chore type: " + typeName
	}
}
