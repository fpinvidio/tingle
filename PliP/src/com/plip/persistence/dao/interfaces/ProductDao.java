package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Product;

public interface ProductDao {
	
	public Long addProduct(Product product);
	
	public Product getProduct(long idProduct);
	
	public Product getProductByName(String name);
	
	public void updateProduct(Product product);
	
	public void deleteProduct(long productId);

}
