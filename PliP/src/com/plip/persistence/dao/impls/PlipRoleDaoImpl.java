package com.plip.persistence.dao.impls;

import com.plip.persistence.dao.interfaces.PlipRoleDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.PlipRole;

public class PlipRoleDaoImpl implements PlipRoleDao {
	
	DaoManager daoManager;

	public PlipRoleDaoImpl(DaoManager daoManager) {
		super();
		this.daoManager = daoManager;
	}

	@Override
	public Integer addRole(PlipRole role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlipRole getRole(int idRole) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateRole(PlipRole role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRole(Integer roleId) {
		// TODO Auto-generated method stub
		
	}
}