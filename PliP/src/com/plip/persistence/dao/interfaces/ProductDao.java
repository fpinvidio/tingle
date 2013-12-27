package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Product;

public interface ProductDao {
	
	public Long addProduct(Product product);
	
	public Product getProduct(int idProduct);
	
	public void updateProduct(Product product);
	
	public void deletePosition(Integer productId);

}
