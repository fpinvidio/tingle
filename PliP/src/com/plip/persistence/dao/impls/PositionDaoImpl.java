package com.plip.persistence.dao.impls;

import com.plip.persistence.dao.interfaces.PositionDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Position;

public class PositionDaoImpl implements PositionDao {
	
	DaoManager daoManager;

	public PositionDaoImpl(DaoManager daoManager) {
		super();
		this.daoManager = daoManager;
	}

	@Override
	public Integer addPosition(Position position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position getPosition(int idPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePosition(Position position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePosition(Integer positionId) {
		// TODO Auto-generated method stub
		
	}

}
