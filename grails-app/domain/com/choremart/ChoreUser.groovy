package com.choremart

import groovy.transform.ToString

@ToString(includeNames = true)
class ChoreUser {

	String userId
	String userName
	String password
	Address address
	Date dateCreated
	Date lastUpdated

	static constraints = {
	}
}
