package com.plip.persistence.dao.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.persistence.dao.interfaces.PlipRoleDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.PlipRole;
import com.plip.persistence.model.Status;

public class PlipRoleDaoImpl implements PlipRoleDao {
	
	public PlipRoleDaoImpl() {
		super();
	}

	@Override
	public Long addRole(PlipRole role) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long roleID = null;
		try {
			tx = session.beginTransaction();
			roleID = (Long) session.save(role);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return roleID;
	}

	@Override
	public PlipRole getRole(Long idRole) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		PlipRole plipRole = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM PlipRole where idPlipRole = :id");
			query.setParameter("id", idRole);
			plipRole = (PlipRole) query.list().get(0);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return plipRole;
	}

	@Override
	public void updateRole(PlipRole role) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PlipRole plipRole = (PlipRole) session.get(PlipRole.class,
					role.getIdPlipRole());
			plipRole.setName(role.getName());
			plipRole.setPlipUsers(role.getPlipUsers());
			plipRole.setDescription(role.getDescription());
			session.update(plipRole);
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
	public void deleteRole(Integer roleId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PlipRole plipRole = (PlipRole) session.get(PlipRole.class, roleId);
			session.delete(plipRole);
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
