package com.plip.persistance.models;

// Generated Dec 15, 2013 7:23:19 PM by Hibernate Tools 3.4.0.CR1

/**
 * Image generated by hbm2java
 */
public class Image implements java.io.Serializable {

	private Long idImage;
	private Product product;
	private Position position;
	private String path;
	private String name;
	private byte[] descriptor;

	public Image() {
	}

	public Image(Product product, Position position, String path,
			byte[] descriptor) {
		this.product = product;
		this.position = position;
		this.path = path;
		this.descriptor = descriptor;
	}

	public Image(Product product, Position position, String path, String name,
			byte[] descriptor) {
		this.product = product;
		this.position = position;
		this.path = path;
		this.name = name;
		this.descriptor = descriptor;
	}

	public Long getIdImage() {
		return this.idImage;
	}

	public void setIdImage(Long idImage) {
		this.idImage = idImage;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Position getPosition() {
		return this.position;
	}

	public void setPosition(Position position) {
		this.position = position;
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

	public byte[] getDescriptor() {
		return this.descriptor;
	}

	public void setDescriptor(byte[] descriptor) {
		this.descriptor = descriptor;
	}

}
