package com.plip.persistence.dao.interfaces;

import com.plip.persistence.exceptions.OrderNotFoundException;
import com.plip.persistence.model.Order;

public interface OrderDao {
	
	public Long addOrder(Order order) ;
	
	public Order getOrder(long idOrder) throws OrderNotFoundException;
	
	public void updateOrder(Order order) throws OrderNotFoundException;
	
	public void deleteOrder(long orderId);

}
