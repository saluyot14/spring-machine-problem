package com.qbryx.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORY")
public class Category implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String name;
	
	public Category(){}
	
	public Category(long categoryId, String name) {
		super();
		this.id = categoryId;
		this.name = name;
	}

	@Id @GeneratedValue
	@Column(name = "id")
	public long getCategoryId() {
		return id;
	}

	public void setCategoryId(long categoryId) {
		this.id = categoryId;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
