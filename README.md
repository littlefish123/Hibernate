Test HIBERNATE 5.4.10
=====================
Oracle 19c
Java Version 11
JDBC Version 10

1) 
create table EMPLOYEE (
   id INT NOT NULL,
   first_name VARCHAR(20) default NULL,
   last_name  VARCHAR(20) default NULL,
   salary     INT  default NULL,
   PRIMARY KEY (id)
);
grant ALL PRIVILEGES on employee to system;

CREATE SEQUENCE HIBERNATE_SEQUENCE
START WITH 1
MAXVALUE 999999999999999999999999999
MINVALUE 1
NOCYCLE
CACHE 20
NOORDER;

grant ALL PRIVILEGES on HIBERNATE_SEQUENCE to system;

add CERTIFICATE table to test one-to-many relationship
create table CERTIFICATE (
   id INT NOT NULL,
   certificate_name VARCHAR(30) default NULL,
   employee_id INT default NULL,
   PRIMARY KEY (id)
);

2) Add odbc10.jar  
project->properties-> Java Build Path -> add odbc10.jar, ucp.jar

3) Add Employee.hbm.xml with class mapping 
   
   class name should "<package name>.<class name>"
   
   Oracle does NOT support "native" generator. Need to use "sequence" and create "HIBERATE_SEQUENCE" in Oracle database.

3) set Annotation to Employee field mapping. Cannot delete Employee.hbm.xml in this version.

4) set secondary cachae with EhCache called "EmployeeCache" in Employee.hbm.xml. (read-write cache usage)
   
   Setup ehcache.xml parameters
   
   In the listEmployee() function of ManageEmployee.java, add the following :
   
             query.setCacheable(true);	
			 
	         query.setCacheRegion("EmployeeCache");
			 

5) set hibernate batch size 50 in the hiberate.cfg.xml
  
   Add addBatch() function to ManageEmployee.java
   
   Call session.flush() and session.clear() for every 50 records added.
   
6) Implement Hiberate Interceptors (EmployeeInterceptors)

    An interceptor function (OnSave, OnLoad, OnFlushDirty, OnDelete) is implemented in the EmployeeInterceptor.java
	
	Change session creation with interceptor as follows :
	Session sessio<<<<<<< .mine
			 
	         query.setCacheRegion("EmployeeCache");
			 

5) set hibernate batch size 50 in the hiberate.cfg.xml
  
   Add addBatch() function to ManageEmployee.java
   
   Call session.flush() and session.clear() for every 50 records added.
   
6) Implement Hiberate Interceptors (EmployeeInterceptors)

    An interceptor function (OnSave, OnLoad, OnFlushDirty, OnDelete) is implemented in the EmployeeInterceptor.java
	
	Change session creation with interceptor as follows :
	Session session = factory.withOptions().interceptor(new EmployeeInterceptor()).openSession();

