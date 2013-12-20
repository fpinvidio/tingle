package com.plip.persistence.home;

// Generated Dec 20, 2013 6:41:39 PM by Hibernate Tools 4.0.0

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.plip.persistence.models.TrayProduct;

/**
 * Home object for domain model class TrayProduct.
 * @see com.plip.persistence.models.TrayProduct
 * @author Hibernate Tools
 */
public class TrayProductHome {

	private static final Log log = LogFactory.getLog(TrayProductHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(TrayProduct transientInstance) {
		log.debug("persisting TrayProduct instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TrayProduct instance) {
		log.debug("attaching dirty TrayProduct instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TrayProduct instance) {
		log.debug("attaching clean TrayProduct instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TrayProduct persistentInstance) {
		log.debug("deleting TrayProduct instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TrayProduct merge(TrayProduct detachedInstance) {
		log.debug("merging TrayProduct instance");
		try {
			TrayProduct result = (TrayProduct) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TrayProduct findById(com.plip.persistence.models.TrayProductId id) {
		log.debug("getting TrayProduct instance with id: " + id);
		try {
			TrayProduct instance = (TrayProduct) sessionFactory
					.getCurrentSession().get(
							"com.plip.persistence.models.TrayProduct", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TrayProduct instance) {
		log.debug("finding TrayProduct instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.plip.persistence.models.TrayProduct")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
