package com.plip.persistence.daos.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PositionNotFoundException;
import com.plip.persistence.daos.interfaces.PositionDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Position;

public class PositionDaoImpl implements PositionDao {

	public PositionDaoImpl() {
		super();		
	}

	@Override
	public Long addPosition(Position position) throws NullModelAttributesException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long positionID = null;
		try {
			tx = session.beginTransaction();
			if(position.validate()){
			positionID = (Long) session.save(position);
			}else throw new NullModelAttributesException();
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
	public Position getPosition(long idPosition) throws PositionNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Position position = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Position where idPosition = :id");
			query.setParameter("id", idPosition);
			if(query.list().size() > 0){
			position = (Position) query.list().get(0);
			}else{
				throw new PositionNotFoundException();
			}
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
	public void updatePosition(Position position) throws PositionNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Position pos = (Position) session.get(Position.class,
					position.getIdPosition());
			if(pos != null){
			pos.setAngle(position.getAngle());
			pos.setFace(position.getFace());
			pos.setImages(position.getImages());
			session.update(pos);
			}else{
				throw new PositionNotFoundException();
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
	public void deletePosition(long positionId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Position position = (Position) session.get(Position.class, positionId);
			if(position != null){
			session.delete(position);
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
