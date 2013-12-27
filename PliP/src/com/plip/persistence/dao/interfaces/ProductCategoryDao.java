package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.ProductCategory;
import com.plip.persistence.model.Status;

public interface ProductCategoryDao {
	
	public Long addProductCategory(ProductCategory productCategory);
	
	public ProductCategory getProductCategory(long idProductCategory);
	
	public void updateProductCategory(ProductCategory productCategory);
	
	public void deleteProductCategory(long productCategoryId);

}
