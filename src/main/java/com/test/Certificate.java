package com.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CERTIFICATE")
public class Certificate {
	@Id @GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public Certificate() {}
	
	public Certificate(String name) {
		this.name = name;
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this.getClass().equals(obj.getClass()))
			return true;
		
		Certificate obj2 = (Certificate)obj;
		if ((this.id == obj2.getId()) && (this.name.equals(obj2.getName())))
			return true;
		
		return false;
	}
	
	public int hashCode() {
		int tmp=0;
		tmp = (id + name).hashCode();
		return tmp;
	}

}
