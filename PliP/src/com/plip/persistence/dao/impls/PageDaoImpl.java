package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.PageDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.Status;

public class PageDaoImpl implements PageDao {

	public PageDaoImpl() {
		super();
	}

	@Override
	public Long addPage(Page page) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long pageID = null;
		try {
			tx = session.beginTransaction();
			pageID = (Long) session.save(page);
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
	public Page getPage(long idPage) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Page page = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM Page where idPage = :id");
			query.setParameter("id", idPage);
			page = (Page) query.list().get(0);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return page;
	}

	@Override
	public void updatePage(Page page) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Page pag = (Page) session.get(Status.class, page.getIdPage());
			pag.setOrder(page.getOrder());
			pag.setPageImage(page.getPageImage());
			pag.setPageNumber(page.getPageNumber());
			pag.setPageProducts(page.getPageProducts());
			pag.setProductQuantity(page.getProductQuantity());
			pag.setTrays(page.getTrays());
			session.update(pag);
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
	public void deletePage(long pageId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Page page = (Page) session.get(Status.class, pageId);
			session.delete(page);
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
