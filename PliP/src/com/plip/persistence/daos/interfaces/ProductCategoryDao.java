package com.plip.persistence.daos.interfaces;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.ProductCategoryNotFoundException;
import com.plip.persistence.model.ProductCategory;

public interface ProductCategoryDao {
	
	public Long addProductCategory(ProductCategory productCategory) throws NullModelAttributesException;
	
	public ProductCategory getProductCategory(long idProductCategory) throws ProductCategoryNotFoundException;
	
	public ProductCategory getProductCategoryByName(String name) throws ProductCategoryNotFoundException;
	
	public void updateProductCategory(ProductCategory productCategory) throws ProductCategoryNotFoundException;
	
	public void deleteProductCategory(long productCategoryId);

}
