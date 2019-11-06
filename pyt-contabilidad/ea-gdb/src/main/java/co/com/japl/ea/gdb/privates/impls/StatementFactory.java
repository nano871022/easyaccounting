package co.com.japl.ea.gdb.privates.impls;

import org.pyt.common.abstracts.ADto;

import co.com.japl.ea.gdb.privates.interfaces.IStatementSql;

/**
 * Fabrica que se encarga de encontrar el statement especial para realizar
 * ejecucion de sql dependiendo del motor de BD
 * 
 * @author Alejandro Parra
 * @since 2019-06-18
 */
public class StatementFactory {
	
	public final <T extends ADto> IStatementSql<T> getStatement(String motor, Class<T> obj) {
		IStatementSql<T> statement = null;
		switch (motor) {
		default:
			statement = new BasicStatementSql<T>();
			break;
		}
		return statement;
	}
}
