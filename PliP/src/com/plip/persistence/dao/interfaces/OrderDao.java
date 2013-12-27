package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Order;

public interface OrderDao {
	
	public Long addOrder(Order order) ;
	
	public Order getOrder(long idOrder);
	
	public void updateOrder(Order order);
	
	public void deleteOrder(long orderId);

}
