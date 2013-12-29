package com.plip.persistence.dao.interfaces;

import com.plip.persistence.exceptions.ProductNotFoundException;
import com.plip.persistence.model.Product;

public interface ProductDao {
	
	public Long addProduct(Product product);
	
	public Product getProduct(long idProduct);
	
	public Product getProductByName(String name) throws ProductNotFoundException;;
	
	public void updateProduct(Product product) throws ProductNotFoundException;
	
	public void deleteProduct(long productId) ;

}
