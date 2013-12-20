package com.plip.persistence.model;

// Generated Dec 20, 2013 6:41:39 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

/**
 * PageImage generated by hbm2java
 */
public class PageImage implements java.io.Serializable {

	private long idPageImage;
	private String path;
	private Set pages = new HashSet(0);

	public PageImage() {
	}

	public PageImage(long idPageImage, String path) {
		this.idPageImage = idPageImage;
		this.path = path;
	}

	public PageImage(long idPageImage, String path, Set pages) {
		this.idPageImage = idPageImage;
		this.path = path;
		this.pages = pages;
	}

	public long getIdPageImage() {
		return this.idPageImage;
	}

	public void setIdPageImage(long idPageImage) {
		this.idPageImage = idPageImage;
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