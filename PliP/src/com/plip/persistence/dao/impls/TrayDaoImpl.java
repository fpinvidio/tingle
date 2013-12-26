package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.TrayDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Position;
import com.plip.persistence.model.Status;
import com.plip.persistence.model.Tray;

public class TrayDaoImpl implements TrayDao {
	
	

	public TrayDaoImpl() {
		super();
		
	}

	@Override
	public Integer addTray(Tray tray) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Integer trayID = null;
		try {
			tx = session.beginTransaction();
			trayID = (Integer) session.save(tray);
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
	public Tray getTray(int idTray) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Tray tray = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Tray where idTray = :id");
			query.setParameter("id", idTray);
			tray = (Tray) query.list().get(0);
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
	public void updateTray(Tray tray) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Tray tr = (Tray) session.get(Tray.class,
					tray.getIdTray());
			tr.setCode(tray.getCode());
			tr.setPage(tray.getPage());
			session.update(tr);
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
	public void deleteTray(Tray trayId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Tray tray = (Tray) session.get(Tray.class, trayId);
			session.delete(tray);
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
