package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Page;

public interface PageDao {
	
	public Long addPage(Page page) ;
	
	public Page getPage(int idPage);
	
	public void updatePage(Page page);
	
	public void deletePage(Integer pageId);

}
