package com.plip.persistence.dao.interfaces;

import java.util.ArrayList;

import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.PageNotFoundException;
import com.plip.persistence.model.Page;

public interface PageDao {
	
	public Long addPage(Page page) throws NullModelAttributesException ;
	
	public Page getPage(long idPage) throws PageNotFoundException;
	
	public ArrayList<Page> getPagesByOrderId(long idOrder) throws PageNotFoundException;
	
	public void updatePage(Page page) throws PageNotFoundException;
	
	public void deletePage(long pageId);

}
