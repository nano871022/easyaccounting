package co.com.japl.ea.gdb.privates.impls;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import org.h2.jdbc.JdbcSQLSyntaxErrorException;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.Log;

import com.pyt.query.interfaces.IVerifyStructuredDB;

import co.com.japl.ea.gdb.privates.constants.QueryConstants;
import co.com.japl.ea.sql.impl.ScriptSql;

public class VerifyDB implements IVerifyStructuredDB {
	private ConnectionJDBC connectJDBC = null;
	private Log log = Log.Log(this.getClass());
	private final static String CONST_PACKAGE = "com.pyt.service.dto";
	private final static String[] CONST_SUB_PACKAGES = { "", "dels", "inventario", "upds" };
	private final static String CONST_DOT = ".";
	private final static String CONST_2_DOT = "..";
	private final static String CONST_DTO = "DTO";
	private final static String CONST_CLASS_NOT_FOUND = "Clase no se encuentra %s";
	private final static String CONST_NOT_FOUND = "not found";
	private final static String CONST_RUN_EXCEPTION_RUNSCRIPT = "Ejecutada la excepcion de runscript";
	private Integer posPackage = 0;
	private Integer countScript = 0;
	private Integer countRun = 0;

	public VerifyDB() {
		connectJDBC = ConnectionJDBC.getInstance();
	}

	/**
	 * Se encarga de realizar la busqueda de la clase dentro de todos los paquetes
	 * que contienen dtos
	 * 
	 * @param nameDto {@link String}
	 * @return {@link Class}
	 * @throws {@link ClassNotFoundException}
	 */
	@SuppressWarnings("unchecked")
	private final synchronized <T extends ADto> Class<T> getClassFromNameFileSql(String nameDto)
			throws ClassNotFoundException {
		Class<T> dtoClass = null;
		try {
			var pathAllDTO = CONST_PACKAGE + CONST_DOT + CONST_SUB_PACKAGES[posPackage] + CONST_DOT
					+ nameDto.substring(0, 1).toUpperCase() + nameDto.substring(1) + CONST_DTO;
			pathAllDTO = pathAllDTO.replace(CONST_2_DOT, CONST_DOT);
			dtoClass = (Class<T>) Class.forName(pathAllDTO);
			posPackage = 0;
		} catch (ClassNotFoundException e) {
			posPackage++;
			if (posPackage > CONST_SUB_PACKAGES.length) {
				throw e;
			}
			dtoClass = getClassFromNameFileSql(nameDto);
		}
		return dtoClass;
	}

	@SuppressWarnings("unchecked")
	private final <T extends ADto> void verifyRows(String nameDto) throws Exception {
		var dtoClass = getClassFromNameFileSql(nameDto);
		if (dtoClass != null) {
			var basicStatement = new BasicStatementSql<T>();
			var query = basicStatement.select_count((T) dtoClass.getConstructor().newInstance());
			var result = connectJDBC.executeQuery(query);
			Integer count = 0;
			while (result.next()) {
				count = result.getInt(QueryConstants.CONST_FIELD_COUNT);
			}
			if (count == 0) {
				insertRows(nameDto);
			}
		} else {
			log.error(String.format(CONST_CLASS_NOT_FOUND, nameDto));
		}
	}

	private final void verifyPersistenceException(Exception e, Map<String, File> map, String dtoName) {
		if (e.getCause() instanceof SQLException || e instanceof JdbcSQLSyntaxErrorException) {
			if (e.getMessage().contains(CONST_NOT_FOUND)) {
				var file = map.get(dtoName);
				try {
					connectJDBC.runScript(file.getAbsolutePath());
					insertRows(dtoName);
				} catch (Exception e1) {
					log.error(CONST_RUN_EXCEPTION_RUNSCRIPT);
					log.logger(e1);
				}
			}
		} else {
			log.logger(e);
		}

	}

	private final void insertRows(String nameJPA) {
		var map = ScriptSql.getInstance().getInserts();
		if (map.containsKey(nameJPA)) {
			var script = map.get(nameJPA);
			try {
				connectJDBC.runScript(script.getAbsolutePath());
			} catch (Exception e) {
				log.logger(e);
			}
		}
	}

	private final void changedVersions() {
		var map = ScriptSql.getInstance().getChangedVersions();
		var keys = map.keySet();
		countScript += keys.size();
		for (String nameJPA : keys) {
			if (map.containsKey(nameJPA)) {
				var script = map.get(nameJPA);
				var files = script.keySet();
				countScript += files.size();
				for (String fileName : files) {
					try {
						countRun++;
						connectJDBC.runScript(script.get(fileName).getAbsolutePath());
					} catch (Exception e) {
						log.logger(e);
					}
				}
			}
		}
	}

	public void verifyDB() throws Exception {
		var map = ScriptSql.getInstance().getTables();
		var key = 1;
		while (map.containsKey(key)) {
			var jpaMap = map.get(key);
			var keys = jpaMap.keySet();
			countScript += keys.size();
			for (String kkey : keys) {
				try {
					countRun++;
					verifyRows(kkey);
				} catch (Exception e) {
					verifyPersistenceException(e, jpaMap, kkey);
				}
			}
			key++;
		}
		changedVersions();
	}

	@Override
	public Integer countScripts() {
		return countScript;
	}

	@Override
	public Integer counScriptRuns() {
		return countRun;
	}
}
