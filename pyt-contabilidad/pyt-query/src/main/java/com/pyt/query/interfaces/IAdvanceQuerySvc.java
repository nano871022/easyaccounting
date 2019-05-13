package com.pyt.query.interfaces;

import java.sql.ResultSet;
import java.util.ServiceLoader;

import org.pyt.common.common.ADto;
import org.pyt.common.exceptions.QueryException;

public interface IAdvanceQuerySvc {
	public enum triggerOption {BEFORE,AFTER}
	public enum triggerAction {UPDATE,DELETE,SELECT}
	public <T extends ADto> void dropTable(T obj) throws QueryException;
	public <T extends ADto> void createTableStandard(T obj) throws QueryException;
	public ResultSet queryLaunch(String query) throws QueryException;
	public <T extends ADto> void createTrigger(Class<T> obj,triggerOption to,triggerAction... ta) throws QueryException;
	static Iterable<IAdvanceQuerySvc> getImplements() {
        return ServiceLoader.load(IAdvanceQuerySvc.class);
    }
}
