package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.StatusDao;
import com.plip.persistence.exceptions.StatusNotFoundException;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Status;

public class StatusDaoImpl implements StatusDao {

	public StatusDaoImpl() {
		super();		
	}

	@Override
	/* Method to add a new Plip Status */
	public Long addStatus(Status status) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long statusID = null;
		try {
			tx = session.beginTransaction();
			statusID = (Long) session.save(status);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return statusID;
	}

	@Override
	/* Method to GET a Plip System Status */
	public Status getStatus(long idStatus) throws StatusNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Status status = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Status where idStatus = :id");
			query.setParameter("id", idStatus);
			if(query.list().size() > 0){
			status = (Status) query.list().get(0);
			}else{
				throw new StatusNotFoundException();
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return status;
	}

	@Override
	/* Method to UPDATE a Plip System Status */
	public void updateStatus(Status status) throws StatusNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Status stat = (Status) session.get(Status.class,
					status.getIdStatus());
			if(stat != null){
			stat.setDescription(status.getDescription());
			session.update(stat);
			}else{
				throw new StatusNotFoundException();
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
	/* Method to DELETE a possible system Status */
	public void deleteStatus(long statusId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Status status = (Status) session.get(Status.class, statusId);
			if(status != null){
			session.delete(status);
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
