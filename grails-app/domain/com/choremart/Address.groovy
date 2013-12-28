package com.choremart


class Address {
	
	String address1
	String address2
	String city
	State state
	String zip

    static constraints = {
		address1(maxSize:50, blank:false)
		address2(maxSize:50, nullable:true)
		city(maxSize:50, blank:false)
		state(maxSize:2, blank:false)
		zip(maxSize:9, blank:false)
    }
	
	String toString() {
		address1 + ( address2 ? ", $address2" : '') + ", $city, $state $zip"
	}
}
