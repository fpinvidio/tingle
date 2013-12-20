package com.plip.persistence.dao.impls;

import com.plip.persistence.dao.interfaces.PlipUserDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.PlipUser;

public class PlipUserDaoImpl implements PlipUserDao {
	
	DaoManager daoManager;

	public PlipUserDaoImpl(DaoManager daoManager) {
		super();
		this.daoManager = daoManager;
	}

	@Override
	public Integer addUser(PlipUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlipUser getUser(int idUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUser(PlipUser user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(Integer userId) {
		// TODO Auto-generated method stub
		
	}

}
