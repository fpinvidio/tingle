package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.OrderDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Order;
import com.plip.persistence.model.Status;

public class OrderDaoImpl implements OrderDao {
	
	DaoManager daoManager;

	public OrderDaoImpl(DaoManager daoManager) {
		super();
		this.daoManager = daoManager;
	}

	@Override
	public Integer addOrder(Order order) {
		SessionFactory factory = daoManager.initiateSession();
		Session session = factory.openSession();
		Transaction tx = null;
		Integer orderID = null;
		try {
			tx = session.beginTransaction();
			orderID = (Integer) session.save(order);
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
	public Order getOrder(int idOrder) {
		SessionFactory factory = daoManager.initiateSession();
		Session session = factory.openSession();
		Transaction tx = null;
		Order order = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Order where idOrder = :id");
			query.setParameter("id", idOrder);
			order = (Order) query.list().get(0);
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
	public void updateOrder(Order order) {
		SessionFactory factory = daoManager.initiateSession();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Order ord = (Order) session.get(Order.class, order.getIdOrders());
			ord.setClient(order.getClient());
			ord.setCode(order.getCode());
			ord.setInsertDate(order.getInsertDate());
			ord.setPages(order.getPages());
			session.update(ord);
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
	public void deleteOrder(Integer orderId) {
		SessionFactory factory = daoManager.initiateSession();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Order order = (Order) session.get(Order.class, orderId);
			session.delete(order);
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
