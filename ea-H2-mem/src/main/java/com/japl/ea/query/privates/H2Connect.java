package com.japl.ea.query.privates;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.RunScript;
import org.h2.tools.Script;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.pyt.common.common.Log;

import com.japl.ea.query.constants.H2PropertiesConstant;
import com.japl.ea.query.privates.Constants.QueryConstants;
import com.japl.ea.query.privates.utils.PropertiesH2;

import co.com.japl.ea.sql.impl.Analize;

/**
 * Se realiza clase para contectar a una base de datos H2 embebida en la
 * aplicacion en ejecucion en memoria para cuando no se cuenta con servidor de
 * BD
 * 
 * @author Alejando Parra
 * @since 09/05/2019
 */
public final class H2Connect implements AutoCloseable {
	private Log log = Log.Log(H2Connect.class);
	private String connect = QueryConstants.CONST_CONNECT_BDH2;
	private Connection connection;
	private Statement statement;
	private static H2Connect db;
	private PropertiesH2 properties = PropertiesH2.getInstance();
	private SessionFactoryBuild sessionFactoryBuild;

	private H2Connect() {
		try {
			this.connect = properties.load().getValue(H2PropertiesConstant.PROP_JDBC_H2);
		} catch (Exception e) {
			log.logger(e);
		}
	}

	public static H2Connect getInstance() {
		if (db == null) {
			db = new H2Connect();
		}
		return db;
	}

	/**
	 * Obtiene una conection a la base de datos solo la crea una vez
	 * 
	 * @return {@link Connection}
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		if (connection == null) {
			connection = DriverManager.getConnection(connect);
		}
		return connection;
	}

	public Statement getStatement() throws SQLException {
		if (statement == null) {
			statement = getConnection().createStatement();
		}
		return statement;
	}

	public Session getHibernateConnect() {
		if (sessionFactoryBuild == null) {
			sessionFactoryBuild = new SessionFactoryBuild();
		}
		SessionFactory sf = sessionFactoryBuild.buildSessionFactory();
		if (sf == null)
			return null;
		if (sf.isOpen())
			return sf.getCurrentSession();
		return sf.openSession();
	}

	public ResultSet executeQuery(String query) throws SQLException {
		Statement st = getStatement();
		ResultSet rs = st.executeQuery(query);
		return rs;
	}

	/**
	 * Se ejecuta cuando se raliza un insert, update o delete
	 * 
	 * @param query {@link String} sql
	 * @return {@link Boolean} true(Sql afecto registros)/false(sql no qfecto
	 *         registros)
	 * @throws {@link SQLException}
	 */
	public Boolean executeIUD(String query) throws SQLException {
		Statement st = getStatement();
		int rs = st.executeUpdate(query);
		if (query.contains(QueryConstants.CONST_DELETE))
			return true;
		return rs == 1;
	}

	public void cleanConnection() throws SQLException {
		getStatement().close();
		getConnection().close();
		connection = null;
	}

	protected void destroy() throws Exception {
		try {
			cleanConnection();
		} catch (SQLException e) {
			log.logger(e);
			throw new Exception(e);
		}
	}

	@Override
	public void close() throws Exception {
		destroy();
	}

	@SuppressWarnings("static-access")
	public void backup() throws Exception {
		var sc = new Script();
		var prop = PropertiesH2.getInstance().load();
		String password = prop.getValue(H2PropertiesConstant.PROP_HIBERNATE_PASSWORD);
		String user = prop.getValue(H2PropertiesConstant.PROP_HIBERNATE_USER_NAME);
		String fileName = prop.getValue(H2PropertiesConstant.PROP_BACKUP_FILE_NAME);
		;
		sc.process(connect, user, password, fileName, null, null);
	}

	public void runScript(String fileName) throws Exception {
		var prop = PropertiesH2.getInstance().load();
		var charset = Charset.defaultCharset();
		var continueOnError = true;
		String password = prop.getValue(H2PropertiesConstant.PROP_HIBERNATE_PASSWORD);
		String user = prop.getValue(H2PropertiesConstant.PROP_HIBERNATE_USER_NAME);
		RunScript.execute(connect, user, password, fileName, charset, continueOnError);
	}

	public final void verifyDB()
			throws URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		var map = new Analize().analizer();
		var keys = map.keySet();
		for (var key : keys) {
			try {
				var jpaClass = Class.forName("com.japl.ea.query.privates.jpa." + key + "JPA");
				if (jpaClass != null) {
					if (db.getHibernateConnect() != null) {
						if (!db.getHibernateConnect().getTransaction().isActive()) {
							db.getHibernateConnect().beginTransaction();
						}
						var builder = db.getHibernateConnect().getCriteriaBuilder();
						var querys = builder.createQuery(Long.class);
						var root = querys.from(jpaClass);
						querys.select(builder.count(root.get("codigo")));
						var create = db.getHibernateConnect().createQuery(querys);
						var result = create.getSingleResult();
						var count = Integer.valueOf(result.toString());
						System.out.println(key + " " + count);
					}
				} else {
					log.error("Clase no se encuentra " + key);
				}
			} catch (Exception e) {
				log.error(key);
				log.logger(e);
			}
		}
	}

}
