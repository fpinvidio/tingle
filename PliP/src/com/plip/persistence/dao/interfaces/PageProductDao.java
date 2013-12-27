package com.plip.persistence.dao.interfaces;

import java.util.List;

import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;

public interface PageProductDao {
	
	public Long addPageProduct(PageProduct pageProduct) ;
	
	public List <Page> getPagesByProduct(long idProduct);
	
	public List <Product> getProductsByPage(long idPage);
	
	public void updatePageProduct(PageProduct pageProduct);
	
	public void deletePageProduct(long pageProductId);

}
