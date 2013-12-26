package com.plip.persistence.dao.impls;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.PageProductDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;

public class PageProductDaoImpl implements PageProductDao {

	

	public PageProductDaoImpl() {
		super();
	
	}

	@Override
	public Integer addPageProduct(PageProduct pageProduct) {
		SessionFactory factory = DaoManager.createSessionFactory();
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

	@Override
	public List<Page> getPagesByProduct(int idProduct) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		List pageList = null;
		try {
			Query query = session
					.createQuery("FROM PageProduct where idProduct = :id");
			query.setParameter("id", idProduct);
			pageList = query.list();
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pageList;
	}

	@Override
	public List<Product> getProductsByPage(int idPage) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		List productList = null;
		try {
			Query query = session
					.createQuery("FROM PageProduct where idPage = :id");
			query.setParameter("id", idPage);
			productList = query.list();
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return productList;
	}

	@Override
	public void updatePageProduct(PageProduct pageProduct) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PageProduct pageProd = (PageProduct) session.get(Image.class,
					pageProduct.getIdPageProduct());
			pageProd.setQuantity(pageProduct.getQuantity());
			session.update(pageProd);
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
	public void deletePageProduct(Integer pageProductId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PageProduct pageProduct = (PageProduct) session.get(
					PageProduct.class, pageProductId);
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
}
