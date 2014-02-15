package com.plip.persistence.daos.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.ProductNotFoundException;
import com.plip.persistence.daos.interfaces.ProductDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.Product;

public class ProductDaoImpl implements ProductDao{

	public ProductDaoImpl() {
		super();		
	}

	@Override
	public Long addProduct(Product product) throws NullModelAttributesException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long productID = null;
		try {
			tx = session.beginTransaction();
			if(product.validate()){
			productID = (Long) session.save(product);
			}else throw new NullModelAttributesException();
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
	public Product getProduct(long idProduct)  throws ProductNotFoundException{
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Product product = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Product where idProduct = :id");
			query.setParameter("id", idProduct);
//			inner join Image on Image.idProduct = Product.idProduct
			product = (Product) query.list().get(0);
			product.setImages(product.getImages());
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
	public void updateProduct(Product product) throws ProductNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Product prod = (Product) session.get(Product.class,
					product.getIdProduct());
			if(prod != null){
			prod.setCode(product.getCode());
			prod.setDescription(product.getDescription());
			prod.setEnabled(product.isEnabled());
			prod.setImageNumber(product.getImageNumber());
			prod.setLaboratory(product.getLaboratory());
			prod.setName(product.getName());
			prod.setProductCategory(product.getProductCategory());
			prod.setWeight(product.getWeight());
			
			session.update(prod);
			}else{
				throw new ProductNotFoundException();
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
	public void deleteProduct(long productId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Product product = (Product) session.get(Product.class, productId);
			if(product != null){
			session.delete(product);
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
	public Product getProductByName(String name) throws ProductNotFoundException{
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Product product = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM Product where name = :name");
			query.setParameter("name", name);
			if(query.list().size() > 0){
			product = (Product) query.list().get(0);
			}else{
				throw new ProductNotFoundException();
			}
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
}
