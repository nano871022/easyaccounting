package com.japl.ea.query.privates.triggers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.h2.api.Trigger;

import com.japl.ea.query.privates.utils.StatementQuerysUtil;

public class BDParametroDTO implements Trigger {

	@Override
	public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
		var squ = new StatementQuerysUtil();
		var query = "INSERT INTO %s (ELIMINADOR,ACTUALIZADOR,CREADOR,FECHAELIMINACION,FECHAACTUALIZACION,FECHACREACION,CODIGO,ESTADO,GRUPO,VALOR2,VALOR,DESCRIPCION,NOMBRE,ORDEN,USUARIOELIMINA,FECHAELIMINA) VALUES (%s)";
		var values = "";
		for(int i = oldRow.length-1; i>= 0;i--) {
			if(values.length()>0)values+=", ";
			values += "?";
		}
		values +=", ?, ?";
		query = String.format(query, "MEM_ParametroDel",values);
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			for (int i =  oldRow.length-1;i >= 0; i--) {
				if(oldRow[i] instanceof Timestamp) {
					stmt.setObject((oldRow.length-1-i)+1, (oldRow[i]));
				}else{
				stmt.setObject((oldRow.length-1-i)+1, squ.valueFormat(oldRow[i]));
				}
			}
			stmt.setObject(15, squ.valueFormat(oldRow[oldRow.length-1]));
			stmt.setObject(16, LocalDateTime.now());
			stmt.executeUpdate();
		}
	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove() throws SQLException {
		// TODO Auto-generated method stub

	}

}
