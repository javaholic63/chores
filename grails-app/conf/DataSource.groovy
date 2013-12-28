dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
	loggingSql = true
	dbunitXmlType = "flat" // dbunit-operator data file type: 'flat' or 'structured'
	orderTables = false // resolve table dependencies and order tables? (if true: dbunit-operator is slower)

}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
			url = "jdbc:h2:mem:devDb;MVCC=TRUE"
			//url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            initialData = "data/state.xml, data/choreCategory.xml, data/choreType.xml" // 1-n dbunit-operator Flat-XML or XML data files
            initialOperation = "CLEAN_INSERT" // dbunit-operator operation
        }
    }
    test {
        dataSource {
            dbCreate = "update"
			url = "jdbc:h2:mem:devDb;MVCC=TRUE"
            //url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            initialData = "data/state.xml" // 1-n dbunit-operator Flat-XML or XML data files
            initialOperation = "CLEAN_INSERT" // dbunit-operator operation
        }
    }
    production {
        dataSource {
            dbCreate = "update"
			url = "jdbc:h2:mem:devDb;MVCC=TRUE"
            //url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            pooled = true
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1"
            }
        }
    }
}
