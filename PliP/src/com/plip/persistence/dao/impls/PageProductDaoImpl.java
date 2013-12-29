package com.plip.persistence.dao.impls;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.PageProductDao;
import com.plip.persistence.exceptions.PageProductNotFoundException;
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
	public Long addPageProduct(PageProduct pageProduct) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long pageProductID = null;
		try {
			tx = session.beginTransaction();
			pageProductID = (Long) session.save(pageProduct);
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
	public List<Page> getPagesByProduct(long idProduct) throws PageProductNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		List pageList = null;
		try {
			Query query = session
					.createQuery("FROM PageProduct where idProduct = :id");
			query.setParameter("id", idProduct);
			if(query.list().size() > 0){
			pageList = query.list();
			}else{
				throw new PageProductNotFoundException();
			}
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
	public List<Product> getProductsByPage(long idPage) throws PageProductNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		List productList = null;
		try {
			Query query = session
					.createQuery("FROM PageProduct where idPage = :id");
			query.setParameter("id", idPage);
			if(query.list().size() > 0){
			productList = query.list();
			}else{
				throw new PageProductNotFoundException();
			}
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
	public void updatePageProduct(PageProduct pageProduct) throws PageProductNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PageProduct pageProd = (PageProduct) session.get(Image.class,
					pageProduct.getIdPageProduct());
			if(pageProd != null){
			pageProd.setQuantity(pageProduct.getQuantity());
			session.update(pageProd);
			}else{
				throw new PageProductNotFoundException();
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
	public void deletePageProduct(long pageProductId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PageProduct pageProduct = (PageProduct) session.get(
					PageProduct.class, pageProductId);
			if(pageProduct != null){
			session.delete(pageProduct);
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
