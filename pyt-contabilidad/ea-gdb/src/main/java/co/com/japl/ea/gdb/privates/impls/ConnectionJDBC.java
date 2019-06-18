package co.com.japl.ea.gdb.privates.impls;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.RunScript;
import org.h2.tools.Script;
import org.pyt.common.common.Log;

import co.com.japl.ea.gdb.privates.constants.ConnectionPropertiesConstant;
import co.com.japl.ea.gdb.privates.constants.QueryConstants;
import co.com.japl.ea.gdb.privates.utils.ConnectionProperties;

/**
 * Se realiza clase para contectar a una base de datos H2 embebida en la
 * aplicacion en ejecucion en memoria para cuando no se cuenta con servidor de
 * BD
 * 
 * @author Alejando Parra
 * @since 09/05/2019
 */
public final class ConnectionJDBC implements AutoCloseable {
	private Log log = Log.Log(ConnectionJDBC.class);
	private String connect = QueryConstants.CONST_CONNECT_BDH2;
	private Connection connection;
	private Statement statement;
	private static ConnectionJDBC db;
	private ConnectionProperties properties = ConnectionProperties.getInstance();

	private ConnectionJDBC() {
		try {
			this.connect = properties.load().getValue(ConnectionPropertiesConstant.PROP_JDBC);
		} catch (Exception e) {
			log.logger(e);
		}
	}

	public static ConnectionJDBC getInstance() {
		if (db == null) {
			db = new ConnectionJDBC();
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
		var prop = ConnectionProperties.getInstance().load();
		String password = prop.getValue(ConnectionPropertiesConstant.PROP_PASSWORD);
		String user = prop.getValue(ConnectionPropertiesConstant.PROP_USER_NAME);
		String fileName = prop.getValue(ConnectionPropertiesConstant.PROP_BACKUP_FILE_NAME);
		;
		sc.process(connect, user, password, fileName, null, null);
	}

	public void runScript(String fileName) throws Exception {
		var prop = ConnectionProperties.getInstance().load();
		var charset = Charset.defaultCharset();
		var continueOnError = true;
		String password = prop.getValue(ConnectionPropertiesConstant.PROP_PASSWORD);
		String user = prop.getValue(ConnectionPropertiesConstant.PROP_USER_NAME);
		RunScript.execute(connect, user, password, fileName, charset, continueOnError);
	}

}
