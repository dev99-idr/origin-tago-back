package kr.co.j2.das.sql;

import java.sql.Connection;
import java.util.Hashtable;
import kr.co.j2.das.config.DAS3Config;
import kr.co.j2.das.config.DBCP;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

public class DBCPConnectionFactory implements ConnectionFactoryInterface
{
	private static Hashtable hConnectionFactory = new Hashtable();
	private static Object lock = new Object();
	//private static Hashtable hConnection = new Hashtable();

	private BasicDataSource basicDataSource = null;
	public int dbType = -1;

	static Logger logger = Logger.getLogger(DBCPConnectionFactory.class);

	public static DBCPConnectionFactory getInstance(String dbName)
	{
		DBCPConnectionFactory connectionFactory = null;

		synchronized (lock)
		{
			connectionFactory = (DBCPConnectionFactory)hConnectionFactory.get(dbName);

			if (connectionFactory == null)
			{
				connectionFactory = new DBCPConnectionFactory(dbName);
				hConnectionFactory.put(dbName, connectionFactory);
			}
		}

		return connectionFactory;
	}

	private DBCPConnectionFactory(String dbName)
	{
		DBCP dbcp = (DBCP)DAS3Config.hConfig.get(dbName);

		this.basicDataSource = new BasicDataSource();

		int dbType = dbcp.dbType;
		this.dbType = dbType;

		String driverClassName = dbcp.driverClassName;
		String url = dbcp.url;
		String username = dbcp.username;
		String password = dbcp.password;

		int minIdle = dbcp.minIdle;
		int maxActive = dbcp.maxActive;
		int maxWait = dbcp.maxWait;

		boolean poolPreparedStatements = dbcp.poolPreparedStatements;
		int maxOpenPreparedStatements = dbcp.maxOpenPreparedStatements;

		boolean removeAbandoned = dbcp.removeAbandoned;
		int removeAbandonedTimeout = dbcp.removeAbandonedTimeout;
		boolean logAbandoned = dbcp.logAbandoned;

		String validationQuery = dbcp.validationQuery;
		boolean testWhileIdle = dbcp.testWhileIdle;
		int minEvictableIdleTimeMillis = dbcp.minEvictableIdleTimeMillis;
		int timeBetweenEvictionRunsMillis = dbcp.timeBetweenEvictionRunsMillis;

		this.basicDataSource.setDriverClassName(driverClassName);
		this.basicDataSource.setUrl(url);
		this.basicDataSource.setUsername(username);
		this.basicDataSource.setPassword(password);

		this.basicDataSource.setMinIdle(minIdle);
		this.basicDataSource.setMaxActive(maxActive);
		this.basicDataSource.setMaxWait(maxWait);

		this.basicDataSource.setPoolPreparedStatements(poolPreparedStatements);
		this.basicDataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);

		this.basicDataSource.setRemoveAbandoned(removeAbandoned);
		this.basicDataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
		this.basicDataSource.setLogAbandoned(logAbandoned);

		this.basicDataSource.setValidationQuery(validationQuery);
		this.basicDataSource.setTestWhileIdle(testWhileIdle);
		this.basicDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		this.basicDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		//testOnBorrow

		this.basicDataSource.setAccessToUnderlyingConnectionAllowed(true);

		if (dbType == 1)
		{
			System.out.println("1");
			this.basicDataSource.setConnectionProperties("SetBigStringTryClob=true");
		}
	}

	public Connection getConnection() throws Exception
	{
		/*Connection conn = basicDataSource.getConnection();
		Connection dconn = ((org.apache.commons.dbcp.DelegatingConnection)conn).getInnermostDelegate();
		hConnection.put(dconn, conn);

		return dconn;*/
		return basicDataSource.getConnection();
	}

	public void freeConnection(Connection dconn)
	{
		/*if (dconn != null)
		{
			try
			{
				Connection conn = (Connection)hConnection.get(dconn);
				conn.close();
			} catch(Exception e)
			{
				logger.warn("", e);
			}
		}*/
		if (dconn != null)
		{
			try
			{
				dconn.close();
			} catch(Exception e)
			{
				logger.warn("", e);
			}
		}
	}

	public int getDbType()
	{
		return dbType;
	}
}
