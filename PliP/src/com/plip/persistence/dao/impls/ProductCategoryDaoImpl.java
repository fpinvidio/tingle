package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.ProductCategoryDao;
import com.plip.persistence.exceptions.ProductCategoryNotFoundException;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.PlipUser;
import com.plip.persistence.model.ProductCategory;
import com.plip.persistence.model.Status;

public class ProductCategoryDaoImpl implements ProductCategoryDao{
	
	public ProductCategoryDaoImpl() {
		super();	
	}

	@Override
	public Long addProductCategory(ProductCategory productCategory) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long productCategoryID = null;
		try {
			tx = session.beginTransaction();
			productCategoryID = (Long) session.save(productCategory);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return productCategoryID;
	}

	@Override
	public ProductCategory getProductCategory(long idProductCategory) throws ProductCategoryNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		ProductCategory productCategory = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM ProductCategory where idProductType = :id");
			query.setParameter("id", idProductCategory);
			if(query.list().size()!=0){
			productCategory = (ProductCategory) query.list().get(0);
			}else{
				throw new ProductCategoryNotFoundException();
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return productCategory;
	}

	@Override
	public void updateProductCategory(ProductCategory productCategory) throws ProductCategoryNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ProductCategory prodCat = (ProductCategory) session.get(ProductCategory.class,
					productCategory.getIdProductType());
			if(prodCat!=null){
			prodCat.setDescription(productCategory.getDescription());
			prodCat.setProducts(productCategory.getProducts());		
			session.update(prodCat);
			}else{
				throw new ProductCategoryNotFoundException();
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
	public void deleteProductCategory(long productCategoryId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ProductCategory productCategory = (ProductCategory) session.get(ProductCategory.class, productCategoryId);
			if(productCategory!=null){
			session.delete(productCategory);
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
