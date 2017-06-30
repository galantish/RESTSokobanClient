package db;

import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class DbManager {

	private static class DbManagerHolder{
		public static final DbManager instance = new DbManager();
	}
	
	public static DbManager getInstance() {
		return DbManagerHolder.instance;
	}

	private SessionFactory factory;

	private DbManager() {
		// to show the severe msg
		Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);

		// reading the xml so he can connect to the Db
		Configuration configuration = new Configuration();
		configuration.configure();
		factory = configuration.buildSessionFactory();
	}
	
	public LevelSolutionData getLevelSolution(String levelId)
	{
		Session session = null;
		LevelSolutionData levelSolData;
		try 
		{
			session = factory.openSession();
			@SuppressWarnings("rawtypes")
			Query query=session.createQuery("FROM LevelSolutionData as sol WHERE sol.levelId = :levelId ");
			query.setParameter("levelId", levelId);
			levelSolData =(LevelSolutionData) query.getSingleResult();
			
			System.out.println("LevelSol From DB: "+ levelSolData.toString());
			return levelSolData;
		} 
		catch (HibernateException ex) 
		{
			System.out.println(ex.getMessage());
		} 
		finally 
		{
			if (session != null)
				session.close();
		}
		return null;
	}
	
	
	
	public void add(Object obj) {
		Session session = null;
		Transaction tx = null;

		try {
			session = factory.openSession();
			tx = session.beginTransaction();

			session.save(obj);
			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			System.out.println(ex.getMessage());
		} finally {
			if (session != null)
				session.close();
		}
	}

}
