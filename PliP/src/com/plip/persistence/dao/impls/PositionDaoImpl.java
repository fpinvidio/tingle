package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.PositionDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Order;
import com.plip.persistence.model.Position;
import com.plip.persistence.model.Status;

public class PositionDaoImpl implements PositionDao {

	public PositionDaoImpl() {
		super();		
	}

	@Override
	public Long addPosition(Position position) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long positionID = null;
		try {
			tx = session.beginTransaction();
			positionID = (Long) session.save(position);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return positionID;
	}

	@Override
	public Position getPosition(int idPosition) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Position position = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Position where idPosition = :id");
			query.setParameter("id", idPosition);
			position = (Position) query.list().get(0);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return position;
	}

	@Override
	public void updatePosition(Position position) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Position pos = (Position) session.get(Position.class,
					position.getIdPosition());
			pos.setAngle(position.getAngle());
			pos.setFace(position.getFace());
			pos.setImages(position.getImages());
			session.update(pos);
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
	public void deletePosition(Integer positionId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Position position = (Position) session.get(Position.class, positionId);
			session.delete(position);
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
