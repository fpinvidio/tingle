package com.plip.persistence.dao.impls;

import com.plip.persistence.dao.interfaces.TrayDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Tray;

public class TrayDaoImpl implements TrayDao {
	
	DaoManager daoManager;

	public TrayDaoImpl(DaoManager daoManager) {
		super();
		this.daoManager = daoManager;
	}

	@Override
	public Integer addTray(Tray tray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tray getTray(int idTray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateTray(Tray tray) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteTray(Tray trayId) {
		// TODO Auto-generated method stub	
	}
}
