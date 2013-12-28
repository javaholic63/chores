package com.choremart

class ChoreCategory {
	
	String name
	static hasMany = [choreType : ChoreType]
	Date dateCreated
    Date lastUpdated
	
    static constraints = {
		name blank: false, unique: true, maxsize: 32
    }
	
	static mapping = {
		sort "name"
		cache usage: 'read-only'
	}
	
	String toString() {
		"chore category: " + name
	}
}
