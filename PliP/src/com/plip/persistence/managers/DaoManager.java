package com.plip.persistence.managers;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DaoManager {
	
	public static SessionFactory factory;
	public static SessionFactory createSessionFactory() {
		if(factory == null){
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		}
		return factory;
	}

}
