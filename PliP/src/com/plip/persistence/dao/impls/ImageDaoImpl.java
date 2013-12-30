package com.plip.persistence.dao.impls;

import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.ImageDao;
import com.plip.persistence.exceptions.ImageNotFoundException;
import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.OrderNotFoundException;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Status;

public class ImageDaoImpl implements ImageDao {
	
	public ImageDaoImpl() {
		super();
	}

	@Override
	public Long addImage(Image image) throws NullModelAttributesException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long imageID = null;
		try {
			tx = session.beginTransaction();
			if(image.validate()){
			imageID = (Long) session.save(image);
			}else throw new NullModelAttributesException();		
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
	public Image getImage(long idImage) throws ImageNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Image image = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Image where idImage = :id");
			query.setParameter("id", idImage);
			if(query.list().size()>0){
			image = (Image) query.list().get(0);
			}else{
				throw new ImageNotFoundException();
			}
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
	public ArrayList<Image> getImagesByProductId(long idProduct) throws ImageNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		ArrayList<Image> images = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Image where id_product = :idProduct");
			query.setParameter("idProduct", idProduct);
			if(query.list().size()>0){
			images = (ArrayList<Image>) query.list();
			}else{
				throw new ImageNotFoundException();
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return images;
	}

	@Override
	public void updateImage(Image image) throws ImageNotFoundException{
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Image img = (Image) session.get(Image.class, image.getIdImage());
			if(img!=null){
			img.setDescriptor(image.getDescriptor());
			img.setName(image.getName());
			img.setPath(image.getPath());
			img.setPosition(image.getPosition());
			img.setProduct(image.getProduct());
			img.setTrained(image.isTrained());
			session.update(img);
			}else{
				throw new ImageNotFoundException();
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
	public void deleteImage(long imageId){
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Image image = (Image) session.get(Image.class, imageId);
			if(image!=null){
			session.delete(image);
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
