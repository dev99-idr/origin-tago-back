package kr.co.j2.das.sql;

import java.sql.Connection;

public interface ConnectionFactoryInterface
{
	public Connection getConnection() throws Exception;
	public void freeConnection(Connection conn);
	public int getDbType();
}
