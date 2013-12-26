package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.PageProductDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;
import com.plip.persistence.model.Status;

public class PageProductDaoImpl implements PageProductDao {
	
	DaoManager daoManager;

	public PageProductDaoImpl(DaoManager daoManager) {
		super();
		this.daoManager = daoManager;
	}

	@Override
	public Integer addPageProduct(PageProduct pageProduct) {
		SessionFactory factory = daoManager.initiateSession();
		Session session = factory.openSession();
		Transaction tx = null;
		Integer pageProductID = null;
		try {
			tx = session.beginTransaction();
			pageProductID = (Integer) session.save(pageProduct);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pageProductID;
	}
	

	//@Override
	/*public Page getPageProduct(int idPage , int idProduct) {
		/*SessionFactory factory = daoManager.initiateSession();
		Session session = factory.openSession();
		Transaction tx = null;
		PageProduct pageProduct = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM PageProduct where idPage = :id && idProduct = :id");
			query.setParameter("id", idPage);
			pageProduct = (PageProduct) query.list().get(0);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pageProduct;
	}*/

	@Override
	public void updatePageProduct(PageProduct pageProduct) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePageProduct(Integer pageProductId) {
		SessionFactory factory = daoManager.initiateSession();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PageProduct pageProduct = (PageProduct) session.get(PageProduct.class, pageProductId);
			session.delete(pageProduct);
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
	public Page getPageProduct(int idPageProduct) {
		// TODO Auto-generated method stub
		return null;
	}
}
