package com.plip.persistence.dao.impls;

import com.plip.persistence.dao.interfaces.PageDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Page;

public class PageDaoImpl implements PageDao {
	
	DaoManager daoManager;

	public PageDaoImpl(DaoManager daoManager) {
		super();
		this.daoManager = daoManager;
	}

	@Override
	public Integer addPage(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page getPage(int idPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePage(Page page) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePage(Integer pageId) {
		// TODO Auto-generated method stub
		
	}

}
