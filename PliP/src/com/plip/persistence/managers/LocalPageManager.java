package com.plip.persistence.managers;

import com.plip.persistence.dao.impls.PageDaoImpl;
import com.plip.persistence.exceptions.PageNotFoundException;
import com.plip.persistence.managers.exceptions.NoPageRecievedException;
import com.plip.persistence.model.Page;

public class LocalPageManager implements PageManager{

	@Override
	public Page getLastPage() throws NoPageRecievedException, PageNotFoundException{
		/* Get Tray Page from Database */
		PageDaoImpl pageDao = new PageDaoImpl();
		Page page = new Page();
		page = pageDao.getPage(Long.valueOf(1));
		return page;
	}
}
