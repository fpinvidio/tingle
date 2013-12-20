package com.plip.persistence.home;

// Generated Dec 20, 2013 6:41:39 PM by Hibernate Tools 4.0.0

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.plip.persistence.models.PlipUser;

/**
 * Home object for domain model class PlipUser.
 * @see com.plip.persistence.models.PlipUser
 * @author Hibernate Tools
 */
public class PlipUserHome {

	private static final Log log = LogFactory.getLog(PlipUserHome.class);

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

	public void persist(PlipUser transientInstance) {
		log.debug("persisting PlipUser instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PlipUser instance) {
		log.debug("attaching dirty PlipUser instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PlipUser instance) {
		log.debug("attaching clean PlipUser instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PlipUser persistentInstance) {
		log.debug("deleting PlipUser instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PlipUser merge(PlipUser detachedInstance) {
		log.debug("merging PlipUser instance");
		try {
			PlipUser result = (PlipUser) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PlipUser findById(java.lang.Long id) {
		log.debug("getting PlipUser instance with id: " + id);
		try {
			PlipUser instance = (PlipUser) sessionFactory.getCurrentSession()
					.get("com.plip.persistence.models.PlipUser", id);
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

	public List findByExample(PlipUser instance) {
		log.debug("finding PlipUser instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.plip.persistence.models.PlipUser")
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
