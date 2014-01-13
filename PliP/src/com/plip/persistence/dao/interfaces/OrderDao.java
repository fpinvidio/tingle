package com.plip.persistence.dao.interfaces;

import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.OrderNotFoundException;
import com.plip.persistence.model.Order;

public interface OrderDao {
	
	public Long addOrder(Order order) throws NullModelAttributesException ;
	
	public Order getOrderById(long idOrder) throws OrderNotFoundException;
	
	public Order getOrderByCode(String code) throws OrderNotFoundException;
	
	public void updateOrder(Order order) throws OrderNotFoundException;
	
	public void deleteOrder(long orderId);

}
