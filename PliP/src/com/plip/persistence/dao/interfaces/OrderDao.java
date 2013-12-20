package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Order;

public interface OrderDao {
	
	public Integer addOrder(Order order) ;
	
	public Order getOrder(int idOrder);
	
	public void updateOrder(Order order);
	
	public void deleteOrder(Integer orderId);

}
