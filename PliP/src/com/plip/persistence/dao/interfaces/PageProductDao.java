package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;

public interface PageProductDao {
	
	public Integer addPageProduct(PageProduct pageProduct) ;
	
	public Page getPageProduct(int idPageProduct);
	
	public void updatePageProduct(PageProduct pageProduct);
	
	public void deletePageProduct(Integer pageProductId);

}
