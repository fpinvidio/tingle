package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.ProductDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Product;
import com.plip.persistence.model.Status;

public class ProductDaoImpl implements ProductDao{

	
	
	public ProductDaoImpl() {
		super();
		
	}

	@Override
	public Long addProduct(Product product) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long productID = null;
		try {
			tx = session.beginTransaction();
			productID = (Long) session.save(product);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return productID;
	}

	@Override
	public Product getProduct(int idProduct) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Product product = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Product where idProduct = :id");
			query.setParameter("id", idProduct);
			product = (Product) query.list().get(0);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return product;
	}

	@Override
	public void updateProduct(Product product) {
		// TODO Auto-generated method stub
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Product prod = (Product) session.get(Product.class,
					product.getIdProduct());
			
			prod.setCode(product.getCode());
			prod.setDescription(product.getDescription());
			prod.setEnabled(product.isEnabled());
			prod.setImageNumber(product.getImageNumber());
			prod.setLaboratory(product.getLaboratory());
			prod.setName(product.getName());
			prod.setProductCategory(product.getProductCategory());
			prod.setWeight(product.getWeight());
			
			session.update(product);
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
	public void deletePosition(Integer productId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Product product = (Product) session.get(Product.class, productId);
			session.delete(product);
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
