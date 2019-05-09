package com.pyt.query.interfaces;

import java.sql.ResultSet;

import org.pyt.common.common.ADto;
import org.pyt.common.exceptions.QueryException;

public interface IAdvanceQuerySvc {
	public <T extends ADto> void dropTable(T obj) throws QueryException;
	public <T extends ADto> void createTableStandard(T obj) throws QueryException;
	public ResultSet queryLaunch(String query) throws QueryException;
}
