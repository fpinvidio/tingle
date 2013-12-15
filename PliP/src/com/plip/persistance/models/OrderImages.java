package com.plip.persistance.models;

// Generated Dec 15, 2013 7:23:19 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * OrderImages generated by hbm2java
 */
public class OrderImages implements java.io.Serializable {

	private long idOrderImages;
	private String path;
	private Set pages = new HashSet(0);

	public OrderImages() {
	}

	public OrderImages(long idOrderImages, String path) {
		this.idOrderImages = idOrderImages;
		this.path = path;
	}

	public OrderImages(long idOrderImages, String path, Set pages) {
		this.idOrderImages = idOrderImages;
		this.path = path;
		this.pages = pages;
	}

	public long getIdOrderImages() {
		return this.idOrderImages;
	}

	public void setIdOrderImages(long idOrderImages) {
		this.idOrderImages = idOrderImages;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Set getPages() {
		return this.pages;
	}

	public void setPages(Set pages) {
		this.pages = pages;
	}

}
