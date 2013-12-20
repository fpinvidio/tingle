package com.plip.persistence.dao.impls;

import com.plip.persistence.dao.interfaces.OrderDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Order;

public class OrderDaoImpl implements OrderDao {
	
	DaoManager daoManager;

	public OrderDaoImpl(DaoManager daoManager) {
		super();
		this.daoManager = daoManager;
	}

	@Override
	public Integer addOrder(Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order getOrder(int idOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOrder(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteOrder(Integer orderId) {
		// TODO Auto-generated method stub
		
	}

}
