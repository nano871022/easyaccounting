package com.pyt.query.interfaces;

import java.sql.ResultSet;
import java.util.ServiceLoader;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.exceptions.QueryException;
/**
 * Conteiene script para realizar consultas de administracion sobre la base de datos
 * @author Alejandro Parra 
 * @since 06/06/2019
 */
public interface IAdvanceQuerySvc {
	public enum triggerOption {BEFORE,AFTER}
	public enum triggerAction {UPDATE,DELETE,SELECT}
	public <T extends ADto> void dropTable(T obj) throws QueryException;
	public <T extends ADto> void createTableStandard(T obj) throws QueryException;
	public ResultSet queryLaunch(String query) throws QueryException;
	public <T extends ADto> void createTrigger(Class<T> obj,triggerOption to,triggerAction... ta) throws QueryException;
	public void backup() throws Exception;
	public void runScript(String fileName) throws Exception;
	static Iterable<IAdvanceQuerySvc> getImplements() {
        return ServiceLoader.load(IAdvanceQuerySvc.class);
    }
}
