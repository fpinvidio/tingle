package com.plip.persistence.model;

// Generated Dec 26, 2013 6:25:52 PM by Hibernate Tools 4.0.0

/**
 * Image generated by hbm2java
 */
public class Image implements java.io.Serializable {

	private Long idImage;
	private String path;
	private String name;
	private long idProduct;
	private byte[] descriptor;
	private int idPosition;
	private boolean trained;

	public Image() {
	}

	public Image(String path, long idProduct, byte[] descriptor,
			int idPosition, boolean trained) {
		this.path = path;
		this.idProduct = idProduct;
		this.descriptor = descriptor;
		this.idPosition = idPosition;
		this.trained = trained;
	}

	public Image(String path, String name, long idProduct, byte[] descriptor,
			int idPosition, boolean trained) {
		this.path = path;
		this.name = name;
		this.idProduct = idProduct;
		this.descriptor = descriptor;
		this.idPosition = idPosition;
		this.trained = trained;
	}

	public Long getIdImage() {
		return this.idImage;
	}

	public void setIdImage(Long idImage) {
		this.idImage = idImage;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getIdProduct() {
		return this.idProduct;
	}

	public void setIdProduct(long idProduct) {
		this.idProduct = idProduct;
	}

	public byte[] getDescriptor() {
		return this.descriptor;
	}

	public void setDescriptor(byte[] descriptor) {
		this.descriptor = descriptor;
	}

	public int getIdPosition() {
		return this.idPosition;
	}

	public void setIdPosition(int idPosition) {
		this.idPosition = idPosition;
	}

	public boolean isTrained() {
		return this.trained;
	}

	public void setTrained(boolean trained) {
		this.trained = trained;
	}

}
