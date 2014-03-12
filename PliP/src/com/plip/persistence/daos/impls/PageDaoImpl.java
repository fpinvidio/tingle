package com.plip.persistence.daos.impls;

import java.util.ArrayList;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PageNotFoundException;
import com.plip.persistence.daos.interfaces.OrderDao;
import com.plip.persistence.daos.interfaces.PageDao;
import com.plip.persistence.daos.interfaces.PageProductDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;

public class PageDaoImpl implements PageDao {

	public PageDaoImpl() {
		super();
	}

	@Override
	public Long addPage(Page page) throws NullModelAttributesException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long pageID = null;
		try {
			tx = session.beginTransaction();
			if (page.validate()) {
				pageID = (Long) session.save(page);
			} else
				throw new NullModelAttributesException();
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
	public Page getPage(long idPage) throws PageNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Page page = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM Page where idPage = :id");
			query.setParameter("id", idPage);
			if(query.list().size() > 0){
			page = (Page) query.list().get(0);
			page.setPageProducts(page.getPageProducts());
			} else {
				throw new PageNotFoundException();
			}
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
	public void deleteLastPage() throws PageNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Page page = null;
		OrderDao orderDao = new OrderDaoImpl();
		PageProductDao pageProductDao = new PageProductDaoImpl();
		try {
			tx = session.beginTransaction();
			//Query query = session.createQuery("from Page ORDER by idPage DESC LIMIT 1");
			Long pageId = (Long)session.createQuery("select max(idPage) from Page").uniqueResult();
			page = getPage(pageId);
			//if(query.list().size() > 0){
			//page = (Page) query.list().get(0);
			//Long pageId =page.getIdPage();
			Set<PageProduct> pageProducts = page.getPageProducts();
			for(PageProduct pageProduct : pageProducts){
				pageProductDao.deletePageProduct(pageProduct.getIdPageProduct());
			}
			deletePage(pageId);
			orderDao.deleteOrder(page.getOrder().getIdOrder());
			//} else {
			//	throw new PageNotFoundException();
			//}
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
	public void updatePage(Page page) throws PageNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Page pag = (Page) session.get(Page.class, page.getIdPage());
			if(pag != null){
			pag.setOrder(page.getOrder());
			pag.setPageImage(page.getPageImage());
			pag.setPageNumber(page.getPageNumber());
			pag.setPageProducts(page.getPageProducts());
			pag.setProductQuantity(page.getProductQuantity());
			pag.setTrays(page.getTrays());
			session.update(pag);
			}else{
				throw new PageNotFoundException();
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
	public void deletePage(long pageId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Page page = (Page) session.get(Page.class, pageId);
			if (page != null) {
				session.delete(page);
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
	public ArrayList<Page> getPagesByOrderId(long idOrder)
			throws PageNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		ArrayList<Page> pages = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("Select p FROM Page p join p.order o where (o.idOrders = :idOrder)");
			query.setParameter("idOrder", Long.valueOf(idOrder));
			if (query.list().size() > 0) {
				pages = (ArrayList<Page>) query.list();
			} else {
				throw new PageNotFoundException();
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return pages;
	}
	
	public ArrayList <Product> getPageProductsByOrderId ( Long idOrder ) {
		ArrayList<Product> products = new ArrayList();
		try {
			ArrayList<Page> pageList = getPagesByOrderId(idOrder);
			for (Page page : pageList){
				Set<PageProduct> pageProducts = page.getPageProducts();			
				for (PageProduct next : pageProducts){
					products.add(next.getProduct());
				}
			}
		} catch (PageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		return products; 	
	}
	
}
