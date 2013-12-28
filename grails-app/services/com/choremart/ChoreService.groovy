package com.choremart

import grails.validation.ValidationException

class ChoreService {
	
	static transactional = true
	
	def save(choreInstance,address) {
		
		if (!address.save()) {
			println "Exception caught in service: ${address.errors}"
			throw new ValidationException("Chore is not valid", choreInstance.errors)
		}

		choreInstance.address = address
		if (!choreInstance.save()) {
			println "Exception caught in service: ${choreInstance.errors}"
			throw new ValidationException("Chore is not valid", choreInstance.errors)
		}			
	}

    def update(choreInstance) {
		Chore.withTransaction { status ->
            def result = [:]
 
            def fail = { Map m ->
                status.setRollbackOnly()
                if(result.bookInstance && m.field) 
                    result.choreInstance.errors.rejectValue(m.field, m.code)
                result.error = [ code: m.code, args: ["Chore", choreInstance.id] ]
                return result
            }
 
			result.choreInstance = choreInstance
 
            if(!result.choreInstance)
                return fail(code:"default.not.found")
 
            // Optimistic locking check.
            if(choreInstance.version) {
                if(result.choreInstance.version > choreInstance.version.toLong())
                    return fail(field:"version", code:"default.optimistic.locking.failure")
            }
 
//            result.bookInstance.properties = params
// 
//            if(result.bookInstance.hasErrors() || !result.bookInstance.save())
//                return fail(code:"default.update.failure")
 
            // Success.
            return result
 
        } //end withTransaction
    }  // end update()
}
