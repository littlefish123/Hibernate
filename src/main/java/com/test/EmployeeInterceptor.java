package com.test;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class EmployeeInterceptor extends EmptyInterceptor {
	private int updates;
	private int creates;
	private int loads;
	
	public void OnDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		// do nothing
	}
	
	public boolean onFlushDirty(Object entity, Serializable id, 
			Object[] currentState, Object[] previousState, String[] propertyNames,
			Type[] type) {
		
		if (entity instanceof Employee) {
			System.out.println("Update Operation.");
			return true;
		}
		
		return false;
	}
	
	public boolean onLoad(Object entity, Serializable id,
		      Object[] state, String[] propertyNames, Type[] types) {
		         // do nothing
		         return true;
	}
	
	public boolean onSave(Object entity, Serializable id,
		      Object[] state, String[] propertyNames, Type[] types) {
		         if ( entity instanceof Employee ) {
		            System.out.println("Create Operation");
		            return true; 
		         }
		         return false;
	}
		   
	//called before commit into database
	public void preFlush(Iterator iterator) {
	     System.out.println("preFlush");
	}
		   
	//called after committed into database
	public void postFlush(Iterator iterator) {
	     System.out.println("postFlush");
	}
}
