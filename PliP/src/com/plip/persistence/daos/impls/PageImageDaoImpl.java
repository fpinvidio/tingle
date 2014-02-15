package com.plip.persistence.daos.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PageImageNotFoundException;
import com.plip.persistence.daos.interfaces.PageImageDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.PageImage;

public class PageImageDaoImpl implements PageImageDao {

	@Override
	public Long addPageImage(PageImage pageImage)
			throws NullModelAttributesException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long pageID = null;
		try {
			tx = session.beginTransaction();
			if(pageImage.validate()){
			pageID = (Long) session.save(pageImage);
			}else throw new NullModelAttributesException();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pageID;
	}

	@Override
	public PageImage getPageImage(long idPageImage)
			throws PageImageNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		PageImage pageImage = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM page_image where idPageImage = :id");
			query.setParameter("id", idPageImage);
			if(query.list().size() > 0){
			pageImage = (PageImage) query.list().get(0);
			}else{
				throw new PageImageNotFoundException();
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pageImage;
	}

	@Override
	public void updatePageImage(PageImage pageImage)
			throws PageImageNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PageImage page = (PageImage) session.get(PageImage.class, pageImage.getIdPageImage());
			if(page != null){
			page.setPath(pageImage.getPath());
			session.update(page);
			}else{
				throw new PageImageNotFoundException();
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
	public void deletePageImage(long pageImageId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PageImage pageImage = (PageImage) session.get(PageImage.class, pageImageId);
			if(pageImage != null){
			session.delete(pageImage);
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
