package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.TrayDao;
import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.TrayNotFoundException;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Position;
import com.plip.persistence.model.Status;
import com.plip.persistence.model.Tray;

public class TrayDaoImpl implements TrayDao {
	
	public TrayDaoImpl() {
		super();	
	}

	@Override
	public Long addTray(Tray tray) throws NullModelAttributesException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long trayID = null;
		try {
			tx = session.beginTransaction();
			if(tray.validate()){
			trayID = (Long) session.save(tray);
			}else throw new NullModelAttributesException();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return trayID;
	}

	@Override
	public Tray getTray(long idTray) throws TrayNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Tray tray = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Tray where idTray = :id");
			query.setParameter("id", idTray);
			if(query.list().size() > 0){
			tray = (Tray) query.list().get(0);
			}else{
				throw new TrayNotFoundException();
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return tray;
	}

	@Override
	public void updateTray(Tray tray) throws TrayNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Tray tr = (Tray) session.get(Tray.class,
					tray.getIdTray());
			if(tr != null){
			tr.setCode(tray.getCode());
			tr.setPage(tray.getPage());
			session.update(tr);
			}else{
				throw new TrayNotFoundException();
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
	public void deleteTray(long trayId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Tray tray = (Tray) session.get(Tray.class, trayId);
			if(tray != null){
			session.delete(tray);
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
