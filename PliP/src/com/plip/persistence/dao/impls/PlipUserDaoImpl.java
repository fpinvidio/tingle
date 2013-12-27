package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.PlipUserDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.PlipUser;
import com.plip.persistence.model.Status;

public class PlipUserDaoImpl implements PlipUserDao {
	
	public PlipUserDaoImpl() {
		super();	
	}

	@Override
	public Long addUser(PlipUser user) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long userID = null;
		try {
			tx = session.beginTransaction();
			userID = (Long) session.save(user);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return userID;
	}

	@Override
	public PlipUser getUser(int idUser) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		PlipUser plipUser = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM PlipUser where idPlipUser = :id");
			query.setParameter("id", idUser);
			plipUser = (PlipUser) query.list().get(0);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return plipUser;
	}

	@Override
	public void updateUser(PlipUser user) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PlipUser plipUser = (PlipUser) session.get(PlipUser.class,
					user.getIdPlipUser());
			plipUser.setLastName(user.getLastName());
			plipUser.setName(user.getName());
			plipUser.setPassword(user.getPassword());
			plipUser.setPlipRole(user.getPlipRole());
			plipUser.setUsername(user.getUsername());
			
			session.update(plipUser);
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
	public void deleteUser(Integer userId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PlipUser user = (PlipUser) session.get(PlipUser.class, userId);
			session.delete(user);
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
