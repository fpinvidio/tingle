package com.plip.persistence.daos.interfaces;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.ProductNotFoundException;
import com.plip.persistence.model.Product;

public interface ProductDao {
	
	public Long addProduct(Product product) throws NullModelAttributesException;
	
	public Product getProduct(long idProduct)  throws ProductNotFoundException;
	
	public Product getProductByName(String name) throws ProductNotFoundException;;
	
	public void updateProduct(Product product) throws ProductNotFoundException;
	
	public void deleteProduct(long productId) ;

}
