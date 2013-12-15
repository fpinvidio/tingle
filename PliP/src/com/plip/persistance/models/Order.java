package com.plip.persistance.models;

// Generated Dec 15, 2013 7:23:19 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Order generated by hbm2java
 */
public class Order implements java.io.Serializable {

	private Long idOrders;
	private String code;
	private Date insertDate;
	private String client;
	private Set pages = new HashSet(0);

	public Order() {
	}

	public Order(String code, Date insertDate, String client, Set pages) {
		this.code = code;
		this.insertDate = insertDate;
		this.client = client;
		this.pages = pages;
	}

	public Long getIdOrders() {
		return this.idOrders;
	}

	public void setIdOrders(Long idOrders) {
		this.idOrders = idOrders;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public String getClient() {
		return this.client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Set getPages() {
		return this.pages;
	}

	public void setPages(Set pages) {
		this.pages = pages;
	}

}
