package com.plip.persistence.dao.interfaces;

import java.util.List;

import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;

public interface PageProductDao {
	
	public Integer addPageProduct(PageProduct pageProduct) ;
	
	public List <Page> getPagesByProduct(int idProduct);
	
	public List <Product> getProductsByPage(int idPage);
	
	public void updatePageProduct(PageProduct pageProduct);
	
	public void deletePageProduct(Integer pageProductId);

}
