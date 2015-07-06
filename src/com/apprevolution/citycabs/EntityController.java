package com.apprevolution.citycabs;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class EntityController {

	public SessionFactory sessionFactory;
	private static EntityController controller;

	private EntityController() {
		createSessionFactory();
	}

	public static EntityController getInstance(){
		if(controller == null)
			controller = new EntityController();
		return controller;
	}

	public SessionFactory createSessionFactory() {
		if(sessionFactory == null){
			Configuration configuration = new Configuration();
			configuration.configure();
			configuration.addAnnotatedClass(GEOLocation.class);
			configuration.addAnnotatedClass(EOTrackLocation.class);
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		}
		return sessionFactory;
	}

	public void updateLocation(GEOLocation location){
		if(location!=null){
			Session session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(location);
			transaction.commit();
			session.close();
		}
	}

	public GEOLocation getLocation(int cabUid){
		Session session = sessionFactory.openSession();
		GEOLocation loc =  (GEOLocation) session.get(GEOLocation.class, cabUid);
		session.close();
		return loc;
	}

}
