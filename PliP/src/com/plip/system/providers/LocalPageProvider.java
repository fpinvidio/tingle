package com.plip.system.providers;

import com.plip.exceptions.persistence.NoPageRecievedException;
import com.plip.exceptions.persistence.PageNotFoundException;
import com.plip.persistence.daos.impls.PageDaoImpl;
import com.plip.persistence.model.Page;

public class LocalPageProvider implements PageProvider{

	@Override
	public Page getLastPage() throws NoPageRecievedException, PageNotFoundException{
		/* Get Tray Page from Database */
		PageDaoImpl pageDao = new PageDaoImpl();
		Page page = new Page();
		page = pageDao.getPage(Long.valueOf(14));
		return page;
	}
}
