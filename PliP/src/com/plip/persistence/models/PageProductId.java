package com.plip.persistence.models;

// Generated Dec 20, 2013 6:23:53 PM by Hibernate Tools 4.0.0

/**
 * PageProductId generated by hbm2java
 */
public class PageProductId implements java.io.Serializable {

	private long idPage;
	private long idProduct;

	public PageProductId() {
	}

	public PageProductId(long idPage, long idProduct) {
		this.idPage = idPage;
		this.idProduct = idProduct;
	}

	public long getIdPage() {
		return this.idPage;
	}

	public void setIdPage(long idPage) {
		this.idPage = idPage;
	}

	public long getIdProduct() {
		return this.idProduct;
	}

	public void setIdProduct(long idProduct) {
		this.idProduct = idProduct;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PageProductId))
			return false;
		PageProductId castOther = (PageProductId) other;

		return (this.getIdPage() == castOther.getIdPage())
				&& (this.getIdProduct() == castOther.getIdProduct());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getIdPage();
		result = 37 * result + (int) this.getIdProduct();
		return result;
	}

}
