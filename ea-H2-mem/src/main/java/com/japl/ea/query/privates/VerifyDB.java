package com.japl.ea.query.privates;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Map;

import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;
import org.pyt.common.common.Log;

import com.japl.ea.query.constants.H2PropertiesConstant;

import co.com.japl.ea.sql.impl.ScriptSql;

public class VerifyDB {
	private H2Connect h2 = H2Connect.getInstance();
	private Log log = Log.Log(this.getClass());
	
	private final void verifyRows(Session session, String nameJpa) throws PersistenceException, ClassNotFoundException {
		var jpaClass = Class.forName(H2PropertiesConstant.CONST_PACKAGE_JPA + H2PropertiesConstant.CONST_DOT + nameJpa
				+ H2PropertiesConstant.CONST_JPA);
		if (session != null) {
			if (!session.getTransaction().isActive()) {
				session.beginTransaction();
			}
			if (jpaClass != null) {
				var builder = session.getCriteriaBuilder();
				var querys = builder.createQuery(Long.class);
				var root = querys.from(jpaClass);
				querys.select(builder.count(root.get("codigo")));
				var create = session.createQuery(querys);
				var result = create.getSingleResult();
				var count = Integer.valueOf(result.toString());
				if(count == 0) {
					insertRows(nameJpa);
				}
				session.getTransaction().commit();
				session.close();
			} else {
				log.error("Clase no se encuentra " + nameJpa);
			}
		}
	}

	private final void verifyPersistenceException(Session session, PersistenceException e, Map<String, File> map,
			String jpaName) {
		if (e.getCause() instanceof SQLGrammarException) {
			if (e.getCause().getCause().getMessage().contains("not found")) {
				var file = map.get(jpaName);
				try {
					h2.runScript(file.getAbsolutePath());
					insertRows(jpaName);
					session.getTransaction().commit();
				} catch (Exception e1) {
					log.error("Ejeucada la excepcion de runscript");
					log.logger(e1);
				}
			}
		} else {
			log.logger(e);
		}

	}
	
	private final void insertRows(String nameJPA) {
		log.logger("Se realiza busqueda en inserts para ingresar nuevos registros para "+nameJPA);
	}

	public final void verifyDB()
			throws URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		var map = ScriptSql.getInstance().analizer();
		var key = 1;
		while (map.containsKey(key)) {
			var jpaMap = map.get(key);
			var keys = jpaMap.keySet();
			for (String kkey : keys) {
				var session = h2.getHibernateConnect();
				try {
					verifyRows(session, kkey);
				} catch (PersistenceException e) {
					verifyPersistenceException(session, e, jpaMap, kkey);
				} catch (Exception e) {
					log.error("Ejecutada la excepcion");
					log.logger(e);
				}
			}
			key++;
		}
	}
}
