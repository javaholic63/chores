package com.choremart

import java.text.DateFormat
import java.text.SimpleDateFormat

import grails.converters.*

import grails.validation.ValidationException

import org.springframework.dao.DataIntegrityViolationException


class ChoreController {
	
//	def scaffold = Chore
	
	def choreService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
		println "ChoreController.list(). params are:"
		params.each {k,v ->
			 println "params.$k = $v"
		}
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		println "chore list..."
		Chore.list(params).each { println "list: $it" }
		println "list done."
        [choreInstanceList: Chore.list(params), choreInstanceTotal: Chore.count()]
    }

    def create() {
		
		println "ChoreController.create(). params are:"
		params.each {k,v ->
			 println "params.$k = $v"
		}
		
		
		
//		def prevChore = Chore.read(1)

		def choreId = Chore.executeQuery("select max(id) from Chore c where c.user.id = ?", 1L)
		println "\nchoreId $choreId"
		def chore
		if (choreId && choreId[0]) {
			println "choreId = $choreId"
			chore = Chore.read(choreId[0])
			chore.startDate = null
			chore.endDate = null
		} else {
		println "New chore"
			chore = new Chore()
			chore.discard()
		}
		chore.user = ChoreUser.get(1L) // xxx
		println "For testing, user = ${chore.user}"
		
        [choreInstance: chore]
    }
	
	def save() {
		println "\nChoreController.save(). params are:"
		params.sort().each {k,v ->
			 println "params.$k = $v"
		}
		def choreInstance = new Chore(params)
		
		
		def address = new Address(params.address)
		println "\nAddress = $address"
		
		choreInstance.user = ChoreUser.get(1L)
		println "\nchoreInstance = $choreInstance"
//		choreInstance.properties = params
		println "\nChoreUsers:"
		ChoreUser.list().each { println "ChoreUser $it" }

		try {
			choreService.save(choreInstance,address)
			println "Save OK"
		} catch (Exception e) {
		println "Exception caught on save(): ${choreInstance.errors}"
			render view: "create", model: [choreInstance: choreInstance]
			return
		}
		

		flash.message = message(code: 'default.created.message', args: [message(code: 'chore.label', default: 'Chore'), choreInstance.id])
//		redirect(action: "show", id: choreInstance.id)
		chain(action: "show", params: [id: choreInstance.id])
	}

    def show() {
		println "ChoreController.show(). params are:"
		params.each {k,v ->
			 println "params.$k = $v"
		}
//		println "ChoreController.show(). chainModel are:"
//		chainModel.each {k,v ->
//			 println "chainModel.$k = $v"
//		}
//		def model = chainModel.myModel
        def choreInstance = Chore.get(params.id)
        if (!choreInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'chore.label', default: 'Chore'), params.id])
            redirect(action: "list")
            return
        }

        [choreInstance: choreInstance]
    }

    def edit() {
		println "ChoreController.edit(). params are:"
		params.each {k,v ->
			 println "params.$k = $v"
		}
        def choreInstance = Chore.get(params.id)
        if (!choreInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'chore.label', default: 'Chore'), params.id])
            redirect(action: "list")
            return
        }

        [choreInstance: choreInstance]
    }

//    def update() {
//        def choreInstance = Chore.get(params.id)
//        if (!choreInstance) {
//            flash.message = message(code: 'default.not.found.message', args: [message(code: 'chore.label', default: 'Chore'), params.id])
//            redirect(action: "list")
//            return
//        }
//
//        if (params.version) {
//            def version = params.version.toLong()
//            if (choreInstance.version > version) {
//                choreInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
//                          [message(code: 'chore.label', default: 'Chore')] as Object[],
//                          "Another user has updated this Chore while you were editing")
//                render(view: "edit", model: [choreInstance: choreInstance])
//                return
//            }
//        }
//
//        choreInstance.properties = params
//
//        if (!choreInstance.save(flush: true)) {
//            render(view: "edit", model: [choreInstance: choreInstance])
//            return
//        }
//
//		flash.message = message(code: 'default.updated.message', args: [message(code: 'chore.label', default: 'Chore'), choreInstance.id])
//        redirect(action: "show", id: choreInstance.id)
//    }
	
	
	def update() {
		println "ChoreController.update(). params are:"
		params.each {k,v ->
			 println "params.$k = $v"
		}
		def choreInstance = Chore.get( params.id )
		choreInstance.properties = params
		
		def result = choreService.update(choreInstance)
 
		if(!result.error) {
			flash.message = g.message(code: "default.update.success", args: ["Chore", params.id])
			redirect(action:show, id: params.id)
			return
		}
 
		if(result.error.code == "default.not.found") {
			flash.message = g.message(code: result.error.code, args: result.error.args)
			redirect(action:list)
			return
		}
 
		render(view:'edit', model:[bookInstance: result.choreInstance.attach()])
	}
	

    def delete() {
		println "ChoreController.delete(). params are:"
		params.each {k,v ->
			 println "params.$k = $v"
		}
        def choreInstance = Chore.get(params.id)
        if (!choreInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'chore.label', default: 'Chore'), params.id])
            redirect(action: "list")
            return
        }

        try {
            choreInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'chore.label', default: 'Chore'), params.id])
            redirect(action: "list")
        }
        catch (Exception e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'chore.label', default: 'Chore'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
	
	def showCategoryType() {
		println("ShowCategoryType()")
		println("params = $params")
//		def choreTypeList = ChoreType.findAllByChoreCategoryId(params.id.toLong())
		def choreTypeList
		println "Selecting chore category ${params.id.toLong()}"
		try {		
			def query = ChoreType.where {
				choreCategory.id == params.id.toLong()
			}
			choreTypeList = query.list()
		} catch(Exception ex){
			ex.printStackTrace();	
			println "exception caught " + ex		
		}
		println "choreType list: $choreTypeList"
		render(template: "choreType", model: [choreType: choreTypeList])
	}
	
	
}
