package co.com.japl.ea.mybatis.privates;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import co.com.japl.ea.mybatis.privates.constants.MyBatisConstants;

public final class ConnectionMyBatis {

	private Reader reader;
	private SqlSessionFactory sqlSessionFactory;

	public ConnectionMyBatis() throws IOException {
		reader = Resources.getResourceAsReader(MyBatisConstants.CONST_SQL_MAP_CONFIG);

	}

	public SqlSession openConnection() {
		if (sqlSessionFactory == null) {
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		}
		return sqlSessionFactory.openSession();
	}

}
