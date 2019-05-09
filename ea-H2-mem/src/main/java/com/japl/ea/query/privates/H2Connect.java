package com.japl.ea.query.privates;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.pyt.common.common.Log;

/**
 * Se realiza clase para contectar a una base de datos H2 embebida en la
 * aplicacion en ejecucion en memoria para cuando no se cuenta con servidor de
 * BD
 * 
 * @author Alejando Parra
 * @since 09/05/2019
 */
public final class H2Connect implements AutoCloseable{
	private Log log = Log.Log(H2Connect.class);
	private String connect = "jdbc:h2:~/account.db";
	private Connection connection;
	private Statement statement;

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
		if(statement == null) {
			statement = getConnection().createStatement();
		}
		return statement;
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
		if(query.contains("DELETE"))return true;
		return rs == 1;
	}

	public void cleanConnection() throws SQLException {
		getStatement().close();
		getConnection().close();
		connection = null;
	}

    protected void destroy()throws Exception{ 
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

}
