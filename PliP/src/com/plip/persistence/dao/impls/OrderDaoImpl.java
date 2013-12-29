package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.OrderDao;
import com.plip.persistence.exceptions.OrderNotFoundException;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Order;
import com.plip.persistence.model.Status;

public class OrderDaoImpl implements OrderDao {
	
	public OrderDaoImpl() {
		super();
	}

	@Override
	public Long addOrder(Order order) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long orderID = null;
		try {
			tx = session.beginTransaction();
			orderID = (Long) session.save(order);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return orderID;
	}

	@Override
	public Order getOrder(long idOrder) throws OrderNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Order order = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Order where idOrders = :id");
			query.setParameter("id", idOrder);
			if(query.list().size() > 0){
			order = (Order) query.list().get(0);
			}else{
				throw new OrderNotFoundException();
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return order;
	}

	@Override
	public void updateOrder(Order order) throws OrderNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Order ord = (Order) session.get(Order.class, order.getIdOrders());
			if(ord != null){
			ord.setClient(order.getClient());
			ord.setCode(order.getCode());
			ord.setInsertDate(order.getInsertDate());
			ord.setPages(order.getPages());
			session.update(ord);
			}else{
				throw new OrderNotFoundException();
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}		
		
	}

	@Override
	public void deleteOrder(long orderId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Order order = (Order) session.get(Order.class, orderId);
			if(order != null){
			 session.delete(order);	
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}
