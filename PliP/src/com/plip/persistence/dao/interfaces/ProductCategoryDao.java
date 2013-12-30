package com.plip.persistence.dao.interfaces;

import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.ProductCategoryNotFoundException;
import com.plip.persistence.model.ProductCategory;

public interface ProductCategoryDao {
	
	public Long addProductCategory(ProductCategory productCategory) throws NullModelAttributesException;
	
	public ProductCategory getProductCategory(long idProductCategory) throws ProductCategoryNotFoundException;
	
	public ProductCategory getProductCategoryByName(String name) throws ProductCategoryNotFoundException;
	
	public void updateProductCategory(ProductCategory productCategory) throws ProductCategoryNotFoundException;
	
	public void deleteProductCategory(long productCategoryId);

}
