package com.test;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.AnnotationException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;

/*
 * 

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

create table CERTIFICATE (
   id INT NOT NULL,
   certificate_name VARCHAR(30) default NULL,
   employee_id INT default NULL,
   PRIMARY KEY (id)
);

project -> properties -> Java Build Path -> add odbc10.jar
 set annotation with Employee field mapping (can be removed employee.hbm.xml)
 set secondary cache = EhCache (EmployeeCache)
 set query-level cache to true
 set hibernate batch size = 50
 Implement Hiberate Interceptors 
 https://www.tutorialspoint.com/hibernate/hibernate_interceptors.htm

*/

public class ManageEmployee {
	
	private static SessionFactory factory;
	
	public void addBatch() {
	      Session session = factory.withOptions().interceptor(new EmployeeInterceptor()).openSession();		
// 		  Session session = factory.openSession();
		  Transaction tx = null;
	      Integer employeeID = null;
	      
	      try {
	          tx = session.beginTransaction();
	          for (int i=0; i < 10000; i++) {
	        	  String fname = "FirstName " + i;
	        	  String lname = "LastName" + i;
	        	  int salary = 100;
	        	  Employee employee = new Employee(fname, lname, salary);
		          employeeID = (Integer) session.save(employee); 
		          if (i % 50 ==0) {
		        	  session.flush();
		        	  session.clear();
		          }
	          }

	          tx.commit();
	       } catch (HibernateException e) {
	          if (tx!=null) tx.rollback();
	          e.printStackTrace(); 
	       } finally {
	          session.close(); 
	       }
	       return;
	}
	
	public Integer addEmployee(String fname,String lname, int salary) {
	      Session session = factory.withOptions().interceptor(new EmployeeInterceptor()).openSession();		
//		  Session session = factory.openSession();
	      Transaction tx = null;
	      Integer employeeID = null;
	      
	      try {
	          tx = session.beginTransaction();
	          Employee employee = new Employee(fname, lname, salary);
	          employeeID = (Integer) session.save(employee); 
	          tx.commit();
	       } catch (HibernateException e) {
	          if (tx!=null) tx.rollback();
	          e.printStackTrace(); 
	       } finally {
	          session.close(); 
	       }
	       return employeeID;
	}
	
	public Integer addEmployee(String fname,String lname, int salary, Set cert) {
	    Session session = factory.withOptions().interceptor(new EmployeeInterceptor()).openSession();		
//		Session session = factory.openSession();
		Transaction tx = null;
		Integer employeeID = null;
		
		try {
			tx = session.beginTransaction();
			Employee employee = new Employee(fname,lname,salary);
			employee.setCertificates(cert);
			employeeID = (Integer)session.save(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return employeeID;
		
	}
	
	public void listEmployee( ){
//	      Session session = factory.openSession();
	      Session session = factory.withOptions().interceptor(new EmployeeInterceptor()).openSession();	      
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         String sql = "Select * from EMPLOYEE";
	         NativeQuery query = session.createNativeQuery(sql, Employee.class);
	         query.setCacheable(true);
	         query.setCacheRegion("EmployeeCache");
	         List employees = query.list();
	       

	         for (Iterator iterator = employees.iterator(); iterator.hasNext();){
	            Employee employee = (Employee) iterator.next();
/*	            
	            System.out.print("First Name: " + employee.getFirstName()); 
	            System.out.print("  Last Name: " + employee.getLastName()); 
	            System.out.println("  Salary: " + employee.getSalary());
*/
	            Set certificates = employee.getCertificates();
	            for (Iterator iterator2 = certificates.iterator(); iterator2.hasNext();) {
	            	Certificate certName = (Certificate) iterator2.next();
	            	System.out.println("Certificate : " + certName.getName());
	            }
	         }
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	   }
	
	
	 public void updateEmployee(Integer EmployeeID, int salary ){
//	      Session session = factory.openSession();
	      Session session = factory.withOptions().interceptor(new EmployeeInterceptor()).openSession();		 
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         Employee employee = (Employee)session.get(Employee.class, EmployeeID); 
	         employee.setSalary( salary );
			 session.update(employee); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	   }
	 
	 
	 public void deleteEmployee(Integer EmployeeID){
//		 Session session = factory.openSession();		 
	      Session session = factory.withOptions().interceptor(new EmployeeInterceptor()).openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         Employee employee = (Employee)session.get(Employee.class, EmployeeID); 
	         session.delete(employee); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	   }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Instant start = Instant.now();
		
		try {
			/* AnnotationConfiguration is deprecatd since Hibernate 3.6
			factory = new AnnotationConfiguration().
					configure().
					addPackage("com.test").
					addAnnotatedClass(Employee.class).
					buildSessionFactory();
			 */
			
			factory = new Configuration().
					configure().buildSessionFactory();
			
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionfactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		ManageEmployee ME =  new ManageEmployee();
		HashSet set1 = new HashSet();
		set1.add(new Certificate("MCA"));
		set1.add(new Certificate("MBA"));
		set1.add(new Certificate("PRINCE2"));
		set1.add(new Certificate("PMP"));
		
		Integer empID1 = ME.addEmployee("Zara", "Ali", 10000, set1);
		
		HashSet set2 = new HashSet();
		set1.add(new Certificate("BCA"));
		set1.add(new Certificate("BBA"));
		set1.add(new Certificate("BRINCE2"));
		set1.add(new Certificate("BMP"));
		
		Integer empID2 = ME.addEmployee("Selina", "Fish", 50000, set2);
		Integer empID3 = ME.addEmployee("Richard", "Rich", 20000, set2);
		
		ME.listEmployee();
		
//		ME.addBatch();
		
		ME.updateEmployee(empID1,500);
		
		ME.deleteEmployee(empID3);
		
		ME.listEmployee();
		
		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).toSeconds();
		System.out.printf("Elapsed Time: %d\n", timeElapsed);
	}

}
