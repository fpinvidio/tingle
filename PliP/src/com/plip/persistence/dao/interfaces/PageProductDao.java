package com.plip.persistence.dao.interfaces;

import java.util.List;

import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.PageProductNotFoundException;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;

public interface PageProductDao {
	
	public Long addPageProduct(PageProduct pageProduct) throws NullModelAttributesException;
	
	public List <Page> getPagesByProduct(long idProduct) throws PageProductNotFoundException;
	
	public List <Product> getProductsByPage(long idPage) throws PageProductNotFoundException;
	
	public void updatePageProduct(PageProduct pageProduct) throws PageProductNotFoundException;
	
	public void deletePageProduct(long pageProductId);

}
