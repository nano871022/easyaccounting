package co.com.japl.ea.mybatis.privates;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public final class ConnectionMyBatis {

	private Reader reader;
	private static String CONST_SQL_MAP_CONFIG = "SqlMapCongif.xml";

	public ConnectionMyBatis() throws IOException {
		reader = Resources.getResourceAsReader(CONST_SQL_MAP_CONFIG);
	}
	
	public SqlSession openConnection() {
		var sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		return sqlSessionFactory.openSession();
	}

}
