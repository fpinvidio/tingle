package com.plip.persistence.daos.interfaces;

import java.util.ArrayList;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PageNotFoundException;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.Product;

public interface PageDao {
	
	public Long addPage(Page page) throws NullModelAttributesException ;
	
	public Page getPage(long idPage) throws PageNotFoundException;
	
	public ArrayList<Page> getPagesByOrderId(long idOrder) throws PageNotFoundException;
	
	public void updatePage(Page page) throws PageNotFoundException;
	
	public void deletePage(long pageId);
	
	public ArrayList <Product> getPageProductsByOrderId ( Long idOrder );

}
