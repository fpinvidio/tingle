package com.plip.persistence.managers;

import com.plip.persistence.dao.impls.PageDaoImpl;
import com.plip.persistence.exceptions.PageNotFoundException;
import com.plip.persistence.managers.exceptions.NoPageRecievedException;
import com.plip.persistence.model.Page;

public class LocalPageManager implements PageManager{

	@Override
	public Page getLastPage() throws NoPageRecievedException{
		/* Get Tray Page from Database */
		PageDaoImpl pageDao = new PageDaoImpl();
		Page page = new Page();
		try {
			page = pageDao.getPage(Long.valueOf(10));
		} catch (PageNotFoundException e1) {
			//e1.printStackTrace();
			System.out.println("Page not found");
			throw new NoPageRecievedException();
		}
		return page;
	}
}
