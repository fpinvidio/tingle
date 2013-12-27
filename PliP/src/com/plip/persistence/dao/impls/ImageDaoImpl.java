package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.ImageDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Status;

public class ImageDaoImpl implements ImageDao {
	
	public ImageDaoImpl() {
		super();
	}

	@Override
	public Long addImage(Image image) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long imageID = null;
		try {
			tx = session.beginTransaction();
			imageID = (Long) session.save(image);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return imageID;
	}

	@Override
	public Image getImage(int idImage) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Image image = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Image where idImage = :id");
			query.setParameter("id", idImage);
			image = (Image) query.list().get(0);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return image;
	}

	@Override
	public void updateImage(Image image) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Image img = (Image) session.get(Image.class, image.getIdImage());
			img.setDescriptor(image.getDescriptor());
			img.setName(image.getName());
			img.setPath(image.getPath());
			img.setPosition(image.getPosition());
			img.setProduct(image.getProduct());
			img.setTrained(image.isTrained());
			session.update(img);
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
	public void deleteImage(Integer imageId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Image image = (Image) session.get(Image.class, imageId);
			session.delete(image);
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
