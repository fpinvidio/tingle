package com.plip.persistence.daos.impls;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PlipRoleNotFoundException;
import com.plip.persistence.daos.interfaces.PlipRoleDao;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.model.PlipRole;

public class PlipRoleDaoImpl implements PlipRoleDao {
	
	public PlipRoleDaoImpl() {
		super();
	}

	@Override
	public Long addRole(PlipRole role) throws NullModelAttributesException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		Long roleID = null;
		try {
			tx = session.beginTransaction();
			if(role.validate()){
			roleID = (Long) session.save(role);
			}else throw new NullModelAttributesException();
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
	public PlipRole getRole(long idRole) throws PlipRoleNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		PlipRole plipRole = null;
		try {
			tx = session.beginTransaction();
			Query query = session
					.createQuery("FROM PlipRole where idPlipRole = :id");
			query.setParameter("id", idRole);
			if(query.list().size() > 0){
			plipRole = (PlipRole) query.list().get(0);	
			}else{
				throw new PlipRoleNotFoundException();
			}
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
	public void updateRole(PlipRole role) throws PlipRoleNotFoundException {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PlipRole plipRole = (PlipRole) session.get(PlipRole.class,
					role.getIdPlipRole());
			if(plipRole != null){
			plipRole.setName(role.getName());
			plipRole.setPlipUsers(role.getPlipUsers());
			plipRole.setDescription(role.getDescription());
			session.update(plipRole);
			}else{
				throw new PlipRoleNotFoundException();
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
	public void deleteRole(long roleId) {
		SessionFactory factory = DaoManager.createSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PlipRole plipRole = (PlipRole) session.get(PlipRole.class, roleId);
			if(plipRole != null){
			session.delete(plipRole);	
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
